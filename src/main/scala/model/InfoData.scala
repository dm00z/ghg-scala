package model

case class Plant(tpe: String, name: String, addr: String)

/** @param power TODO đơn vị?
  * @note Công nghệ xử lý hiện không dùng trong tính toán */
case class InfoData(f: Plant, power: Double)
