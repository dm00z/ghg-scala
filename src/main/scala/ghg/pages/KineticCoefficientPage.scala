package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{KineticCoefficientData, GhgData}
import scala.scalajs.js
import tex.TeX._

object KineticCoefficientPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  import ghg.Utils._
  import KineticCoefficientData._

//  implicit class KTEx(val k: KT) extends AnyVal {
//    def tdV = <.td(k.vNorm)
//    def tdCo = <.td(k.coeff)
//  }

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

      val p = P.my()

      <.div(
        <.h3("1. Quá trình hiếu khí"),
        aerobicTbl(p.aerobic),
        <.div(^.marginTop := 10.px,
          <.span(^.color := "green", ^.marginLeft := 140.px, "μ", <.sub("m")),
          <.span(^.color := "blue", ^.marginLeft := 280.px, "k", <.sub("d")),
          <.span(^.color := "red", ^.marginLeft := 290.px, "`k = mu_m / Y`".teX)
        ),
        KTGrapth(p.aerobic.m, "m", "green"),
//        KTGrapth(p.aerobic.ks, "ks"),
        KTGrapth(p.aerobic.kd, "kd", "blue"),
//        KTGrapth(p.aerobic.y, "y"),
        KTGrapth(p.aerobic.k, "k", "red"),
        <.h3("2. Quá trình nitrat và khử nitrat"),
        nitratTbl(p.nitrate),
        <.h3("3. Quá trình yếm khí"),
        anaerobicTbl(p.anaerobic)
      )
    }
  }

  val component = ReactComponentB[Props]("KineticCoefficient")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}

object KTGrapth {
  import reactd3._

  def apply(f: Double => Double, field: String, color: String) = {
    val data = for(t <- 20 to 40)
      yield js.Dynamic.literal("t" -> t, field -> f(t))
    val chartSeries = js.Array(ChartSerie(field, color))
    val otherProps = js.Dynamic.literal(
      width = 300, height = 260,
      margins = js.Dynamic.literal(
        top = 20, bottom = 20, left = 40, right = 40
      ))
    LineChart(
      data.toJsArray,
      (d: js.Object with js.Dynamic) => d.t.asInstanceOf[Double],
      chartSeries)(otherProps)
  }
}
