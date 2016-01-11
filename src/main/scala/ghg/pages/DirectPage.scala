package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import ghg.Utils._
import reactd3.ChartSerie

import scala.scalajs.js

object DirectPage {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
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

    def render(d: Props) = {
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

      val b2 = d.bien2Ae //fixme anaerobic
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

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
