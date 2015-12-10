package ghg

import model.R

object Utils {
  val decimalRegex = """^\-?[0-9]*\.?[0-9]+$"""
  implicit final class StringValidator(val v: String) extends AnyVal {
    @inline def decimal(v: String) = v matches decimalRegex
    @inline def >#(x: Double) = v.matches(decimalRegex) && v.toDouble > x
    @inline def >=#(x: Double) = v.matches(decimalRegex) && v.toDouble >= x
    @inline def <#(x: Double) = v.matches(decimalRegex) && v.toDouble < x
    @inline def <=#(x: Double) = v.matches(decimalRegex) && v.toDouble <= x
    @inline def between(r: R[Double]) = v.matches(decimalRegex) && { val n = v.toDouble; n >= r.min && n <= r.max }
    @inline def >|(x: Double): Double = v >| (x, x)
    @inline def >=|(x: Double): Double = v >=| (x, x)
    @inline def >|(x: Double, d: Double): Double = if (v ># x) v.toDouble else d
    @inline def >=|(x: Double, d: Double): Double = if (v >=# x) v.toDouble else d
    @inline def <|(x: Double): Double = v <| (x, x)
    @inline def <=|(x: Double): Double = v <=| (x, x)
    @inline def <|(x: Double, d: Double): Double = if (v <# x) v.toDouble else d
    @inline def <=|(x: Double, d: Double): Double = if (v <=# x) v.toDouble else d
  }
}
