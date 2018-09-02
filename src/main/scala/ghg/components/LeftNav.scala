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

//    .sidebar .nav {
//      width: 200px;
//      -ms-flex-direction: column;
//      flex-direction: column;
//      min-height: 100%;
//      padding: 0;
//    }
//
//      .nav {
//        display: -ms-flexbox;
//        display: flex;
//        -ms-flex-wrap: wrap;
//        flex-wrap: wrap;
//        padding-left: 0;
//        margin-bottom: 0;
//        list-style: none;
//      }

    val menuItem = styleF(Domain.ofValues(0, 1) *** Domain.boolean) {
      case (indentLevel, selected) => styleS(
        position.relative,
        lineHeight(40.px),
        padding :=! "0 25px",
        cursor.pointer,
        textDecoration := "none",

        marginLeft((indentLevel * 5).px),
        mixinIf(indentLevel == 0)(fontWeight.bold),

        mixinIfElse(selected)(
          color.white,
          backgroundColor(c"#304047")
        )(color.black,
          &.hover(
            color(c"#555555"),
            backgroundColor(c"#ecf0f1")
          )
        ),

        transition := "background .3s ease-in-out",
        color.white
      )
    }

//    position: relative;
//    margin: 0;
//    -webkit-transition: background .3s ease-in-out;
//    -o-transition: background .3s ease-in-out;
//    transition: background .3s ease-in-out;

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

//    position: relative;
//    -ms-flex: 1 1;
//    flex: 1 1;
//    overflow-x: hidden;
//    overflow-y: auto;
//    width: 200px;
  }

  case class Props(menus: List[AppRoute], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  case class Backend($: BackendScope[Props, _]){
    def render(P: Props) = {
      <.nav(Style.sidebarNav)(
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
