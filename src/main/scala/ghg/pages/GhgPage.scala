package ghg.pages

import diode.react.ModelProxy
import ghg.components.{AppHeader, LeftNav}
import ghg.routes.{AppRoute, AppRoutes}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object GhgPage {
  object Style extends StyleSheet.Inline { import dsl._
    val container = style(
      fontFamily := "-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif",
      fontSize(14.px),
      display.flex,
      minHeight(94.vh),
      backgroundColor.white,
      height :=! "100%"
    )

    val nav = style(
      display.flex,
      position.fixed,
      flexDirection.column,
      padding.`0`,
      width(300.px),
      backgroundColor(c"#145dbf"),
      color(c"#fff"),
      height :=! "100%"

    )

    val content = style(
      marginLeft(320.px),
      marginTop(20.px),
      marginRight(20.px),
      paddingLeft(10.px),
      paddingRight(10.px),
      paddingTop(5.px),
      paddingBottom(5.px),
      border :=! "3px solid rgb(223, 220, 220)",
      flex := "1")
  }

  case class Backend($: BackendScope[Props, _]) {

    def render(P: Props) = {
      <.div(Style.container,
        <.div(Style.nav, LeftNav(AppRoutes.all, P.selectedPage, P.ctrl, P.proxy)),
        <.div(Style.content,
          AppHeader.component(AppHeader.Props(
            P.proxy, P.selectedPage.group, Option(P.selectedPage.subGroup), P.selectedPage != AppRoutes.Info
          )),
          P.selectedPage.render(P.proxy)
        )
      )
    }
  }

  val component = ReactComponentB[Props]("GhgPage")
    .renderBackend[Backend]
    .build

  case class Props(proxy: ModelProxy[GhgData], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  def apply(proxy: ModelProxy[GhgData], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute]) = component(Props(proxy, selectedPage, ctrl))
}
