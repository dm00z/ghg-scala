package ghg.components

import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.prefix_<^._
import reactd3._
import scala.scalajs.js
import japgolly.scalajs.react._

object MGraph {
  def apply(legend: TagMod,
            data: js.Array[js.Object with js.Dynamic],
            large: Boolean,
            chartSerie: ChartSerie*) = {

    val otherProps = js.Dynamic.literal(
      width = 840,
      height = 620,
      margins = js.Dynamic.literal(
        top = 20, bottom = 20, right = 34,
        left = if (large) 100 else 38
      )
    )

    val charts = chartSerie.map { serie =>
      LineChart(data,
        (d: js.Dynamic) => d.t.asInstanceOf[Double],
        js.Array(serie)
      )(otherProps): TagMod
    }
    <.div(legend +: charts :_*)
  }
}