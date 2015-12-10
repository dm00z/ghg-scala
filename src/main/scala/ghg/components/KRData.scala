package ghg.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticRelationData.Data

/** KineticRelation.Data component */
object KRData {
  type Props = ModelProxy[Data]

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P()
      <.div(
        "TODO impl KRData"
      )
    }
  }

  val component = ReactComponentB[Props]("KRData")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[Data]) = component(d)
}
