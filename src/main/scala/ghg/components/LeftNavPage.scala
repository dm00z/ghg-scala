package ghg.components

import ghg.routes.AppRoute
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js
import scalacss.Defaults._
import scalacss.ScalaCssReact._

//fixme naming. This component is not only `left`
object LeftNavPage {
  object Style extends StyleSheet.Inline { import dsl._
    val container = style(
      display.flex,
      minHeight(600.px))
    val nav = style(width(190.px))
    val content = style(
      padding(10.px),
      borderLeft :=! "1px solid rgb(223, 220, 220)",
      flex := "1")
  }

  case class Backend($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.div(Style.container,
        <.div(Style.nav, LeftNav(P.menu, P.selectedPage, P.ctrl)),
        <.div(Style.content, P.selectedPage.render())
      )
    }
  }
  val component = ReactComponentB[Props]("LeftNavPage")
    .renderBackend[Backend]
    .build

  case class Props(menu: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  def apply(menu: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute],
            ref: js.UndefOr[String] = "",
            key: js.Any = {}) = component.set(key, ref)(Props(menu, selectedPage, ctrl))
}
