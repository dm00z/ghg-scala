package d3charts

import japgolly.scalajs.react._
import org.singlespaced.d3js.d3
import org.singlespaced.d3js.scale.Linear
import scala.scalajs.js
import DataSeries.D

object LinesChart {
  case class LineD(data: js.Array[D], color: String = "blue", interpolate: String = "linear")

  case class Props(width: Int, height: Int, lines: List[LineD])

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      var (xmin, xmax, ymin, ymax) = (Double.MaxValue, Double.MinValue, Double.MaxValue, Double.MinValue)
      for {
        l <- P.lines
        d <- l.data
      } {
        xmin = xmin min d.x
        xmax = xmax max d.x
        ymin = ymin min d.y
        ymax = ymax max d.y
      }
      println(s"min max: $xmin, $xmax, $ymin, $ymax")

      val xScale = d3.scale.linear()
        .domain(js.Array(xmin, xmax))
        .range(js.Array(0, P.width))

      val yScale = d3.scale.linear()
        .domain(js.Array(ymin, ymax))
        .range(js.Array(P.height, 0))

      val xAxis = Axis.component(Axis.Props('x', 0, P.height + ymax,
        d3.scale.identity()
          .domain(js.Array(xmin, xmax))
          .range(js.Array(0, P.width))
          .asInstanceOf[Linear[Double, Double]],
        xScale))

      val yAxis = Axis.component(Axis.Props('y', P.width, 0,
        d3.scale.identity()
          .domain(js.Array(ymin, ymax))
          .range(js.Array(P.height, 0))
          .asInstanceOf[Linear[Double, Double]],
        yScale))

      val dataSeries = P.lines.map { p =>
        DataSeries(p.data, xScale, yScale, p.color, p.interpolate)
      }

      //+40 for the axis texts
      Chart(P.width + 40, P.height + 40)(xAxis +: dataSeries :+ yAxis: _*)
    }
  }
  val component = ReactComponentB[Props]("LinesChart")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(width: Int, height: Int, lines: List[LineD]) = component(Props(width, height, lines))
}
