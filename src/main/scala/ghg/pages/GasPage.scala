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
import model.GasData.{GWP, GasRatio, PowerRow}

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

      val powerRows = my.powers.zipWithIndex.map { case (r, i) =>
        implicit val rImplicit: PowerRow = r
        implicit val dispatch: PowerRow => Callback = d => {
          my.powers.splitAt(i) match {
            case (r1, _ :: r2) => P.dispatch(my.copy(powers = (r1 :+ d) ++ r2))
            case (r1, Nil) => P.dispatch(my.copy(powers = r1 :+ d))
          }
        }

        def addRow(e: ReactMouseEvent) = {
          val powers = my.powers.splitAt(i) match {
            case (r1, r2) => (r1 :+ PowerRow()) ++ r2
          }
          P.dispatch(my.copy(powers = powers))
        }

        def removeRow(e: ReactMouseEvent) = {
          val powers = my.powers.toBuffer
          powers.remove(i)
          P.dispatch(my.copy(powers = powers.toSeq))
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
          tdInput(PowerRow.m3),
          <.td(r.average.toFixed(3))
        )
      }

      implicit val gasRatio: GasRatio = my.gas
      implicit val gwp: GWP = my.gwp
      implicit val gasDispatch: GasRatio => Callback = P.dispatch
      implicit val gwpDispatch: GWP => Callback = P.dispatch

      @inline def addRow(e: ReactMouseEvent) = P.dispatch(my.copy(powers = my.powers :+ PowerRow()))

      <.div(
        <.h2("A. Công suất tiêu thụ khí tự nhiên"),
        <.div("Nhập dữ liệu thực tế"),
        table(<.th(), <.th("Từ ngày"), <.th("Đến ngày"), <.th("Số ngày"), <.th("Tiêu thụ (m3)"), <.th("Trung bình (m3/day)"))(
          powerRows :+ <.tr(
            <.td(
              <.i(^.onClick ==> addRow,
                ^.cursor.pointer, ^.cls := "material-icons", "add_circle"
              )),
            <.td(^.colSpan := 4, <.b("Tổng cộng")),
            <.td(my.power.toFixed(3))
          ): _*
        ),

        <.h2("B. Bảng hệ số khí"),
        table(
          <.th(),
          <.th("Hệ số phát thải (g CO2 / m3 khí)"),
          <.th("Khả năng gây ấm toàn cầu GWP")
        )(
          <.tr(
            td("CO", "2"),
            tdInput(GasRatio.co2),
            tdInput(GWP.co2)
          ),
          <.tr(
            td("CH", "4"),
            tdInput(GasRatio.ch4),
            tdInput(GWP.ch4)
          ),
          <.tr(
            <.td("N", <.sub("2"), "O"),
            tdInput(GasRatio.n2o),
            tdInput(GWP.n2o)
          ),
          <.tr(
            <.td("Tài liệu tham khảo"),
            <.td(gasRatio.ref),
            <.td(gwp.ref)
          )
        ),

        <.h2(s"C. Phát thải KNK từ tiêu thụ khí tự nhiên = ${my.ghg.toFixed(3)} (kg", <.sub("CO2-td"), "/day)")
      )
    }
  }

  val component = ReactComponentB[Props]("Gas")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
