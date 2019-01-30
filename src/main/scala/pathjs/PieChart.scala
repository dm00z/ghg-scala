package pathjs

import scala.scalajs.js
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all.key
import japgolly.scalajs.react.vdom.svg.all._
import paths.high.Pie

import pathjs.Colors._

object PieChart {
  case class Percentage(name: String, value: Int)

  private def move(p: js.Array[Double]) = s"translate(${ p(0) },${ p(1) })"
  private val palette = mix(Color(130, 140, 210), Color(180, 205, 150))


  val PieChart = ReactComponentB[List[Percentage]]("Pie chart")
    .render_P(percentages => {
      val pie = Pie[Percentage](
        data = percentages,
        accessor = _.value,
        r = 60,
        R = 140,
        center = (0, 0)
      )
      val slices = pie.curves map { curve =>
        g(key := curve.item.name)(
          lineargradient(
            id := s"grad-${ curve.index }",
            stop(stopColor := string(palette(curve.index)), offset := "0%"),
            stop(stopColor := string(lighten(palette(curve.index))), offset := "100%")
          ),
          path(d := curve.sector.path.print, fill := s"url(#grad-${ curve.index })"),
          text(
            transform := move(curve.sector.centroid),
            textAnchor := "middle",
            curve.item.name
          )
        )
      }

      svg(width := 400, height := 400,
        g(transform := move(js.Array(200, 200)), slices)
      )
    })
    .build
}
