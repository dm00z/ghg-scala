package ghg.routes

import japgolly.scalajs.react.ReactElement

abstract class LeftRoute(val name: String, val route: String, val render: () => ReactElement, val indentLevel: Int = 0)
