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
import chandu0101.scalajs.react.components.materialui._
import scala.scalajs.js.Dynamic.{literal => jsObj}

object ReleasePage {
  type Props = ModelProxy[GhgData]

  case class Backend($: BackendScope[GhgData, _]) {

    lazy val tabLabel = jsObj(backgroundColor = "#c0c3c3", fontWeight = 700, textTransform = "none", color = "black")
    lazy val tabLabelAlt = jsObj(backgroundColor = "#9b9da0", fontWeight = 700, textTransform = "none", color = "black")

    import scala.scalajs.js.JSNumberOps._

    def render(data: GhgData) = {
      val release = data.releaseResult

      def dataTbl() = {
        table4(
          <.tr(<.th(^.color := "white", ^.backgroundColor := "#145dbf",^.rowSpan := 2, "Nguồn phát thải"),
            <.th(^.color := "white", ^.colSpan := 2, ^.backgroundColor := "#145dbf", "Loại phát thải"),
            <.th(^.color := "white", ^.rowSpan := 2, ^.backgroundColor := "#145dbf", "Tỷ lệ % theo nguồn")),
          <.tr(<.th(^.color := "white", ^.backgroundColor := "#145dbf","Từ sản xuất điện năng phục vụ HTXLNT (kg/ngày)"),
            <.th(^.color := "white", ^.backgroundColor := "#145dbf","Từ quá trình xử lý nước thải (kg/ngày)")),
          <.tr(<.td("Tiêu thụ điện năng"), <.td(release.elecPower.toFixed(2)), <.td(), <.td()),
          <.tr(<.td("Bể xử lý sinh học"), <.td(), <.td((release.M_co2_quaTrinh).toFixed(2)), <.td((release.tyle_co2_quaTrinh*100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ bùn yếm khí"), <.td(), <.td((release.M_co2tdPhanHuyPhongKhong).toFixed(2)), <.td((release.tyle_co2tdPhanHuyPhongKhong*100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ BOD dòng ra"), <.td(""), <.td((release.M_co2BODra).toFixed(2)), <.td((release.tyle_co2BODra*100).toFixed(2) + "%")),
          <.tr(<.td("Phát thải từ khí N2O"), <.td(""), <.td((release.phatThai_co2td_n2o).toFixed(2)), <.td((release.tyle_phatThai_co2td_n2o*100).toFixed(2) + "%")),
          <.tr(<.td("Tổng phát thải KNK theo loại"), <.td(release.sumKNKByElecPower.toFixed(2)), <.td((release.sumKNKByWasteDisposal).toFixed(2)), <.td()),
          <.tr(<.td("Tổng lượng KNK"), <.td(^.colSpan := 2, (release.sumKNKAll).toFixed(2)), <.td()),
          <.tr(<.td("Tỷ lệ % phát thải theo loại"), <.td((release.tyle_elecPower*100).toFixed(2) + "%"), <.td((release.tyle_wasteDisposal*100).toFixed(2) + "%"), <.td("100%"))
        )
      }

      <.div(
        <.h2("4.2.Trường hợp phóng không khí CH4"),
        dataTbl(),
        <.div(
          <.div(^.`class` := "input-field note-area",
            <.label("Ghi chú"),
            MuiTextField(multiLine = true, rowsMax = 7)()
          ),
          <.div(^.`class` := "graph-area",
            <.label("Biểu đồ"),
            MuiTabs()(
              MuiTab(label = "Biểu đồ 1", style = tabLabel)(<.label("Biểu đồ 1"),
                PieChartBuilder(List(("Bể xử lý sinh học", release.tyle_co2_quaTrinh * 100),
                  ("Phân huỷ bùn yếm khí", release.tyle_co2tdPhanHuyPhongKhong * 100),
                  ("Phân huỷ BOD dòng ra", release.tyle_co2BODra * 100),
                  ("Phát thải khí N2O", release.tyle_phatThai_co2td_n2o * 100)
                ),
                  600, 240)),
              MuiTab(label = "Biểu đồ 2", style = tabLabelAlt)(<.label("Biểu đồ 2"),
                BarChartBuilder(List(("Tiêu thụ điện năng", release.sumKNKByElecPower),
                  ("Quá trình xử lý", release.sumKNKByWasteDisposal),
                  ("Tổng phát thải KNK", release.sumKNKAll)
                ),
                  "Phóng không khí CH4 ","kg CO2-tđ/ngày", "Nguồn phát thải",600, 240)),
              MuiTab(label = "Biểu đồ 3", style = tabLabel)(<.label("Biểu đồ 3"),
                BarChartBuilder(List(("Bể xử lí sinh học", release.M_co2_quaTrinh),
                  ("Phân huỷ bùn yếm khí", release.M_co2tdPhanHuyPhongKhong),
                  ("Phân huỷ BOD dòng ra", release.M_co2BODra),
                  ("Phát thải khí N2O", release.phatThai_co2td_n2o),
                  ("Phát thải từ quá trình xử lý", release.sumKNKAll)
                ),
                  "Biểu đồ phát thải KNK hệ thống GOLDMARK ", "kg CO2-tđ/ngày", "Nguồn phát thải",640, 240))
            )
          )
        )
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
