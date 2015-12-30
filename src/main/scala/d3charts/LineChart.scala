package d3charts

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Attr
import japgolly.scalajs.react.vdom.svg.prefix_<^._

object LineChart {
  private val _d = Attr("d")

  case class Props(path: String, color: String, width: Int)

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      <.path(
        ^.stroke := P.color,
        ^.strokeWidth := P.width,
        ^.fill := "none",
        _d := P.path
      )
    }
  }
  val component = ReactComponentB[Props]("LineChart")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(path: String, color: String = "blue", width: Int = 2) =
    component(Props(path, color, width))
}
