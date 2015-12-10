package ghg.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticRelationData.Data
import model.WaterType
import monocle.Lens
import scalacss.Defaults._

/** KineticRelation.Data component */
object KRData {
  object Styles extends StyleSheet.Standalone {
    import dsl._
    @inline def kr = "krdata"

    s".$kr, .$kr tr, .$kr td" - (
      border(1.px, solid, black),
      borderCollapse.collapse
    )
  }

  case class Props(d: ModelProxy[Data], tpe: WaterType.Value)
  private val ThongSo = <.td(<.b("Thông số"))
  private val DonVi = <.td(<.b("Đơn vị"))
  private val GiaTri = <.td(<.b("Giá trị"))

  @inline private def td(n: String, sub: String) = <.td(n, <.sub(sub))
  @inline private def td(n: String, sub: String, sup: String) = <.td(n, <.sup(sup), <.sub(^.marginLeft := "-10px", sub))
  case class Backend($: BackendScope[Props, _]) {
    import ghg.Utils._

    def render(P: Props) = {
      val p = P.d()
      def onChange(lens: Lens[Data, Double]): ReactEventI => Callback = e => {
        val valid = e.target.value.decimal
        Callback.ifTrue(valid, P.d.dispatch(P.tpe -> lens.set(e.target.value.toDouble)(p)))
      }

      def _td(lens: Lens[Data, Double]) = <.td(<.input(
        ^.value := lens.get(p),
        ^.onChange ==> onChange(lens))
      )

      <.div(
        <.h3("1. Quá trình hiếu khí"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2"), td("Y", "VSS"), td("Y", "Alk"), td("r", "CO2")),
          <.tr(DonVi, <.td("gCO2/gBOD"), <.td("gVSS/gBOD"), <.td("gCaCO3/gBOD"), <.td("gO2/gBOD")),
          <.tr(GiaTri, _td(Data.yCO2), _td(Data.yVSS), _td(Data.yAlk), _td(Data.rCO2))
        )),
        <.h4("* Phân hủy nội bào trong hiếu khí"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2,decay"), td("Y", "Alk,decay"), td("r", "CO2,decay")),
          <.tr(DonVi, <.td("gCO2/gVSS"), <.td("gCaCO3/gVSS"), <.td("gO2/gVSS")),
          <.tr(GiaTri, _td(Data.yCO2Decay), _td(Data.yAlkDecay), _td(Data.rCO2Decay))
        )),
        <.h3("2. Quá trình yếm khí"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2", "an"), td("Y", "CH4", "an"), td("Y", "VSS", "an"), td("Y", "Alk", "an")),
          <.tr(DonVi, <.td("kgCO2/kgBOD"), <.td("kgCH4/kgBOD"), <.td("kgVSS/kgBOD"), <.td("kgCaCO3/kgBOD")),
          <.tr(GiaTri, _td(Data.yCO2An), _td(Data.yCH4An), _td(Data.yVSSAn), _td(Data.yAlk))
        )),
        <.h4("* Phân hủy nội bào trong yếm khí"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2,decay", "an"), td("Y", "CH4,decay", "an"), td("Y", "Alk,decay", "an")),
          <.tr(DonVi, <.td("kgCO2/kgVSS"), <.td("kgCH4/kgVSS"), <.td("gCaCO3/gVSS")),
          <.tr(GiaTri, _td(Data.yCO2AnDecay), _td(Data.yCH4AnDecay), _td(Data.yAlkAnDecay))
        )),
        <.h3("3. Quá trình khử Nitơ"),
        <.h4("* Quá trình Nitrat hóa"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "VSS,nit"), td("Y", "NO3,nit"), td("r", "O2,nit"), td("r", "rCO2,nit"), td("r", "Alk,nit")),
          <.tr(DonVi, <.td("gVSS/gN"), <.td("gN-NO3/gN"), <.td("gO2/gN"), <.td("gCO2/gN"), <.td("gCaCO3/gN")),
          <.tr(GiaTri, _td(Data.yVSSNit), _td(Data.yNO3Nit), _td(Data.rO2Nit), _td(Data.rCO2Nit), _td(Data.rAlkNit))
        )),
        <.h4("* Quá trình khử Nitrat"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "VSS,dnt"), td("Y", "CO2,dnt"), td("Y", "Alk,dnt"), td("r", "BOD,dnt"), td("r", "Methanol,dnt")),
          <.tr(DonVi, <.td("gVSS/gN-NO3"), <.td("gCO2/gN-NO3"), <.td("gCaCO3/gN-NO3"), <.td("gBOD/gN-NO3"), <.td("gCH3OH/gN-NO3")),
          <.tr(GiaTri, _td(Data.yVSSDnt), _td(Data.yCO2Dnt), _td(Data.yAlkDnt), _td(Data.rBODDnt), _td(Data.rMethanolDnt))
        )),
        <.h3("4. Phân hủy yếm khí"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2,dr"), td("Y", "CH4,dr"), td("Y", "VSS,dr"), td("Y", "Alk,dr")),
          <.tr(DonVi, <.td("gCO2/gBOD"), <.td("gCH4/gBOD"), <.td("gVSS/gBOD"), <.td("gCaCO3/gBOD")),
          <.tr(GiaTri, _td(Data.yCO2Dr), _td(Data.yCH4Dr), _td(Data.yVSSDr), _td(Data.yAlkDr))
        )),
        <.h4("* Phân hủy sinh khối"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CO2,decay", "dr"), td("Y", "CH4,decay", "dr"), td("Y", "Alk,decay", "dr")),
          <.tr(DonVi, <.td("gCO2/gVSS"), <.td("gCH4/gVSS"), <.td("gCaCO3/gVSS")),
          <.tr(GiaTri, _td(Data.yCO2DrDecay), _td(Data.yCH4DrDecay), _td(Data.yAlkDrDecay))
        )),
        <.h3("5. Quá trình đốt"),
        <.table(^.className := Styles.kr, <.tbody(
          <.tr(ThongSo, td("Y", "CH4,combusion")),
          <.tr(DonVi, <.td("gCO2/gCH4")),
          <.tr(GiaTri, _td(Data.yCH4Combusion))
        ))
      )
    }
  }

  val component = ReactComponentB[Props]("KRData")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[Data], tpe: WaterType.Value) = component(Props(d, tpe))
}
