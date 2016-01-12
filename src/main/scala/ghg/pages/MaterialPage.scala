package ghg.pages

import chandu0101.scalajs.react.components.materialui._
import diode.react.ModelProxy
import ghg.components.AppHeader
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{MaterialData, GhgData}
import ghg.Utils._

object MaterialPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.material)
  }

  case class Backend($: BackendScope[Props, _]) {
    private val colNames2 = MuiTableRow()(
      MuiTableHeaderColumn()(),
      MuiTableHeaderColumn(tooltip = "Hệ số phát thải của nguyên liệu (gCO2/gNguyenLieu). [Ref: Bani Shahabadi et al., 2009; Maas, 2009]")(
        <.span("EF", <.sup("i"), <.sub("NguyenLieu"))),
      MuiTableHeaderColumn(tooltip = "Khối lượng nguyên liệu (kg/ngày)")(
        <.span("Q", <.sup("i"), <.sub("ng"))),
      MuiTableHeaderColumn()(
        <.span("EF", <.sup("i"), <.sub("NguyenLieu"), " * Q", <.sup("i"), <.sub("ng")))
    )

    def render(P: Props) = {
      import MaterialData.Ratio

      implicit val my: MaterialData = P.my()
      implicit val dispatch: MaterialData => Callback = P.dispatch

      <.div(
        <.h2("Khí nhà kính phát thải từ tiêu thụ nguyên liệu"),

        MuiTable(style = AppHeader.txtStyle)(
          MuiTableHeader(displaySelectAll = false, adjustForCheckbox = false)(
            MuiTableRow()(MuiTableHeaderColumn()()),
            colNames2),
          MuiTableBody(displayRowCheckbox = false)(
            Ratio.names.zip(Ratio.all).zip(List(
              MaterialData.methanol, MaterialData.alkali, MaterialData.fe, MaterialData.ja, MaterialData.po
            )).map {
              case ((n, r), v) => MuiTableRow()(
                MuiTableRowColumn()(n),
                MuiTableRowColumn()(r),
                MuiTableRowColumn()(input(v)),
                MuiTableRowColumn()(r * v.get(my))
              )
            } :+ MuiTableRow()(
              MuiTableRowColumn()("Tổng (kg/day)"),
              MuiTableRowColumn()(),
              MuiTableRowColumn()(),
              MuiTableRowColumn()(my.ghg)
            )  :_*
          ),
          MuiTableFooter(adjustForCheckbox = false)(
            colNames2,
            MuiTableRow()()
          )
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Material")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
