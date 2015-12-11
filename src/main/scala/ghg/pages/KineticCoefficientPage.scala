package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{KineticCoefficientData, GhgData}

object KineticCoefficientPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  import ghg.Utils._
  import KineticCoefficientData._

  implicit class KTEx(val k: KT) extends AnyVal {
    def tdV = <.td(k.vNorm)
    def tdCo = <.td(k.coeff)
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
          <.tr(td("k", "d"), Ngay1, <.td(Aerobic.Kd.range.text),
            tdInput(Aerobic.kd ^|-> KT.vNorm, _.between(Aerobic.Kd.range)),
            tdInput(Aerobic.kd ^|-> KT.coeff))
          //        <.tr(<.td(Muy, "/Y"), <.td(), <.td(), <.td(), <.td()),
        )
      }

      def nitratTbl(d: Nitrate) = table(

      )

      val p = P.my()
      <.div(
        <.h3("1. Quá trình hiếu khí"),
        aerobicTbl(p.aerobic),
        <.h3("2. Quá trình nitrat và khử nitrat"),
        nitratTbl(p.nitrate),
        <.h3("3. Quá trình yếm khí ")
      )
    }
  }

  val component = ReactComponentB[Props]("KineticCoefficient")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
