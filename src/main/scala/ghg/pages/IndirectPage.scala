package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import scala.scalajs.js.JSNumberOps._

object IndirectPage {
  val component = ReactComponentB[GhgData]("Indirect")
    .render_P { d =>
      val ghg = d.ghgElectric + d.indirect.gas.ghg + d.indirect.material.ghg

      <.div(
        <.h2(s"Khí nhà kính gián tiếp = ${ghg.toFixed(3)} (kg", <.sub("CO2-td"), "/day)")
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
