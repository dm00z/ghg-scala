package model

object KineticRelationData {
//  case class NamedData(symbol: String, unit: String, value: Double)
//  type Ds = Seq[NamedData]
//  case class Data(aerobic1: Ds, aerobic2: Ds,
//                  anaerobic1: Ds, anaerobic2: Ds,
//                  nitrate: Ds, denitrate: Ds,
//                  decay1: Ds, decay2: Ds,
//                  incinerate: Ds)
//
//  import KineticRelationData.{NamedData => D}
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

import KineticRelationData._
case class KineticRelationData(domestic: Data, industrial: Data)
