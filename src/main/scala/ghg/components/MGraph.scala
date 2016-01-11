package ghg.components

import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.prefix_<^._
import reactd3._
import scala.scalajs.js
import japgolly.scalajs.react._

object MGraph {
  val otherProps = js.Dynamic.literal(
    width = 280, height = 260,
    margins = js.Dynamic.literal(
      top = 20, bottom = 20, left = 38, right = 34
    ))

  def apply(legend: TagMod, data: js.Array[js.Object with js.Dynamic], chartSerie: ChartSerie*) = {
    val charts = chartSerie.map { serie =>
      LineChart(data,
        (d: js.Dynamic) => d.t.asInstanceOf[Double],
        js.Array(serie)
      )(otherProps): TagMod
    }
    <.div(legend +: charts :_*)
  }

  def one(f: Double => Double, x_range: Range, color: String = "red") = {
    val p = js.Dynamic.literal(
      width = 300, height = 260,
      margins = js.Dynamic.literal(
        top = 20, bottom = 20, left = 100, right = 40
      ))
    LineChart(
      x_range.map(t => js.Dynamic.literal("t" -> t, "d" -> f(t))).toJsArray,
      (d: js.Dynamic) => d.t.asInstanceOf[Double],
      js.Array(ChartSerie("d", color))
    )(p)
  }
}