package ghg.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{KineticRelationData => Data, GhgData}

/** KineticRelation.Data component */
object KRData {
  type Props = ModelProxy[Data]

  case class Backend($: BackendScope[Props, _]) {
    import ghg.Utils._

    def render(P: Props) = {
      implicit val p: Data = P()
      implicit val dispatch: Data => Callback = d => P.dispatch(d)

      <.div(
        <.h3("1. Quá trình hiếu khí"),
        table(
          <.tr(ThongSo, td("Y", "CO2"), td("Y", "VSS"), td("r", "CO2")),
          <.tr(DonVi, <.td("gCO2/gBOD"), <.td("gVSS/gBOD"), <.td("gO2/gBOD")),
          <.tr(GiaTri, tdInput(Data.yCO2), tdInput(Data.yVSS), tdInput(Data.rCO2))
        ),
        <.h4("* Phân hủy nội bào trong hiếu khí"),
        table(
          <.tr(ThongSo, td("Y", "CO2,decay"), td("r", "CO2,decay")),
          <.tr(DonVi, <.td("gCO2/gVSS"), <.td("gO2/gVSS")),
          <.tr(GiaTri, tdInput(Data.yCO2Decay), tdInput(Data.rCO2Decay))
        ),
//        <.h3("2. Quá trình yếm khí"),
//        table(
//          <.tr(ThongSo, td("Y", "CO2", "an"), td("Y", "CH4", "an"), td("Y", "VSS", "an")),
//          <.tr(DonVi, <.td("kgCO2/kgBOD"), <.td("kgCH4/kgBOD"), <.td("kgVSS/kgBOD")),
//          <.tr(GiaTri, tdInput(Data.yCO2An), tdInput(Data.yCH4An), tdInput(Data.yVSSAn))
//        ),
//        <.h4("* Phân hủy nội bào trong yếm khí"),
//        table(
//          <.tr(ThongSo, td("Y", "CO2,decay", "an"), td("Y", "CH4,decay", "an")),
//          <.tr(DonVi, <.td("kgCO2/kgVSS"), <.td("kgCH4/kgVSS"), <.td("gCaCO3/gVSS")),
//          <.tr(GiaTri, tdInput(Data.yCO2AnDecay), tdInput(Data.yCH4AnDecay))
//        ),
        <.h3("2. Quá trình khử Nitơ"),
        <.h4("* Quá trình Nitrat hóa"),
        table(
          <.tr(ThongSo, td("Y", "VSS,nit"), td("Y", "NO3,nit"), td("r", "O2,nit"), td("r", "rCO2,nit")),
          <.tr(DonVi, <.td("gVSS/gN"), <.td("gN-NO3/gN"), <.td("gO2/gN"), <.td("gCO2/gN")),
          <.tr(GiaTri, tdInput(Data.yVSSNit), tdInput(Data.yNO3Nit), tdInput(Data.rO2Nit), tdInput(Data.rCO2Nit))
        ),
        <.h4("* Quá trình khử Nitrat"),
        table(
          <.tr(ThongSo, td("Y", "VSS,dnt"), td("Y", "CO2,dnt"), td("r", "BOD,dnt"), td("r", "Methanol,dnt")),
          <.tr(DonVi, <.td("gVSS/gN-NO3"), <.td("gCO2/gN-NO3"), <.td("gBOD/gN-NO3"), <.td("gCH3OH/gN-NO3")),
          <.tr(GiaTri, tdInput(Data.yVSSDnt), tdInput(Data.yCO2Dnt), tdInput(Data.rBODDnt), tdInput(Data.rMethanolDnt))
        ),
        <.h3("3. Phân hủy yếm khí"),
        table(
          <.tr(ThongSo, td("Y", "CO2,dr"), td("Y", "CH4,dr"), td("Y", "VSS,dr")),
          <.tr(DonVi, <.td("gCO2/gBOD"), <.td("gCH4/gBOD"), <.td("gVSS/gBOD")),
          <.tr(GiaTri, tdInput(Data.yCO2Dr), tdInput(Data.yCH4Dr), tdInput(Data.yVSSDr))
        ),
        <.h4("* Phân hủy sinh khối"),
        table(
          <.tr(ThongSo, td("Y", "CO2,decay", "dr"), td("Y", "CH4,decay", "dr")),
          <.tr(DonVi, <.td("gCO2/gVSS"), <.td("gCH4/gVSS")),
          <.tr(GiaTri, tdInput(Data.yCO2DrDecay), tdInput(Data.yCH4DrDecay))
        ),
        <.h3("4. Quá trình đốt"),
        table(
          <.tr(ThongSo, td("Y", "CH4,combusion")),
          <.tr(DonVi, <.td("gCO2/gCH4")),
          <.tr(GiaTri, tdInput(Data.yCH4Combusion))
        )
      )
    }
  }

  val component = ReactComponentB[Props]("KRData")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d.zoom(_.direct.relation))
}
