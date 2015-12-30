package model

import monocle.macros.Lenses

object KineticCoefficientData {
  @Lenses case class KT(vNorm: Double, coeff: Double, tNorm: Double = 20) extends (Double => Double) {
    def apply(t: Double) = vNorm * Math.pow(coeff, t - tNorm)
  }
  object Aerobic {
    val Y = RNorm(R(.4, .8), .5)
    val Ks = RNorm(R(25, 100), 60)
    val Kd = RNorm(R(.02, .1), .1)

    def default = Aerobic(KT(4, 1.07), KT(60, 1), KT(Kd.norm, 1.04), KT(Y.norm, 1))
  }
  @Lenses case class Aerobic(m: KT, ks: KT, kd: KT, y: KT, t: Double = 25) {
    def k(t: Double) = m(t) / y(t)
  }

  object Nitrate {
    val M = RNorm(R(.4, 2), .45)
    val Y = RNorm(R(.1, .3), .16)
    val Kd = RNorm(R(.03, .06), .04)

    @Lenses case class MT(vNorm: Double, tNorm: Double) {
      def apply(t: Double) = vNorm * Math.exp(.089 * (t - tNorm))
    }

    val default = Nitrate(MT(M.norm, 15), KT(Y.norm, 1), KT(Kd.norm, 1.04))
  }
  @Lenses case class Nitrate(m: Nitrate.MT, y: KT, kd: KT) {
    def kn(t: Double) = Math.pow(10, .05*t - 1.158) //fixme why not (t - tNorm)?
    def k(t: Double) = m(t) / y(t)
  }

  //TODO impl - không biết công thức!
  object Anaerobic {
    val Y = RNorm(R(.05, .1), .08)
    val KD = RNorm(R(.02, .04), .03)

    val default = Anaerobic(KT(.2, 1.06, 25), Y.norm, KT(900, 0.841371404, 25), KD.norm, .15)
  }

  /** Quá trình yếm khí
    *
    * @param m day^-1
    * @param y mg/mg
    * @param ks mg/l
    * @param kd day^-1
    * @param f ?? - FIXME not use
    * @note Giả định m, kd cũng tuân theo công thức KT
    */
  case class Anaerobic(m: KT, y: Double, ks: KT, kd: Double, f: Double) {
    def k(t: Double) = m(t) / y
  }
}

import KineticCoefficientData._
case class KineticCoefficientData(aerobic: Aerobic, nitrate: Nitrate, anaerobic: Anaerobic)