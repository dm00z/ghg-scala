package model

case class Plant(tpe: String, name: String, addr: String)

//Công nghệ xử lý không dùng trong tính toán
case class InfoData(f: Plant, power: Double)
