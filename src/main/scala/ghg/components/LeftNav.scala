package ghg.components

import diode.react.ModelProxy
import ghg.routes.AppRoute
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import model.{GhgData, TechMethod}

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

    val hiddenNav = style(
      display.none
    )

    val visibleNav = style(
      display.listItem
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

  case class BackendAe($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.nav(Style.sidebarNav)(
        <.span(Style.sideSpan)("Menu"),
        <.ul(Style.container)(
          P.menus.map { item =>
            <.li(
              ^.key := item.name,
              ^.id := item.route,
              if(item.route == "anaerobic") Style.hiddenNav else Style.visibleNav,
              Style.menuItem(if(item.subGroup == null) 0 else 1, item == P.selectedPage),
              item.name,
              P.ctrl setOnClick item
            )
          }
        )
      )
    }
  }

  case class BackendAna($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.nav(Style.sidebarNav)(
        <.span(Style.sideSpan)("Menu"),
        <.ul(Style.container)(
          P.menus.map { item =>
            <.li(
              ^.key := item.name,
              ^.id := item.route,
              if(item.route == "aerobic") Style.hiddenNav else Style.visibleNav,
              Style.menuItem(if(item.subGroup == null) 0 else 1, item == P.selectedPage),
              item.name,
              P.ctrl setOnClick item
            )
          }
        )
      )
    }
  }


  val componentAe = ReactComponentB[Props]("LeftNav")
    .renderBackend[BackendAe]
    .build
  val componentAna = ReactComponentB[Props]("LeftNav")
    .renderBackend[BackendAna]
    .build

  def apply(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute], m: ModelProxy[GhgData]) = {
    val d = m()
    if (d.info.tech == TechMethod.Ae)
      componentAe(Props(menus, selectedPage, ctrl))
    else
      componentAna(Props(menus, selectedPage, ctrl))
  }

//  def apply(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute],
//            ref: UndefOr[String] = "",
//            key: js.Any = {}) = component.set(key, ref)(Props(menus, selectedPage, ctrl))
}
