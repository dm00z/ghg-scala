package ghg.pages

import chandu0101.scalajs.react.components.materialui.{MuiTab, MuiTabs}
import diode.react.ModelProxy
import ghg.components.KRData
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.{WaterType, GhgData}
import scala.scalajs.js.|

//TODO remove
object KineticRelationPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.relation)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      KRData(P.my)
    }
  }

  val component = ReactComponentB[Props]("KineticRelation")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
