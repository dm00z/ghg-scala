package d3charts

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.svg.prefix_<^._

object Chart {
  case class Props(width: Int, height: Int)

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props, children: PropsChildren) = {
      <.svg(
        ^.width := P.width,
        ^.height := P.height,
        children
      )
    }
  }
  val component = ReactComponentB[Props]("Chart")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(width: Int, height: Int)(children: ReactNode*) = component(Props(width, height), children: _*)
}
