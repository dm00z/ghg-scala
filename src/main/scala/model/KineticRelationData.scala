package model

import monocle.macros.Lenses

object KineticRelationData {
  /** Mối quan hệ động học - chung cho mọi nhà máy */
  @Lenses case class Data(yCO2: Double, yVSS: Double, yAlk: Double, rCO2: Double,
                  yCO2Decay: Double, yAlkDecay: Double, rCO2Decay: Double,
                  yCO2An: Double, yCH4An: Double, yVSSAn: Double, yAlkAn: Double,
                  yCO2AnDecay: Double, yCH4AnDecay: Double, yAlkAnDecay: Double,
                  yVSSNit: Double, yNO3Nit: Double, rO2Nit: Double, rCO2Nit: Double, rAlkNit: Double,
                  yVSSDnt: Double, yCO2Dnt: Double, yAlkDnt: Double, rBODDnt: Double, rMethanolDnt: Double,
                  yCO2Dr: Double, yCH4Dr: Double, yVSSDr: Double, yAlkDr: Double,
                  yCO2DrDecay: Double, yCH4DrDecay: Double, yAlkDrDecay: Double,
                  yCH4Combusion: Double)
}

object WaterType extends Enumeration {
  val Domestic = Value("Nước thải sinh hoạt")
  val Industrial = Value("Nước thải công nghiệp")
}

import KineticRelationData._
case class KineticRelationData(tpe: WaterType.Value, domestic: Data, industrial: Data)
