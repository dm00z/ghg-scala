package model

object MaterialData {
  object Ratio {
    val Methanol = 1.54
    val Alkali = 1.74
    val FerricChloride = 2.71
    val Javen = 1.74
    val Polimer = 1.74

    def all = List(
      "Hệ số phát thải của nguyên liệu (gCO2/gNguyenLieu)",
      Methanol.toString,
      Alkali.toString,
      FerricChloride.toString,
      Javen.toString,
      Polimer.toString,
      "[Ref: Bani Shahabadi et al., 2009; Maas, 2009]"
    )
  }
}
case class MaterialData(methanol: Double = 0,
                        alkali: Double = 0,
                        fe: Double = 0,
                        ja: Double = 0,
                        po: Double = 0) {
  def all = "Khối lượng nguyên liệu (kg/ngày)" +: List(methanol, alkali, fe, ja, po).map(_.toString)
}

