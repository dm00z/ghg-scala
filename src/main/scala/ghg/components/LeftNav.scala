package ghg.components

import ghg.routes.AppRoute
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object LeftNav {
  object Style extends StyleSheet.Inline { import dsl._
    val container = style(
      display.flex,
      flexDirection.column,
      listStyle := "none",
      paddingLeft.`0`,
      marginBottom.`0`,
      flexWrap.wrap,
      minHeight :=! "100%"
    )


    val menuItem = styleF(Domain.ofValues(0, 1) *** Domain.boolean) {
      case (indentLevel, selected) => styleS(
        position.relative,
        lineHeight(40.px),
        padding :=! "0 25px",
        cursor.pointer,
        textDecoration := "none",
        marginLeft((indentLevel * 17).px),


        mixinIfElse(selected)(
          color.white,
          backgroundColor(c"#3d7ed5")
        )(color.white,
          &.hover(
            color(c"#555555"),
            backgroundColor(c"#3d7ed5")
          )
        ),
        mixinIf(indentLevel == 0)(fontWeight.bold, color.white),
        mixinIf(indentLevel != 0)(color(c"#bac4d1")),
        transition := "background .3s ease-in-out"
      )
    }


    val sidebarNav = style(
      position.relative,
      flex := "1 1",
      overflowX.hidden,
      overflowY.auto,
      width.auto,
      flexDirection.column,
      minHeight.maxContent,
      padding.`0`,
      transition := "width .25s"
    )

    val sideSpan = style(
      position.relative,
      fontWeight._700,
      backgroundColor(c"#124e9f"),
      minHeight.maxContent,
      lineHeight(35.px),
      padding :=! "0 25px"
    )
  }

  case class Props(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  case class Backend($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.nav(Style.sidebarNav)(
        <.span(Style.sideSpan)("Menu"),
        <.ul(Style.container)(
          P.menus.map { item =>
            <.li(
              ^.key := item.name,
              Style.menuItem(if(item.subGroup == null) 0 else 1, item == P.selectedPage),
              item.name,
              P.ctrl setOnClick item
            )
          }
        )
      )
    }
  }
  val component = ReactComponentB[Props]("LeftNav")
    .renderBackend[Backend]
    .build

  def apply(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute]) = component(Props(menus, selectedPage, ctrl))

//  def apply(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute],
//            ref: UndefOr[String] = "",
//            key: js.Any = {}) = component.set(key, ref)(Props(menus, selectedPage, ctrl))
}
