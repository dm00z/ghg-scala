package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object KineticCoefficientPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my()
      <.div(
        <.h3("1. Quá trình hiếu khí"),
        <.h3("2. Quá trình nitrat và khử nitrat"),
        <.h3("3. Quá trình yếm khí ")
      )
    }
  }

  val component = ReactComponentB[Props]("KineticCoefficient")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
