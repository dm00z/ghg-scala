package model

case class Factory(tpe: String, name: String, addr: String)

//Công nghệ xử lý không dùng trong tính toán
case class InfoData(f: Factory, power: Double)
