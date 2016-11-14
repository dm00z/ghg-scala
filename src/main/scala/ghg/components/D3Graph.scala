package ghg.components

import model.R
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.singlespaced.d3js.d3
import org.singlespaced.d3js.d3._
import reactfauxdom.ReactFauxDOM

import scala.scalajs.js

object D3Graph {
  case class Props(width: Double, height: Double,
                   y: String, fomula: String, f: Double => Double, range: R[Double], step: Double,
                   interpolate: String, attrs: js.Dictionary[d3.Primitive]) {
    lazy val data: js.Array[Double] = {
      val a: js.Array[Double] = js.Array()
      var x = range.min
      while (x < range.max) {
        a push f(x)
        x += step
      }
      a
    }
  }

  private type D = (Double, Int)
  private type DF = js.Function2[D, Int, Double]
  private type DSF = (D, Int) => Double

  case class Backend($: BackendScope[Props, _]) {
    /**
      * @see [[https://www.dashingd3js.com/svg-paths-and-d3js]]
      */
    def render(P: Props) = {
      //The data for our line
      val lineData = js.Array[Double](1, 9, 5, 10, 3, 99, 4).zipWithIndex

      //The accessor function to return the x,y coordinates from our data
      val lineFunction = d3.svg.line[D]()
        .x((d: D, i: Int) => d._2.toDouble)
        .y((d: D, i: Int) => d._1)
        .interpolate("linear")

      //The SVG Container
      val svgContainer = d3.select(ReactFauxDOM.createElement("svg"))
        .attr("width", 200)
        .attr("height", 200)

      //The line SVG Path we draw
      svgContainer.append("path")
        .attr("d", lineFunction(lineData))
        .attr("stroke", "blue")
        .attr("stroke-width", 2)
        .attr("fill", "none")

      svgContainer.node().asInstanceOf[reactfauxdom.Element].toReact()
    }
    def render1(P: Props) = {
      val xScale = d3.scale.linear()
        .range(js.Array(0, P.width))
//        .range(js.Array(P.range.min, P.range.max))
//        .domain(d3.extent(P.data, (d: Double, i: Double) => i))
        .domain(js.Array(P.data.min, P.data.max))

      val yScale = d3.scale.linear()
        .range(js.Array(P.height, 0))
//        .domain(d3.extent(P.data, (d: Double, i: Double) => d))
//      .domain(d3.extent(P.data))
        .domain(js.Array(P.data.min, P.data.max))

      val line = d3.svg.line()
        .x((d: Double, i: Int) => xScale(i))
        .y((d: Double, i: Int) => yScale(d))
        .interpolate(P.interpolate)

      val el = P.attrs.foldLeft(
        d3.select(ReactFauxDOM.createElement("svg"))
          .attr("className", "visitors")
          .attr("width", P.width)
          .attr("height", P.height)
          .attr("data", null: String)
      ){
        case (e, (k, v)) => e.attr(k, v)
      }

      el.append("path")
        .datum(P.data)
        .attr("key", "sparkline")
        .attr("className", "sparkline")
        .attr("d", line.asInstanceOf[js.Function3[js.Array[Double], Int, Int, Primitive]])

      el.node().asInstanceOf[reactfauxdom.Element].toReact()
    }
  }

  val component = ReactComponentB[Props]("D3Grapth")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(width: Double, height: Double,
            y: String, fomula: String, f: Double => Double, range: R[Double],
            step: Double = 1,
            interpolate: String = "linear", attrs: js.Dictionary[d3.Primitive] = js.Dictionary()) =
    component(Props(width, height, y, fomula, f, range, step, interpolate, attrs))
}
