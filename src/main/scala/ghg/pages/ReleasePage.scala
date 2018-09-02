package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{TechMethod, KineticCoefficientData, GhgData}
import reactd3.ChartSerie
import scala.scalajs.js
import tex.TeX._
import ghg.Utils._
import KineticCoefficientData._
import scala.scalajs.js.JSNumberOps._

object ReleasePage {
  type Props = ModelProxy[GhgData]

  case class Backend($: BackendScope[GhgData, _]) {
    def render(data: GhgData) = {
      val release = data.releaseResult

      def dataTbl() = {
        table(
          <.tr(<.th(^.rowSpan := 2, "Nguồn phát thải"), <.th(^.colSpan := 2, "Loại phát thải"), <.th(^.rowSpan := 2, "Tỷ lệ % theo nguồn")),
          <.tr(<.th("Từ sản xuất điện năng phục vụ HTXLNT (g/ngày)"), <.th("Từ quá trình xử lý nước thải (g/ngày)")),
          <.tr(<.td("Tiêu thụ điện năng"), <.td(release.elecPower.toFixed(2)), <.td(), <.td()),
          <.tr(<.td("Quá trình xử lý sinh học"), <.td(), <.td(release.M_co2_quaTrinh.toFixed(2)), <.td((release.tyle_co2_quaTrinh*100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ bùn yếm khí, thu hồi và đốt CH4"), <.td(), <.td(release.M_co2tdPhanHuyPhongKhong.toFixed(2)), <.td((release.tyle_co2tdPhanHuyPhongKhong*100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ BOD dòng ra"), <.td(""), <.td(release.M_co2BODra.toFixed(2)), <.td((release.tyle_co2BODra*100).toFixed(2) + "%")),
          <.tr(<.td("Phát thải CO2-tđ từ khí N2O"), <.td(""), <.td(release.phatThai_co2td_n2o.toFixed(2)), <.td((release.tyle_phatThai_co2td_n2o*100).toFixed(2) + "%")),
          <.tr(<.td("Tổng phát thải KNK theo loại"), <.td(release.sumKNKByElecPower.toFixed(2)), <.td(release.sumKNKByWasteDisposal.toFixed(2)), <.td()),
          <.tr(<.td("Tổng lượng KNK"), <.td(^.colSpan := 2, release.sumKNKAll.toFixed(2)), <.td()),
          <.tr(<.td("Tỷ lệ % phát thải theo loại"), <.td((release.tyle_elecPower*100).toFixed(2) + "%"), <.td((release.tyle_wasteDisposal*100).toFixed(2) + "%"), <.td("100%"))
        )
      }

      <.div(
        <.h2("4.2.Trường hợp phóng không khí CH4"),
        dataTbl()
      )
    }
  }

  val component = ReactComponentB[GhgData]("Release")
    .renderBackend[Backend]
    .build

  def apply(model: ModelProxy[GhgData]) = {
    val d = model()
    component(d)
  }
}
