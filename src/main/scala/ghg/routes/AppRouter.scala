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
      <.div(^.textAlign := "center",
        ^.key := "footer",
        ^.marginLeft := "300px"
//        ^.position := "absolute",
//        ^.bottom := 0,
//        ^.left := 0,
//        ^.right := 0
      )
      (
        //<.hr(),
        <.p(^.fontFamily := "-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif",
          ^.fontWeight := "bold", ^.color := "#145dbf",
          "Nghiên cứu sinh: Nguyễn Thị Vân Anh; Người hướng dẫn: PGS.TS. Đặng Xuân Hiển")
      )
    )
  }

  val router = Router(BaseUrl.until_#, config)
}
