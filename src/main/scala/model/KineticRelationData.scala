package model

object KineticRelationData {

  /** Mối quan hệ động học - chung cho mọi nhà máy */
  case class Data(yC02: Double, yVSS: Double, yAlk: Double, rCO2: Double,
                  yC02Decay: Double, yAlkDecay: Double, rCO2Decay: Double,
                  yC02An: Double, yCH4An: Double, yVSSAn: Double, yAlkAn: Double,
                  yC02AnDecay: Double, yCH4AnDecay: Double, yAlkAnDecay: Double,
                  yVSSNit: Double, yNO2Nit: Double, rO2Nit: Double, rCO2Nit: Double, rAlkNit: Double,
                  yVSSDnt: Double, yCO2Dnt: Double, yAlkDnt: Double, rBODDnt: Double, rMethanolDnt: Double,
                  yC02Dr: Double, yCH4Dr: Double, yVSSDr: Double, yAlkDr: Double,
                  yC02DrDecay: Double, yCH4DrDecay: Double, yAlkDrDecay: Double,
                  yCH4Combusion: Double)
}

object WaterType extends Enumeration {
  val Domestic = Value("Nước thải sinh hoạt")
  val Industrial = Value("Nước thải công nghiệp")
}

import KineticRelationData._
case class KineticRelationData(tpe: WaterType.Value, domestic: Data, industrial: Data)
