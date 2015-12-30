package d3charts

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.svg.prefix_<^._
import org.singlespaced.d3js.scale.Linear
import scala.scalajs.js
import scala.scalajs.js.JSArrayOps.jsArrayOps

object Axis {
  case class Props(axis: Char,
                   x: Double, y: Double,
                   scale: Linear[Double, Double],
                   displayScale: Linear[Double, Double],
                   tickFormatter: Double => String = _.toString) {
    def axisLineX(v1: Double, v2: Double, tickLength: Double = 0) =
      <.line(
        ^.stroke := "black",
        ^.strokeWidth := 2,
        ^.x1 := scale(v1) + x,
        ^.x2 := scale(v2) + x,
        ^.y1 := y,
        ^.y2 := y + tickLength
      )
    def axisLineY(v1: Double, v2: Double, tickLength: Double = 0) =
      <.line(
        ^.stroke := "black",
        ^.strokeWidth := 2,
        ^.y1 := scale(v1) + y,
        ^.y2 := scale(v2) + y,
        ^.x1 := x,
        ^.x2 := x + tickLength
      )

    def tickX(tick: Double, xOffset: Double): TagMod = {
      val t = displayScale(tick)
      println(s"tickX: $tick, $t")
      <.g(
        axisLineX(t, t, 8),
        <.text(
          ^.textAnchor := "middle",
          ^.x := scale(t) + x + xOffset, //tickOffsetAxis
          ^.y := y + 20, //tickOffsetOther
          tickFormatter(tick)
        )
      )
    }

    def tickY(tick: Double) = {
      val t = displayScale(tick)
      println(s"tickY: $tick, $t")
      <.g(
        axisLineY(t, t, -8),
        <.text(
          ^.textAnchor := "end",
          ^.y := scale(t) + y + 5, //tickOffsetAxis
          ^.x := x - 16, //tickOffsetOther
          tickFormatter(tick)
        )
      )
    }

    def render = {
      val (min, max) = scale.domain().asInstanceOf[js.Tuple2[Double, Double]]: (Double, Double)
      if (axis == 'x')
        <.g(axisLineX(min, max) +: displayScale.ticks(6).jsMap(
          (tick: Double, i: Int) => tickX(tick, if(i == 0) 10 else 0)
        ): _*)
      else
        <.g(axisLineY(min, max) +: displayScale.ticks(6).map(tickY): _*)
    }
  }

  val component = ReactComponentB[Props]("Axis")
    .stateless
    .render_P(_.render)
    .build
}
