package model

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
case class MaterialData(methanol: Double = 0,
                        alkali: Double = 0,
                        fe: Double = 0,
                        ja: Double = 0,
                        po: Double = 0) {
  def all = List(methanol, alkali, fe, ja, po)
}

