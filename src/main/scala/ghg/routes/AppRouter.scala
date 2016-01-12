package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.AppCircuit

object AppRouter {
  private val config = RouterConfigDsl[AppRoute].buildConfig { dsl =>
    import dsl._

    def route(s: AppRoute): Rule =
      staticRoute("#/" + s.route, s) ~> renderR { ctrl =>
        AppCircuit.connect(d => d)(p => GhgPage(p, s, ctrl))
      }

    val routes = AppRoutes.all.map(route).reduce(_ | _)

    routes.notFound(redirectToPage(AppRoutes.Info)(Redirect.Replace))
    .renderWith(layout)
  }

  def layout(c: RouterCtl[AppRoute], r: Resolution[AppRoute]) = {
    <.div(r.render(),
      <.div(^.textAlign := "center", ^.key := "footer")(
        <.hr(),
        <.p("Nguyễn Vân Anh")))
  }

  val router = Router(BaseUrl.until_#, config)
}
