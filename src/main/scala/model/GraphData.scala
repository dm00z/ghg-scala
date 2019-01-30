package model

case class Col(label: String, types: String)
case class C(v: List[Any])
class Row(c: List[C]) {
  var xc = c

  def add(xxc: C) = {
    xc = xxc :: c
  }
}
case class DataTable(cols: List[Col], rows: List[Row])
case class Options(width: Int, height: Int)
case class State()

case class GraphData(containerId: String, dataTable: DataTable, options: Options, state: State, isDefaultVisualization: Boolean, chartType: String)
