package ghg.pages

import chandu0101.scalajs.react.components.materialui.{MuiTab, MuiTabs}
import diode.react.ModelProxy
import ghg.components.KRData
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.{WaterType, GhgData}
import scala.language.existentials
import scala.scalajs.js.|

object KineticRelationPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.relation)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      import WaterType._
      val my = P.my()
      MuiTabs(value = my.tpe.id.toString: String | Double)(
        MuiTab(value = Domestic.id.toString, label = Domestic.toString)(
          KRData(P.my.zoom(_.domestic))
        ),
        MuiTab(value = Industrial.id.toString, label = Industrial.toString)(
          KRData(P.my.zoom(_.industrial))
        )
      )
    }
  }

  val component = ReactComponentB[Props]("KineticRelation")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
