package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import scala.language.existentials

object GasPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.gas)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my()
      <.div("(gas page)")
    }
  }

  val component = ReactComponentB[Props]("Gas")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
