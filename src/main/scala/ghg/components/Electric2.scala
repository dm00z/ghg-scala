package ghg.components

import chandu0101.scalajs.react.components.materialui.MuiDatePicker
import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.ElectricData.D2
import model.GhgData
import org.widok.moment.Moment
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.JSNumberOps._

object Electric2 {
  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric._2)
  }

  case class Backend($: BackendScope[Props, _]) {
    private def newRow() = {
      val today = Moment().startOf("d")
      D2.Row(today.subtract(1, "d"), today, 0)
    }

    def render(P: Props) = {
      val my = P.my()
      val rows = my.rows.zipWithIndex.map { case (r, i) =>
        implicit val rImplicit: D2.Row = r
        implicit val dispatch: D2.Row => Callback = d => {
          my.rows.splitAt(i) match {
            case (r1, _ :: r2) => P.my.dispatch(my.copy(rows = (r1 :+ d) ++ r2))
            case (r1, Nil) => P.my.dispatch(my.copy(rows = r1 :+ d))
          }
        }

        def addRow(e: ReactMouseEvent) = {
          val r = my.rows.splitAt(i) match {
            case (r1, r2) => (r1 :+ newRow()) ++ r2
          }
          P.dispatch(my.copy(rows = r))
        }

        def removeRow(e: ReactMouseEvent) = {
          val r = my.rows.toBuffer
          r.remove(i)
          P.dispatch(my.copy(rows = r.toList))
        }

        <.tr(
          <.td(
            <.i(^.onClick ==> addRow,
              ^.cursor.pointer, ^.cls := "material-icons", "add_circle"),
            <.i(^.onClick ==> removeRow,
              ^.cursor.pointer, ^.cls := "material-icons", "remove_circle")
          ),
          <.td(MuiDatePicker(
            onDismiss = Callback.empty, onShow = Callback.empty,
            autoOk = true,
            defaultDate = r.from.toDate(),
            formatDate = dateFormater,
            onChange = { (_: UndefOr[Nothing], d: js.Date) => dispatch(r.copy(from = Moment(d))) }
          )()),
          <.td(MuiDatePicker(
            autoOk = true,
            defaultDate = r.to.toDate(),
            formatDate = dateFormater,
            onChange = { (_: UndefOr[Nothing], d: js.Date) => dispatch(r.copy(to = Moment(d))) }
          )()),
          <.td(r.days),
          tdInput(D2.Row.kwh),
          <.td(r.average.toFixed(3))
        )
      }

      @inline def addRow(e: ReactMouseEvent) = P.dispatch(my.copy(rows = my.rows :+ newRow()))

      <.div(
        table(<.th(), <.th("Từ ngày"), <.th("Đến ngày"), <.th("Số ngày"), <.th("Tiêu thụ (kwh)"), <.th("Trung bình (kwh/day)"))(
          rows :+ <.tr(
            <.td(
              <.i(^.onClick ==> addRow,
                ^.cursor.pointer, ^.cls := "material-icons", "add_circle"
              )),
            <.td(^.colSpan := 4, <.b("Tổng cộng")),
            <.td(my.power.toFixed(3))
          ): _*
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Electric2")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
