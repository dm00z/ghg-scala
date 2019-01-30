package ghg.components

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import tex.TeX._
import ghg.Utils._

object Bien3 {
  case class Props(d: GhgData, isAerobic: Boolean)
  case class Backend($: BackendScope[Props, _]) {
    def render(props: Props) = {
      val d = props.d
      val b1 = d.bien1
      val b2ae = d.bien2Ae
      val pPool = d.direct.d.primaryPool
      val dPool = d.direct.d.decayPool
      val relation = d.direct.relation
      val (b2, b3) = if(props.isAerobic) (d.bien2Ae, d.bien3) else (d.bien2Ana, d.bien3Ana)
      val ane = d.direct.coef.anaerobic

      <.div(^.className := "calcTable",
        <.h3("3. Lượng CO2 từ phân hủy BOD còn lại trong dòng ra"),        
        <.div("Công thức tính: `MCO2, BODra = (Y_(CO2) +Y_(VSS).Y_(CO2,phanhuy))*S*Qr`".teX),
        dataTbl(
          tr("MCO2, BODra", b3.m_CO2BODra, "g/ngày"),
          tr("Y", "CO2", relation.yCO2, ""),
          tr("Y", "VSS", relation.yVSS, ""),
          tr("Y", "CO2,phanhuy", relation.yCO2Decay, ""),
          tr("S", b2ae.s, "mg/l"),
          tr("Qr", b2ae.q_v, "m3/ngày")
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Bien3")
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}
