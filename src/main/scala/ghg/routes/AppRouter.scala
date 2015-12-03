package ghg.routes

import ghg.components.AppHeader
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

object AppRouter {
  sealed trait Page
  case class MuiPages(p: LeftRoute) extends Page

  val config = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    val muiRoutes: Rule = MuiRouteModule.routes.prefixPath_/("#").pmap[Page](MuiPages) { case MuiPages(p) => p }
    (trimSlashes | muiRoutes)
      .notFound(redirectToPage(MuiPages(MuiRouteModule.Info))(Redirect.Replace))
      .renderWith(layout)
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) = {
    <.div(
      AppHeader(),
      r.render(),
      <.div(^.textAlign := "center", ^.key := "footer")(
        <.hr(),
        <.p("Nguyễn Vân Anh")))
  }

  val router = Router(BaseUrl.fromWindowOrigin_/, config)
}
