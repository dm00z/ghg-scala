package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

object AppRouter {
  private val config = RouterConfigDsl[AppRoute].buildConfig { dsl =>
    import dsl._

    val routes = AppRoutes.all.map(i =>
      staticRoute("#/" + i.route, i) ~> renderR(r => MuiPage(i, r))
    ).reduce(_ | _)

    routes.notFound(redirectToPage(AppRoutes.Info)(Redirect.Replace))
    .renderWith(layout)
  }

  def layout(c: RouterCtl[AppRoute], r: Resolution[AppRoute]) = {
    <.div(r.render(),
      <.div(^.textAlign := "center", ^.key := "footer")(
        <.hr(),
        <.p("Nguyễn Vân Anh")))
  }

  val router = Router(BaseUrl.fromWindowOrigin_/, config)
}
