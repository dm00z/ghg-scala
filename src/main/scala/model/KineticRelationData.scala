package model

import monocle.macros.Lenses

/** Mối quan hệ động học - kiểu dữ liệu chung cho cả sinh hoạt / công nghiệp */
@Lenses case class KineticRelationData(
  yCO2: Double, yVSS: Double, yAlk: Double, rCO2: Double,
  yCO2Decay: Double, yAlkDecay: Double, rCO2Decay: Double,
  yCO2An: Double, yCH4An: Double, yVSSAn: Double, yAlkAn: Double,
  yCO2AnDecay: Double, yCH4AnDecay: Double, yAlkAnDecay: Double,
  yVSSNit: Double, yNO3Nit: Double, rO2Nit: Double, rCO2Nit: Double, rAlkNit: Double,
  yVSSDnt: Double, yCO2Dnt: Double, yAlkDnt: Double, rBODDnt: Double, rMethanolDnt: Double,
  yCO2Dr: Double, yCH4Dr: Double, yVSSDr: Double, yAlkDr: Double,
  yCO2DrDecay: Double, yCH4DrDecay: Double, yAlkDrDecay: Double,
  yCH4Combusion: Double)

object KineticRelationData {
  val dataIndustrial = KineticRelationData(
    0.49, 0.43, 0.03, 0.41,
    1.56, 0.44,	1.42,
    0.428, 0.236, 0.058, 0.192,
    0.58, 0.35, 0.39,
    0.31, 0.96, 3.96, 0.48, 7,
    0.175, 3.228, 4.231, 0.250, 1.9,
    0.45, 0.24, 0.04, 0.2,
    0.58, 0.35, 0.39,
    2.75
  )

  val dataDomestic = KineticRelationData(
    0.33, 0.422, 0, 0.4,
    1.56, 0, 1.42,
    0.28, 0.235, 0.035, 0.108,
    0.58, 0.35, 0.39,
    0.55, 0.98, 4.32, 0.247, 0,
    0.175, 2.767, 3.952, 2.059, 1.9,
    0.28, 0.23, 0.042, 0.105,
    0.58, 0.35, 0.39,
    2.75
  )
}
object WaterType extends Enumeration {
  val Domestic = Value("Nước thải sinh hoạt")
  val Industrial = Value("Nước thải công nghiệp")
}
