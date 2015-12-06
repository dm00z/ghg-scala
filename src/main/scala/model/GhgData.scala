package model

case class R[T](min: T, max: T)

case class GhgData(info: InfoData, indirect: IndirectData) {
  import ElectricData.CalcMethod._, indirect.electric._
  def power: Double = method match {
    case Method1 => _1.fold(0D)(d => info.power * d.ratio)
    case Method2 => _2.fold(0D)(_.power)
    case Method3 => _3.fold(0D)(_.power)
  }
}

object GhgData {
  val testData = GhgData(
    InfoData(
      Factory(
        "Nước thải công nghiệp",
        "Nhà máy xử lý nước thải cho công ty Giấy Bãi Bằng",
        "Thị trấn Phong Châu, huyện Phù Ninh, tỉnh Phú Thọ"),
      8000
    ),
    IndirectData()
  )
}
