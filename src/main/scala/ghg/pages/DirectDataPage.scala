package ghg.pages

import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.DirectTable._

object DirectDataPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.d)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my

      def genericTbl(implicit d: GenericData) = {
        @inline implicit def dispatch: GenericData => Callback = my.dispatch

        table(
          <.tr(
            <.td(<.b("Thông số")),
            <.td(<.b("Đơn vị")),
            <.td(<.b("Giá trị")),
            <.td(<.b("Ghi chú"))
          ),
          <.tr(td("Q", "o,v"), <.td("m3/day"), <.td(P().info.power),
            <.td("Lưu lượng dòng vào hệ thống xử lý")
          ),
          <.tr(td("S", "o,v"), <.td("mg/l"),
            tdInput(GenericData.s),
            <.td("Nồng độ cơ chất dòng vào hệ thống")
          ),
          <.tr(td("TKN", "v"), <.td("mg/l"),
            tdInput(GenericData.tkn),
            <.td()
          ),
          <.tr(<.td("TSS"), <.td("mg/l"),
            tdInput(GenericData.tss),
            <.td()
          )
        )
      }

      val d = my()
      <.div(
        <.h3("1. Thông số dòng vào hệ thống xử lý"),
        genericTbl(d.generic)
      )

    }
  }

  val component = ReactComponentB[Props]("DirectData")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
