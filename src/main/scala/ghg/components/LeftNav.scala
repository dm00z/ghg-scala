package ghg.components

import ghg.routes.AppRoute
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object LeftNav {
  object Style extends StyleSheet.Inline { import dsl._
    val container = style(display.flex,
      flexDirection.column,
      listStyle := "none",
      padding.`0`)

    val menuItem = styleF(Domain.ofValues(0, 1) *** Domain.boolean) {
      case (indentLevel, selected) => styleS(
        lineHeight(48.px),
        padding :=! "0 25px",
        cursor.pointer,
        textDecoration := "none",

        marginLeft((indentLevel * 5).px),
        mixinIf(indentLevel == 0)(fontWeight.bold),

        mixinIfElse(selected)(
          color.red
        )(color.black,
          &.hover(
            color(c"#555555"),
            backgroundColor(c"#ecf0f1")
          )
        )
      )
    }
  }

  case class Props(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  case class Backend($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.ul(Style.container)(
        P.menus.map { item =>
          <.li(
            ^.key := item.name,
            Style.menuItem(item.indentLevel, item == P.selectedPage),
            item.name,
            P.ctrl setOnClick item
          )
        }
      )
    }
  }
  val component = ReactComponentB[Props]("LeftNav")
    .renderBackend[Backend]
    .build

  def apply(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute],
            ref: UndefOr[String] = "",
            key: js.Any = {}) = component.set(key, ref)(Props(menus, selectedPage, ctrl))
}
