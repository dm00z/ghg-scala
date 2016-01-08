package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AerobicPage {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp???")
      )
    }
  }

  val component = ReactComponentB[Props]("Aerobic")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
