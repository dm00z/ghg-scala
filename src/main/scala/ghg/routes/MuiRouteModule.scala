package ghg.routes

import ghg.pages.{MuiPage, DirectPage, IndirectPage, InfoPage}
import japgolly.scalajs.react.extra.router.RouterConfigDsl

object MuiRouteModule {
  case object Info      extends LeftRoute("1. Thông tin nhà máy", "info", InfoPage.apply)
  case object Indirect  extends LeftRoute("2. Gián tiếp", "indirect", IndirectPage.apply)
  case object Direct    extends LeftRoute("3. Trực tiếp", "direct", DirectPage.apply)

  val menu: List[LeftRoute] = List(Info, Indirect, Direct)

  val routes = RouterConfigDsl[LeftRoute].buildRule { dsl =>
    import dsl._
    menu.map(i =>
      staticRoute(i.route, i) ~> renderR(r => MuiPage(i, r))
    ).reduce(_ | _)
  }
}
