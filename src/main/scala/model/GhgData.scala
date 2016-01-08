package model

case class R[T](min: T, max: T) {
  def text = s"$min - $max"
}
case class RNorm(range: R[Double], norm: Double)

case class GhgData(info: InfoData, indirect: IndirectData, direct: DirectData) {
  def electricPower: Double = {
    import ElectricData.CalcMethod._, indirect.electric._
    method match {
      case Method1 => info.power * _1.ratio
      case Method2 => _2.power
      case Method3 => _3.power
    }
  }
}
