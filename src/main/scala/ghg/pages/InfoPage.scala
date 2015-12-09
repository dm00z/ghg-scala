package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{GhgData, InfoData}
import scala.language.existentials

object InfoPage {
  type Props = ModelProxy[InfoData]

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = <.div(
      <.div("(info page)")
    )
  }

  val component = ReactComponentB[Props]("Info")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d.zoom(_.info))
}
