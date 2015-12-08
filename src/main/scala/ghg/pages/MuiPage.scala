package ghg.pages

import chandu0101.scalajs.react.components.materialui.ThemeInstaller
import ghg.components.LeftNavPage
import ghg.routes.{AppRoutes, AppRoute}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl

object MuiPage {
  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = LeftNavPage(AppRoutes.all, P.selectedPage, P.ctrl)
  }

  val component = ReactComponentB[Props]("MuiPage")
    .renderBackend[Backend]
    .configureSpec(ThemeInstaller.installMuiContext())
    .build

  case class Props(selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  def apply(selectedPage: AppRoute, ctrl: RouterCtl[AppRoute]) = component(Props(selectedPage, ctrl))
}
