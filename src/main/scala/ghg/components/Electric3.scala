package ghg.components

import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.ElectricData.D3
import model.GhgData
import scala.scalajs.js.JSNumberOps._

object Electric3 {
  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric._3)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      implicit val my: D3 = P.my()

      val rows = my.rows.zipWithIndex.map { case (r, i) =>
        implicit val rImplicit: D3.Row = r
        implicit val dispatch: D3.Row => Callback = d => {
          my.rows.splitAt(i) match {
            case (r1, _ :: r2) => P.my.dispatch(my.copy(rows = (r1 :+ d) ++ r2))
            case (r1, Nil) => P.my.dispatch(my.copy(rows = r1 :+ d))
          }
        }

        def addRow(e: ReactMouseEvent) = {
          val r = my.rows.splitAt(i) match {
            case (r1, r2) => (r1 :+ D3.Row()) ++ r2
          }
          P.dispatch(my.copy(rows = r))
        }

        def removeRow(e: ReactMouseEvent) = {
          val r = my.rows.toBuffer
          r.remove(i)
          P.dispatch(my.copy(rows = r.toList))
        }

        <.tr(<.td(i + 1),
          <.td(
            <.i(^.onClick ==> addRow,
              ^.cursor.pointer, ^.cls := "material-icons", "add_circle"),
            <.i(^.onClick ==> removeRow,
              ^.cursor.pointer, ^.cls := "material-icons", "remove_circle")
          ),
          tdStrInput(D3.Row.device),
          tdInput(D3.Row.kw),
          tdIntInput(D3.Row.quantity),
          tdInput(D3.Row.workHoursPerDay),
          <.td(r.kwhPerDay.toFixed(3))
        )
      }

      implicit val dispatch: D3 => Callback = P.my.dispatch

      @inline def addRow(e: ReactMouseEvent) = P.dispatch(my.copy(rows = my.rows :+ D3.Row()))

      <.div(
        table(
          <.th("TT"), <.th(), <.th("Thiết bị"), <.th("CS lắp đặt (kw)"), <.th("Số lượng"),
          <.th("Số giờ làm việc / ngày"), <.th("Công suất tiêu thụ điện năng (kwh/day)")
        )(rows ++ List(
          <.tr(<.td(rows.length + 1),
            <.td(
              <.i(^.onClick ==> addRow,
                ^.cursor.pointer, ^.cls := "material-icons", "add_circle"
              )),
            <.td("Điện chiếu sáng + khác"),
            <.td(^.colSpan := 2, "Ước tính chiếm 0.5-1.0% chi phí điện năng sản xuất"),
            tdInput(D3.ratioOther),
            //tdInput(D3.ratioOther, _.between(D3.RatioOther.range)),
            <.td(my.powerOther.toFixed(3))
          ),
          <.tr(<.td(),
            <.td(^.colSpan := 4, "Công suất tiêu thụ / ngày"), <.td(),
            <.td(my.powerTheory.toFixed(3))
          ),
          <.tr(<.td(),
            <.td(^.colSpan := 3, "Công suất tiêu thụ thực tế / ngày"),
            tdInput(D3.etieuThu, _.between(D3.EtieuThu.range)), <.td(),
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
