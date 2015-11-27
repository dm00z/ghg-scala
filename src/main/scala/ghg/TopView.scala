package ghg

import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane

class TopView(d: TopData) extends GridPane {
  val rowNames = Seq("Loại nước thải", "Tên nhà máy", "Địa điểm", "Hạng mục")

  addColumn(0, rowNames.map(Label(_).delegate): _*)
  addColumn(1, Seq(d.tpe, d.name, d.addr, d.group).map(_.createText()): _*)

  val subGroupRow = Seq(Label("Tiểu mục").delegate, d.subGroup.createText())

  (d.subGroup !=~ null).onChange{ (_, _, notNull) =>
    if (notNull) addRow(rowNames.length, subGroupRow: _*)
    else children.removeAll(subGroupRow: _*)
  }
}
