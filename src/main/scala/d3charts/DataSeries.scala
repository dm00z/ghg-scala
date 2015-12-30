package d3charts

import japgolly.scalajs.react._
import org.singlespaced.d3js.d3
import org.singlespaced.d3js.scale.Linear
import scala.scalajs.js

object DataSeries {
  case class D(x: Double, y: Double)

  case class Props(data: js.Array[D],
                   xScale: Linear[Double, Double],
                   yScale: Linear[Double, Double],
                   color: String, interpolate: String)

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val path = d3.svg.line[D]()
          .x((d: D, i: Int) => P.xScale(d.x))
          .y((d: D, i: Int) => P.yScale(d.y))
          .interpolate(P.interpolate)
      LineChart(path(P.data), P.color)
    }
  }
  val component = ReactComponentB[Props]("DataSeries")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(data: js.Array[D],
            xScale: Linear[Double, Double],
            yScale: Linear[Double, Double],
            color: String = "blue", interpolate: String = "linear") =
    component(Props(data, xScale, yScale, color, interpolate))
}
