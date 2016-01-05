package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{KineticCoefficientData, GhgData}
import reactd3.ChartSerie
import scala.scalajs.js
import tex.TeX._
import ghg.Utils._
import KineticCoefficientData._


object KineticCoefficientPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  @inline def td2(s: String) = <.td(^.rowSpan := 2, <.b(s))

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      def aerobicTbl(implicit d: Aerobic) = {
        @inline implicit def dispatch: Aerobic => Callback = P.my.dispatch
        table(
          <.tr(
            td2("Thông số"),
            td2("Đơn vị"),
            <.td(^.colSpan := 2, <.b("Giá trị tiêu chuẩn ở 20°C")),
            td2("Hệ số nhiệt độ θ")
          ),
          <.tr(Khoang, GiaTri),
          <.tr(<.td(Muy), Ngay1, <.td(),
            tdInput(Aerobic.m ^|-> KT.vNorm),
            tdInput(Aerobic.m ^|-> KT.coeff)),
          <.tr(<.td("Y"), <.td("mg/mg"), <.td(Aerobic.Y.range.text),
            tdInput(Aerobic.y ^|-> KT.vNorm, _.between(Aerobic.Y.range)),
            tdInput(Aerobic.y ^|-> KT.coeff)),
          <.tr(td("K", "S"), <.td("mg/l"), <.td(Aerobic.Ks.range.text),
            tdInput(Aerobic.ks ^|-> KT.vNorm, _.between(Aerobic.Ks.range)),
            <.td()
          ),
          <.tr(td("k", "d"), Ngay1, <.td(Aerobic.Kd.range.text),
            tdInput(Aerobic.kd ^|-> KT.vNorm, _.between(Aerobic.Kd.range)),
            tdInput(Aerobic.kd ^|-> KT.coeff))
          //        <.tr(<.td(Muy, "/Y"), <.td(), <.td(), <.td(), <.td()),
        )
      }

      def nitratTbl(implicit d: Nitrate) = {
        @inline implicit def dispatch: Nitrate => Callback = P.my.dispatch
        table(
          <.tr(
            td2("Thông số"),
            td2("Đơn vị"),
            <.td(^.colSpan := 2, <.b("Giá trị tiêu chuẩn ở 20°C"))
          ),
          <.tr(Khoang, GiaTri),
          <.tr(td("μ", "m,nit"), Ngay1,
            <.td(Nitrate.M.range.text),
            tdInput(Nitrate.m ^|-> MT.vNorm, _.between(Nitrate.M.range))),
          <.tr(td("Y", "nit"), <.td("mg/mg"),
            <.td(Nitrate.Y.range.text),
            tdInput(Nitrate.y ^|-> KT.vNorm, _.between(Nitrate.Y.range))),
          <.tr(td("k", "d,nit"), Ngay1,
            <.td(Nitrate.Kd.range.text),
            tdInput(Nitrate.kd ^|-> KT.vNorm, _.between(Nitrate.Kd.range)))
        )
      }

      def anaerobicTbl(implicit d: Anaerobic) = {
        @inline implicit def dispatch: Anaerobic => Callback = P.my.dispatch
        table(
          <.tr()
        )
      }

      def aerobicGraph(d: Aerobic) = MGraph(
        <.div(^.marginTop := 10.px,
          <.span(^.color := "green", ^.marginLeft := 140.px, "μ", <.sub("m")),
          <.span(^.color := "blue", ^.marginLeft := 260.px, "k", <.sub("d")),
          <.span(^.color := "red", ^.marginLeft := 240.px, "`k = mu_m / Y`".teX)
        ),
        (20 to 40).map(t => js.Dynamic.literal(
          "t" -> t,
          "m" -> d.m(t),
          "kd" -> d.kd(t),
          "k" -> d.k(t)
        )).toJsArray,
        ChartSerie("m", "green"),
        ChartSerie("kd", "blue"),
        ChartSerie("k", "red")
      )

      def nitratGraph(d: Nitrate) = MGraph(
        <.div(^.marginTop := 10.px,
          <.span(^.color := "green", ^.marginLeft := 20.px, s"`mu_(m,nit)(t) = mu_(m,nit)(${d.m.tNorm}) * e^(0.098 * (t-${d.m.tNorm}))`".teX),
          <.span(^.color := "blue", ^.marginLeft := 60.px, s"`k_(d,nit)(t) = k_(d,nit)(${d.kd.tNorm}) * theta ^ (t-${d.kd.tNorm})`"),
          <.span(^.color := "red", ^.marginLeft := 100.px, "`k = mu_(m,nit) / Y`".teX),
          <.span(^.color := "black", ^.marginLeft := 172.px, "`K_N(t) = 10^(0.05*t - 1.185)`".teX)
        ),
        (20 to 40).map(t => js.Dynamic.literal(
          "t" -> t,
          "m" -> d.m(t),
          "kd" -> d.kd(t),
          "k" -> d.k(t),
          "kn" -> d.kn(t)
        )).toJsArray,
        ChartSerie("m", "green"),
        ChartSerie("kd", "blue"),
        ChartSerie("k", "red"),
        ChartSerie("kn", "black")
      )

      def anaerobicGraph(d: Anaerobic) = <.div(

      )

      val p = P.my()

      <.div(
        <.h3("1. Quá trình hiếu khí"),
        aerobicTbl(p.aerobic),
        aerobicGraph(p.aerobic),
        <.h3("2. Quá trình nitrat và khử nitrat"),
        nitratTbl(p.nitrate),
        nitratGraph(p.nitrate),
//        <.h3("3. Quá trình yếm khí"), TODO impl
        anaerobicTbl(p.anaerobic),
        anaerobicGraph(p.anaerobic)
      )
    }
  }

  val component = ReactComponentB[Props]("KineticCoefficient")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
