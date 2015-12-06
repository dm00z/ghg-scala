package model

object KineticCoefficientData {
  case class RNorm(range: R[Double], norm: Double)
  case class KT(vNorm: Double, coeff: Double, tNorm: Int = 20) {
    def apply(t: Int) = vNorm * Math.pow(coeff, t - tNorm)
  }
  object Aerobic {
    val Y = RNorm(R(.4, .8), .5)
    val Ks = RNorm(R(25, 100), 60)
    val Kd = RNorm(R(.02, .1), .1)

    def default = Aerobic(KT(4, 1.07), KT(Kd.norm, 1.04), Y.norm)
  }
  case class Aerobic(m: KT, kd: KT, y: Double) {
    def k(t: Int) = m(t) / y
  }

  object Nitrate {
    val M = RNorm(R(.4, 2), .45)
    val Y = RNorm(R(.1, .3), .16)
    val Kd = RNorm(R(.03, .06), .04)

    case class MT(vNorm: Double, tNorm: Int) {
      def apply(t: Int) = vNorm * Math.exp(.089 * (t - tNorm))
    }

    val default = Nitrate(MT(M.norm, 15), Y.norm, KT(Kd.norm, 1.04))
  }
  case class Nitrate(m: Nitrate.MT, y: Double, kd: KT) {
    def kn(t: Int) = Math.pow(10, .05*t - 1.158) //fixme why not (t - tNorm)?
    def k(t: Int) = m(t) / y
  }

  //TODO impl - không biết công thức!
  object Anaerobic {

  }
}

import KineticCoefficientData._
case class KineticCoefficientData(aerobic: Aerobic, nitrate: Nitrate)