package ghg.routes

import ghg.components.AppHeader
import japgolly.scalajs.react.{ReactComponentB, ReactElement}
import japgolly.scalajs.react.vdom.prefix_<^._

abstract class LeftRoute(val name: String, val route: String, pageRender: () => ReactElement, val indentLevel: Int = 0) {
  val render = ReactComponentB[Unit]("LeftRoute")
    .render(_ => <.div(AppHeader(), pageRender()))
    .buildU
}
