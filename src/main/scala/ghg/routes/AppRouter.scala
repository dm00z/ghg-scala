package ghg.routes

import ghg.components.AppHeader
import ghg.pages.{DirectPage, IndirectPage, InfoPage}
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._

object AppRouter {
  sealed trait Page
  case object Info extends Page
  case object Indirect extends Page
  case object Direct extends Page

  val menu = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (emptyRule
    | staticRoute(root, Info) ~> render(InfoPage())
    | staticRoute("indirect", Indirect) ~> render(IndirectPage())
    | staticRoute("direct", Indirect) ~> render(DirectPage())
    ).notFound(redirectToPage(Info)(Redirect.Replace))
    .renderWith(layout _)
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) = {
    <.div(
      AppHeader(),
      r.render(),
      <.div(^.textAlign := "center", ^.key := "footer")(
        <.hr(),
        <.p("Nguyễn Vân Anh")))
  }

  val router = Router(BaseUrl.fromWindowUrl{url =>
    println(s"fromWindowUrl: $url")
    url
  }, menu)
}
