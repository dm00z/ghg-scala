package ghg.pages

import chandu0101.scalajs.react.components.materialui.MuiDatePicker
import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import org.widok.moment.Moment
import scala.scalajs.js
import scala.scalajs.js.JSNumberOps._
import scala.scalajs.js.UndefOr
import model.GasData.PowerRow

/**
  * TODO A. Công suất tiêu thụ khí tự nhiên (hiện tại không impl - tương ứng với nhà máy không dùng khí tự nhiên)
  */
object GasPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.gas)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my()

      val rows = my.powers.zipWithIndex.map { case (r, i) =>
        implicit val rImplicit: PowerRow = r
        implicit val dispatch: PowerRow => Callback = d => {
          my.powers.splitAt(i) match {
            case (r1, _ :: r2) => P.my.dispatch(my.copy(powers = (r1 :+ d) ++ r2))
            case (r1, Nil) => P.my.dispatch(my.copy(powers = r1 :+ d))
          }
        }
        <.tr(
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
          tdInput(PowerRow.m3),
          <.td(r.average.toFixed(3))
        )
      }

      <.div(
        <.h2("A. Công suất tiêu thụ khí tự nhiên"),
        <.div("Nhập dữ liệu thực tế"),
        table(<.th("Từ ngày"), <.th("Đến ngày"), <.th("Số ngày"), <.th("Tiêu thụ (m3)"), <.th("Trung bình (m3/day)"))(
          rows :+ <.tr(
            <.td(^.colSpan := 4, <.b("Tổng cộng")),
            <.td(my.power.toFixed(3))
          ): _*
        ),
        <.button(^.onClick ==> { e: ReactMouseEvent =>
          val today = Moment().startOf("d")
          P.dispatch(my.copy(powers = my.powers :+ PowerRow(today.subtract(1, "d"), today, 0)))
        }, "Thêm")

      )
    }
  }

  val component = ReactComponentB[Props]("Gas")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
