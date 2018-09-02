package ghg.components

import ghg.Utils._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.GhgData
import tex.TeX._
import scala.scalajs.js.JSNumberOps._

object Bien5 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b5 = d.bien5
      <.div(
        <.h3(f"5. Tính toán phát thải khí N2O từ hệ thống XLNT"),
        <.h4("5.1 Phát thải N2O gián tiếp từ nước thải dòng ra"),
        <.div("Công thức tính: Phát thải `N_2O = N_(dòng ra) * EF_(dòng ra) * 44/28`".teX),
        dataTbl(
          tr("Phát thải N2O gián tiếp","", b5.phatThaiGianTiep_n2o, "g/ngày"),
          tr("N","dòng ra", b5.N_dongra, "g/ngày"),
          tr("EF","dòng ra", b5.EF_dongra, "")
        ),
        <.h4("5.2 Phát thải N2O trực tiếp từ hệ thống XLNT"),
        <.div("Công thức tính: `N_2O_(htxlnt) = P*T_(htxlnt) * F_(IND-COM) * EF_(htxlnt)`".teX),
        dataTbl(
          tr("Phát thải N2O trực tiếp","", b5.phatThaiTrucTiep_n2o, "g/ngày"),
          tr("Số dân số P","", b5.pop, "người"),
          tr("T","htxlnt", b5.T_hlxlnt * 100, "%"),
          tr("EF","htxlnt", b5.EF_hlxlnt, "g/người.ngày"),
          tr("CF","", b5.F_IND_COM, "")
        ),

        <.h4("5.3 Tổng phát thải N2O từ hệ thống XLNT"),
        <.div("Tổng phát thải N2O từ hệ thống XLNT: " + b5.tongPhatThai_n2o.toFixed(3) + " g/ngày"),
        <.div("Phát thải CO2-td, N2O:  " + b5.phatThai_co2td_n2o.toFixed(3) + " g/ngày")
      )
    }
  }

  val component = ReactComponentB[Props]("Bien5")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
