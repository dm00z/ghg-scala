package ghg.components

import ghg.Utils._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.GhgData
import tex.TeX._

object Bien6 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b6 = d.bien6

      <.div(
        <.h3("6. Tính toán phát thải khí N2O từ hệ thống XLNT"),
        <.h4("6.1. Phát thải N2O gián tiếp từ nước thải dòng ra"),
        <.div("Công thức tính: `Phát thải N_2O = N_(dòng ra) * EF_(dòng ra) * 44/28`".teX),
        dataTbl(
          tr("Phát thải N2O gián tiếp từ nước thải dòng ra", b6.n2o_phatThaiGianTiep, "g/ngày"),
          tr("N", "dòng ra", b6.n_dongRa , "g/ngày"),
          tr("EF","dòng ra", b6.ef_dongRa, "g/ngày")
        ),
        <.h4("6.2. Phát thải N2O trực tiếp từ hệ thống xlnt"),
        <.div("Công thức tính: `N_2O_(htxlnt) = P * EF_(htxlnt) * CF`".teX),
        dataTbl(
          tr("Phát thải  N2O trực tiếp từ hệ thống XLNT", b6.n2o_phatThaiTrucTiep, "g/ngày"),
          tr("Số dân số P", b6.soDanSo_p, "người"),
          tr("EF","htxlnt", b6.ef_htxlnt, "g/người.ngày"),
          tr("CF", b6.cf, "g/ngày")
        ),
        <.h4("6.3. Tổng phát thải N2O từ hệ thống XLNT"),
        <.div(f"Tổng phát thải N2O từ hệ thống XLNT: `${b6.n2o_tongPhatThai}%,.2f` g/ngày"),
        <.div(f"Phát thải CO2 tương đương từ xử lý Nito = `${b6.co2_tuongDuongNito}%,.2f` g/ngày")
      )
    }
  }

  val component = ReactComponentB[Props]("Bien6")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
