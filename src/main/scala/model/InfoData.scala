package model

case class Plant(tpe: String, name: String, addr: String)

/** @param power m3/day - Lưu lượng dòng vào hệ thống xử lý
  * @note Công nghệ xử lý hiện không dùng trong tính toán */
case class InfoData(f: Plant, power: Double)
