package reactd3

import chandu0101.macros.tojs.JSMacro
import japgolly.scalajs.react.{ReactComponentU_, React}
import scala.scalajs.js
import scala.scalajs.js.UndefOr

//FIXME y
case class LineChart[T <: js.Object](data: js.Array[T],
                                  x: js.Function1[T, Double],
                                  chartSeries: js.Array[ChartSerie],
                                  xDomain: UndefOr[js.Array[Double]] = js.undefined,
                                  xLabel: UndefOr[String] = js.undefined,
                                  yDomain: UndefOr[js.Array[Double]] = js.undefined,
                                  yLabel: UndefOr[String] = js.undefined,
                                  yLabelPosition: UndefOr[String] = js.undefined) {
  /** @param otherProps ex:
    * {{{
    *   {
    *     width: 960,
    *     height: 500,
    *     margins: { top: 80, right: 100, bottom: 80, left: 100 }
    *   }
    * }}} */
  def apply(otherProps: js.Object) = {
    val props = es6.Object.assign(js.Dynamic.literal(), otherProps, JSMacro[LineChart[_]](this))
    val f = React.asInstanceOf[js.Dynamic].createFactory(ReactD3Basic.LineChart)
    f(props).asInstanceOf[ReactComponentU_]
  }
}
