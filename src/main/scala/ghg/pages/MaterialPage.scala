package ghg.pages

import chandu0101.scalajs.react.components.materialui._
import diode.react.ModelProxy
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{MaterialData, GhgData}
import scala.language.existentials

object MaterialPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.material)
  }

  case class Backend($: BackendScope[Props, _]) {
    private lazy val colNames = MuiTableRow()(
      MuiTableHeaderColumn(key = "meta")(),
      MuiTableHeaderColumn(key = "Methanol")("Methanol"),
      MuiTableHeaderColumn(key = "Kiềm")("Kiềm"),
      MuiTableHeaderColumn(key = "x2")("Ferric chloride (FeCl3 .6H2O)"),
      MuiTableHeaderColumn(key = "x3")("Javen"),
      MuiTableHeaderColumn(key = "x4")("Polimer"),
      MuiTableHeaderColumn(key = "ref")() //TL tham khảo
    )

    def render(P: Props) = {
      import MaterialData.Ratio
      val my = P.my()
      <.div(
        MuiTable()(
          MuiTableHeader(displaySelectAll = false, adjustForCheckbox = false)(
            MuiTableRow()(MuiTableHeaderColumn()()),
            colNames),
          MuiTableBody(displayRowCheckbox = false)(
            MuiTableRow(key = "1")(
              Ratio.all.map(MuiTableRowColumn()(_)): _*
            ),
            MuiTableRow(key = "2")(
              my.all.map(MuiTableRowColumn()(_)): _*
            )
          ),
          MuiTableFooter(adjustForCheckbox = false)(
            colNames,
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
