package model

case class R[T](min: T, max: T)

case class GhgData(info: InfoData, indirect: IndirectData, direct: DirectData) {
  import ElectricData.CalcMethod._, indirect.electric._
  def power: Double = method match {
    case Method1 => _1.fold(0D)(d => info.power * d.ratio)
    case Method2 => _2.fold(0D)(_.power)
    case Method3 => _3.fold(0D)(_.power)
  }
}
