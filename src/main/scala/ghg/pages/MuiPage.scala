package ghg.pages

import chandu0101.scalajs.react.components.materialui.ThemeInstaller
import ghg.components.LeftNavPage
import ghg.routes.{LeftRoute, AppRouter}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl

object MuiPage {
  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = LeftNavPage(AppRouter.menu, P.selectedPage, P.ctrl)
  }

  val component = ReactComponentB[Props]("MuiPage")
    .renderBackend[Backend]
    .configureSpec(ThemeInstaller.installMuiContext())
    .build

  case class Props(selectedPage: LeftRoute, ctrl: RouterCtl[LeftRoute])

  def apply(selectedPage: LeftRoute, ctrl: RouterCtl[LeftRoute]) = component(Props(selectedPage, ctrl))
}
