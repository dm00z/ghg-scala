package model

import monocle.macros.Lenses

object MaterialData {
  object Ratio {
    val Methanol = 1.54
    val Alkali = 1.74
    val FerricChloride = 2.71
    val Javen = 1.74
    val Polimer = 1.74

    val all = List(
      Methanol,
      Alkali,
      FerricChloride,
      Javen,
      Polimer
    )

    val names = List("Methanol", "Kiềm","Ferric chloride (FeCl3 .6H2O)", "Javen", "Polimer")
  }
}
@Lenses case class MaterialData(methanol: Double = 0,
                        alkali: Double = 1800,
                        fe: Double = 6000,
                        ja: Double = 0,
                        po: Double = 180) {
  val all = List(methanol, alkali, fe, ja, po)
  def ghg = MaterialData.Ratio.all.zip(all).map { case (r, v) => r * v }.sum
}

