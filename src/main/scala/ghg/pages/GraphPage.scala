package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{TechMethod, KineticCoefficientData, GhgData}
import reactd3.ChartSerie
import scala.scalajs.js
import tex.TeX._
import ghg.Utils._
import KineticCoefficientData._

object GraphPage {
  type Props = ModelProxy[GhgData]

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {

      <.div(

      )
    }
  }

  val component = ReactComponentB[Props]("Graph")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
