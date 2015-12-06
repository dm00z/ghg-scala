package model

object MaterialData {
  object Ratio {
    val Methanol = 1.54
    val Alkali = 1.74
    val FerricChloride = 2.71
    val Ref = "Bani Shahabadi et al., 2009; Maas, 2009"
  }
}
case class MaterialData(methanol: Double = 0, alkali: Double = 0, fe: Double = 0)
