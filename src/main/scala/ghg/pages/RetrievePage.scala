package ghg.pages

import reactd3.ChartSerie

import scala.scalajs.js
import ghg.Utils._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import scalacss.Defaults._
import chandu0101.scalajs.react.components.materialui._
import pathjs.PieChart._

import scala.scalajs.js.Dynamic.{literal => jsObj}

object RetrievePage {
  val percentages = List(
    Percentage("M1", 20),
    Percentage("M1", 20),
    Percentage("M1", 20),
    Percentage("M1", 20),
    Percentage("M1", 20)
  )

  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.d)
  }

  case class Backend($: BackendScope[GhgData, _]) {
    def pieGraph(r: Range, large: Boolean, q: GhgData) = {
      val otherProps = js.Dynamic.literal(
        width = if (large) 420 else 280,
        height = 260,
        margins = js.Dynamic.literal(
          top = 20, bottom = 20, right = 34,
          left = if (large) 100 else 38
        )
      )

      //      val data = {
      //        val rei = q.retrieveResult
      //
      //        js.Array[js.Object](
      //          js.Dynamic.literal("value" -> rei.M_co2_quaTrinh, "name" -> "Quá trình"),
      //          js.Dynamic.literal("value" -> rei.M_co2tdPhanHuyDot, "name" -> "Đốt")
      //        )
      //      }

      val data = r.map { temp =>
        val rel = q.releaseResult
        val rei = q.retrieveResult

        js.Dynamic.literal(
          "value" -> rei.M_co2_quaTrinh,
          "name" -> "Quá trình xử lý sinh học"
        )
      }.toJsArray

      val bio = ChartSerie("co2_qua_trinh", "green")
      //val combust = ChartSerie("phan_huy_dot", "blue", "Phân hủy bùn yếm khí, thu hồi và đốt CH4")

//      val chartSerie = js.Array(bio)
//      val valueFunc: js.Function1[js.Dynamic, Double] = { (d: js.Dynamic) => +d.value.asInstanceOf[Double] }
//      val nameFunc: js.Function1[js.Dynamic, String] = { (d: js.Dynamic) => d.name.asInstanceOf[String] }

      //val chart = PieChart(data, chartSerie, valueFunc, nameFunc)(otherProps): TagMod

      //<.div(chart)
    }

    lazy val tabLabel = jsObj(backgroundColor = "#c0c3c3", fontWeight = 700, textTransform = "none", color = "black")
    lazy val tabLabelAlt = jsObj(backgroundColor = "#9b9da0", fontWeight = 700, textTransform = "none", color = "black")

    import scala.scalajs.js.JSNumberOps._

    def render(data: GhgData) = {

      val retrieve = data.retrieveResult

      def dataTbl() = {
        table4(
          <.tr(<.th(^.color := "white", ^.backgroundColor := "#145dbf", ^.rowSpan := 2, "Nguồn phát thải"),
            <.th(^.color := "white", ^.colSpan := 2, ^.backgroundColor := "#145dbf", "Loại phát thải"),
            <.th(^.color := "white", ^.rowSpan := 2, ^.backgroundColor := "#145dbf", "Tỷ lệ % theo nguồn")),
          <.tr(<.th(^.color := "white", ^.backgroundColor := "#145dbf", "Từ sản xuất điện năng phục vụ HTXLNT (kg/ngày)"),
            <.th(^.color := "white", ^.backgroundColor := "#145dbf", "Từ quá trình xử lý nước thải (kg/ngày)")),
          <.tr(<.td("Tiêu thụ điện năng"), <.td(retrieve.elecPower.toFixed(2)), <.td(), <.td()),
          <.tr(<.td("Bể xử lý sinh học"), <.td(), <.td((retrieve.M_co2_quaTrinh).toFixed(2)), <.td((retrieve.tyle_co2_quaTrinh * 100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ bùn yếm khí"), <.td(), <.td((retrieve.M_co2tdPhanHuyDot).toFixed(2)), <.td((retrieve.tyle_co2tdPhanHuyDot * 100).toFixed(2) + "%")),
          <.tr(<.td("Phân huỷ BOD dòng ra"), <.td(""), <.td((retrieve.M_co2BODra).toFixed(2)), <.td((retrieve.tyle_co2BODra * 100).toFixed(2) + "%")),
          <.tr(<.td("Phát thải khí N2O"), <.td(""), <.td((retrieve.phatThai_co2td_n2o).toFixed(2)), <.td((retrieve.tyle_phatThai_co2td_n2o * 100).toFixed(2) + "%")),
          <.tr(<.td("Tổng phát thải KNK theo loại"), <.td(retrieve.sumKNKByElecPower.toFixed(2)), <.td((retrieve.sumKNKByWasteDisposal).toFixed(2)), <.td()),
          <.tr(<.td("Tổng lượng KNK"), <.td(^.colSpan := 2, (retrieve.sumKNKAll).toFixed(2)), <.td()),
          <.tr(<.td("Tỷ lệ % phát thải theo loại"), <.td((retrieve.tyle_elecPower * 100).toFixed(2) + "%"), <.td((retrieve.tyle_wasteDisposal * 100).toFixed(2) + "%"), <.td("100%"))
        )
      }

      //#9b9da0


      <.div(
        <.h2("4.1.Trường hợp thu hồi và đốt khí CH4"),
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
                PieChartBuilder(List(("Bể xử lý sinh học", retrieve.tyle_co2_quaTrinh * 100),
                  ("Phân huỷ bùn yếm khí", retrieve.tyle_co2tdPhanHuyDot * 100),
                  ("Phân huỷ BOD dòng ra", retrieve.tyle_co2BODra * 100),
                  ("Phát thải khí N2O", retrieve.tyle_phatThai_co2td_n2o * 100)
                ),
                  600, 240)),
              MuiTab(label = "Biểu đồ 2", style = tabLabelAlt)(<.label("Biểu đồ 2"),
                BarChartBuilder(List(("Tiêu thụ điện năng", retrieve.sumKNKByElecPower),
                  ("Quá trình xử lý", retrieve.sumKNKByWasteDisposal),
                  ("Tổng phát thải KNK", retrieve.sumKNKAll)
                ),
                  "Thu hồi và đốt khí CH4 ","kg CO2-tđ/ngày", "Nguồn phát thải",600, 240)),
              MuiTab(label = "Biểu đồ 3", style = tabLabel)(<.label("Biểu đồ 3"),
                BarChartBuilder(List(("Bể xử lí sinh học", retrieve.M_co2_quaTrinh),
                  ("Phân huỷ bùn yếm khí", retrieve.M_co2tdPhanHuyDot),
                  ("Phân huỷ BOD dòng ra", retrieve.M_co2BODra),
                  ("Phát thải khí N2O", retrieve.phatThai_co2td_n2o),
                  ("Phát thải từ quá trình xử lý", retrieve.sumKNKAll)
                ),
                  "Biểu đồ phát thải KNK hệ thống GOLDMARK ", "kg CO2-tđ/ngày", "Nguồn phát thải",640, 240))
            )
          )
        )
      )
    }
  }

  val component = ReactComponentB[GhgData]("RetrievePage")
    .renderBackend[Backend]
    .build

  def apply(model: ModelProxy[GhgData]) = {
    val d = model()
    component(d)
  }
}
