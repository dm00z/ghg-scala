package ghg.pages

import chandu0101.scalajs.react.components.materialui.ThemeInstaller
import diode.react.ModelProxy
import ghg.components.LeftNavPage
import ghg.routes.{AppRoutes, AppRoute}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import model.GhgData

object GhgPage {
  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = LeftNavPage(AppRoutes.all, P.selectedPage, P.ctrl)
  }

  val component = ReactComponentB[Props]("MuiPage")
    .renderBackend[Backend]
    .configureSpec(ThemeInstaller.installMuiContext())
    .build

  case class Props(proxy: ModelProxy[GhgData], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute])

  def apply(proxy: ModelProxy[GhgData], selectedPage: AppRoute, ctrl: RouterCtl[AppRoute]) = component(Props(proxy, selectedPage, ctrl))
}
