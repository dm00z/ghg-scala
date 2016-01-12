package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{TechMethod, GhgData}
import ghg.Utils._
import reactd3.ChartSerie
import scala.scalajs.js

object DirectPage {
  case class BackendAe($: BackendScope[GhgData, _]) {
    def graph(r: Range, f: Double => GhgData) = MGraph(
      <.div(^.marginTop := 10.px,
        <.span(^.color := "green", ^.marginLeft := 44.px, "CO", <.sub("2"), "(bể sinh học)"),
        <.span(^.color := "blue", ^.marginLeft := 220.px, "CO", <.sub("2"), "(bể phân hủy)"),
        <.span(^.color := "red", ^.marginLeft := 190.px, "KNK", <.sub("trực_tiếp"))
      ),
      r.map { t =>
        val d = f(t)
        val b2 = d.bien2Ae
        val b3 = d.bien3
        js.Dynamic.literal(
          "t" -> t,
          "bio" -> (b2.co2_quaTrinh + b2.co2_n2o) / 1000,
          "decay" -> b3.co2_phanHuyTotal / 1000,
          "knk" -> b3.knk_direct / 1000
        )
      }.toJsArray, true,
      ChartSerie("bio", "green"),
      ChartSerie("decay", "blue"),
      ChartSerie("knk", "red")
    )

    def render(d: GhgData) = {
      def d_t(t: Double) = d.copy(
        direct = d.direct.copy(
          coef = d.direct.coef.copy(
            aerobic = d.direct.coef.aerobic.copy(t = t),
            nitrate = d.direct.coef.nitrate.copy(t = t)
          )))

      def d_bod(bod: Double) = d.copy(
        direct = d.direct.copy(
          d = d.direct.d.copy(
            streamIn = d.direct.d.streamIn.copy(s = bod)
          )))

      def d_srt(srt: Double) = d.copy(
        direct = d.direct.copy(
          d = d.direct.d.copy(
            aerobicPool = d.direct.d.aerobicPool.map(_.copy(srt = srt))
          )))

      val b2 = d.bien2Ae
      val b3 = d.bien3

      <.div(
        <.h2("Tổng lượng KNK phát sinh từ hệ xử lý hiếu khí"),
        table(
          tr("CO", "2,quaTrinhHieuKhi", b2.co2_quaTrinh, "g/day"),
          tr("CO", "2,N2OphatThai", b2.co2_n2o, "g/day"),
          tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct / 1000, "kg/day")
        ),
        <.h4("Biểu đồ biến thiên theo nhiệt độ của quá trình xử lý"),
        graph(10.to(30, 2), d_t),
        <.h4("Biểu đồ biến thiên theo BOD"),
        graph(50.to(350, 25), d_bod),
        <.h4("Biểu đồ biến thiên theo tuổi bùn"),
        graph(5 to 15, d_srt)
      )
    }
  }

  case class BackendAn($: BackendScope[GhgData, _]) {
    def graph(r: Range, f: Double => GhgData) = MGraph(
      <.div(^.marginTop := 10.px,
        <.span(^.color := "green", ^.marginLeft := 44.px, "CO", <.sub("2"), "(bể sinh học)"),
        <.span(^.color := "blue", ^.marginLeft := 220.px, "CO", <.sub("2"), "(bể phân hủy)"),
        <.span(^.color := "red", ^.marginLeft := 190.px, "KNK", <.sub("trực_tiếp"))
      ),
      r.map { t =>
        val d = f(t)
        val b2 = d.bien2Ana
        val b3 = d.bien3Ana
        js.Dynamic.literal(
          "t" -> t,
          "bio" -> (b2.co2_quaTrinh + b2.ch4_beYemKhi * 25) / 1000, //ch4 * 25 ~ co2
          "decay" -> (b3.co2_bePhanHuy + 25 * b3.ch4_bePhanHuy) / 1000,
          "knk" -> b3.knk_direct / 1000
        )
      }.toJsArray, true,
      ChartSerie("bio", "green"),
      ChartSerie("decay", "blue"),
      ChartSerie("knk", "red")
    )

    def render(d: GhgData) = {
      def d_t(t: Double) = d.copy(
        direct = d.direct.copy(
          coef = d.direct.coef.copy(
            anaerobic = d.direct.coef.anaerobic.copy(t_an = t)
          )))

      def d_bod(bod: Double) = d.copy(
        direct = d.direct.copy(
          d = d.direct.d.copy(
            streamIn = d.direct.d.streamIn.copy(s = bod)
          )))

      def d_srt(srt: Double) = d.copy(
        direct = d.direct.copy(
          d = d.direct.d.copy(
            anaerobicPool = d.direct.d.anaerobicPool.map(_.copy(srt = srt))
          )))

      val b2 = d.bien2Ana
      val b3 = d.bien3Ana

      <.div(
        <.h2("Tổng lượng KNK phát sinh từ hệ xử lý yếm khí"),
        table(
          tr("CO", "2,beYemKhi", b2.co2_quaTrinh, "g/day"),
          tr("CO", "2,bePhanHuy", b3.co2_bePhanHuy, "g/day"),
          tr("CO", "2,tuongDuong", b3.co2_phanHuyMetan, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct / 1000, "kg/day")
        ),
        <.h4("Biểu đồ biến thiên theo nhiệt độ của quá trình xử lý"),
        graph(30.to(50, 2), d_t),
        <.h4("Biểu đồ biến thiên theo BOD"),
        graph(400.to(1000, 50), d_bod),
        <.h4("Biểu đồ biến thiên theo tuổi bùn"),
        graph(15 to 35, d_srt)
      )
    }
  }

  val componentAe = ReactComponentB[GhgData]("DirectAe")
    .renderBackend[BackendAe]
    .build

  val componentAn = ReactComponentB[GhgData]("DirectAn")
    .renderBackend[BackendAn]
    .build

  def apply(m: ModelProxy[GhgData]) = {
    val d = m()
    if (d.info.tech == TechMethod.Ae)
      componentAe(d)
    else
      componentAn(d)
  }
}
