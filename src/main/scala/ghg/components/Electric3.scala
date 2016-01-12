package ghg.components

import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import scala.scalajs.js.JSNumberOps._

object Electric3 {
  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric._3)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my()

      val rows = my.rows.zipWithIndex.map { case (r, i) =>
          <.tr(<.td(i + 1),
            <.td(r.device),
            <.td(r.kw),
            <.td(r.quantity),
            <.td(r.workHoursPerDay),
            <.td(r.kwhPerDay.toFixed(3))
          )
      }
      <.div(
        table(
          <.th("TT"), <.th("Thiết bị"), <.th("CS lắp đặt (kw)"), <.th("Số lượng"),
          <.th("Số giờ làm việc / ngày"), <.th("Công suất tiêu thụ điện năng (kwh/day)")
        )(rows ++ List(
          <.tr(<.td(rows.length + 1),
            <.td("Điện chiếu sáng + khác"),
            <.td(^.colSpan := 2, "Ước tính chiếm 0.5-1.0% chi phí điện năng sản xuất"),
            <.td(my.ratioOther),
            <.td(my.powerOther.toFixed(3))
          ),
          <.tr(<.td(),
            <.td(^.colSpan := 4, "Công suất tiêu thụ / ngày"),
            <.td(my.powerTheory.toFixed(3))
          ),
          <.tr(<.td(),
            <.td(^.colSpan := 3, "Công suất tiêu thụ thực tế / ngày"),
            <.td(my.etieuThu),
            <.td(my.power.toFixed(3))
          )
        ): _*)
      )
    }
  }

  val component = ReactComponentB[Props]("Electric3")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
