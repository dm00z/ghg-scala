package reactd3

import chandu0101.macros.tojs.JSMacro
import japgolly.scalajs.react.{ReactComponentU_, React}
import scala.scalajs.js
import scala.scalajs.js.UndefOr

//FIXME y
case class PieChart[T <: js.Object](data: js.Array[T],
                                    //x: js.Function1[T, Double],
                                    chartSeries: js.Array[ChartSerie],
                                    value: js.Function1[T, Double],
                                    name: js.Function1[T, String]) {
  /** @param otherProps ex:
    * {{{
    *   {
    *     width: 960,
    *     height: 500,
    *     margins: { top: 80, right: 100, bottom: 80, left: 100 }
    *   }
    * }}} */
  def apply(otherProps: js.Object) = {
    val props = es6.Object.assign(js.Dynamic.literal(), otherProps, JSMacro[PieChart[_]](this))
    val f = React.asInstanceOf[js.Dynamic].createFactory(ReactD3Basic.PieChart)
    f(props).asInstanceOf[ReactComponentU_]
  }
}
