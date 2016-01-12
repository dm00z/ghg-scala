package model

case class Plant(tpe: WaterType.Value, name: String, addr: String)

object TechMethod extends Enumeration {
  val An = Value("Yếm khí")
  val Ae = Value("Hiếu khí")
  val Hyb = Value("Hỗn hợp")
}

/** @param power m3/day - Lưu lượng dòng vào hệ thống xử lý
  * @note Công nghệ xử lý hiện không dùng trong tính toán */
case class InfoData(f: Plant, power: Double, tech: TechMethod.Value)
