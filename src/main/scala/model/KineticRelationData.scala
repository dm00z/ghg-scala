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

//  object Data {
//    import upickle.Js
//    import upickle.default.{Writer, Reader}
//    implicit val _writer: Writer[Data] = Writer[Data]{ d =>
//      upickle.default.writeJs(Seq(
//        d.yCO2, d.yVSS, d.yAlk, d.rCO2,
//        d.yCO2Decay, d.yAlkDecay, d.rCO2Decay,
//        d.yCO2An, d.yCH4An, d.yVSSAn, d.yAlkAn,
//        d.yCO2AnDecay, d.yCH4AnDecay, d.yAlkAnDecay,
//        d.yVSSNit, d.yNO3Nit, d.rO2Nit, d.rCO2Nit, d.rAlkNit,
//        d.yVSSDnt, d.yCO2Dnt, d.yAlkDnt, d.rBODDnt, d.rMethanolDnt,
//        d.yCO2Dr, d.yCH4Dr, d.yVSSDr, d.yAlkDr,
//        d.yCO2DrDecay, d.yCH4DrDecay, d.yAlkDrDecay,
//        d.yCH4Combusion
//      ))
//    }
//    implicit val _reader: Reader[Data] = Reader[Data] {
//      case Js.Arr(
//      Js.Num(yCO2), Js.Num(yVSS), Js.Num(yAlk), Js.Num(rCO2),
//      Js.Num(yCO2Decay), Js.Num(yAlkDecay), Js.Num(rCO2Decay),
//      Js.Num(yCO2An), Js.Num(yCH4An), Js.Num(yVSSAn), Js.Num(yAlkAn),
//      Js.Num(yCO2AnDecay), Js.Num(yCH4AnDecay), Js.Num(yAlkAnDecay),
//      Js.Num(yVSSNit), Js.Num(yNO3Nit), Js.Num(rO2Nit), Js.Num(rCO2Nit), Js.Num(rAlkNit),
//      Js.Num(yVSSDnt), Js.Num(yCO2Dnt), Js.Num(yAlkDnt), Js.Num(rBODDnt), Js.Num(rMethanolDnt),
//      Js.Num(yCO2Dr), Js.Num(yCH4Dr), Js.Num(yVSSDr), Js.Num(yAlkDr),
//      Js.Num(yCO2DrDecay), Js.Num(yCH4DrDecay), Js.Num(yAlkDrDecay),
//      Js.Num(yCH4Combusion)
//      ) =>
//        Data(yCO2, yVSS, yAlk, rCO2,
//          yCO2Decay, yAlkDecay, rCO2Decay,
//          yCO2An, yCH4An, yVSSAn, yAlkAn,
//          yCO2AnDecay, yCH4AnDecay, yAlkAnDecay,
//          yVSSNit, yNO3Nit, rO2Nit, rCO2Nit, rAlkNit,
//          yVSSDnt, yCO2Dnt, yAlkDnt, rBODDnt, rMethanolDnt,
//          yCO2Dr, yCH4Dr, yVSSDr, yAlkDr,
//          yCO2DrDecay, yCH4DrDecay, yAlkDrDecay,
//          yCH4Combusion)
//    }
//  }
}

object WaterType extends Enumeration {
  val Domestic = Value("Nước thải sinh hoạt")
  val Industrial = Value("Nước thải công nghiệp")
}

import KineticRelationData._
case class KineticRelationData(tpe: WaterType.Value, domestic: Data, industrial: Data) {
  val value = tpe match {
    case WaterType.Domestic => domestic
    case WaterType.Industrial => industrial
  }
}
