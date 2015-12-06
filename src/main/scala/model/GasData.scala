package model

import org.widok.moment.Date

object GasData {
  case class PowerRow(from: Date, to: Date, m3: Double) {
    def days = to.diff(from, "days", asFloat = true)
    def average = m3 / days
  }

  object GasRatioTable {
    case class Row(co2: Double, ch4: Double, n2o: Double,
                   tpe: String = "",
                   active: Boolean = false)
    case class Group(step: String, rows: Seq[Row])

    val instance = GasRatioTable("MS: 9/070514. Nguồn: Picard, 1999", Seq(
      Group("Sản xuất khí tự nhiên", Seq(Row(1.9, 2.1, .000022))),
      Group("Chế biến khí tự nhiên", Seq(Row(.0021, .072, 0))),
      Group("Vận chuyển khí tự nhiên", Seq(
        Row(7.2, 110, 0, "Cô đặc"),
        Row(430, 0, 0, "Hóa lỏng")
      )),
      Group("Sản xuất dầu nhiên liệu", Seq(Row(68000, 1800, .64))),
      Group("Vận chuyển dầu nhiên liệu", Seq(
        Row(.49, 5.4, 0, "Đường ống"),
        Row(2.3, 25, 0, "Xe chở dầu")
      ))))
  }
  case class GasRatioTable private(ref: String, rows: Seq[GasRatioTable.Group]) {
    def efCO2 = rows.flatMap(_.rows).filter(_.active).map(_.co2).sum
    def efCH4 = rows.flatMap(_.rows).filter(_.active).map(_.ch4).sum
    def efN2O = rows.flatMap(_.rows).filter(_.active).map(_.n2o).sum
  }

  object GWP {
    case class Row(co2: Double, ch4: Double, n2o: Double)
    val instance = GWP("Nguồn: IPCC, 2006", 1, Seq(
      20 -> Row(1, 72, 275),
      100 -> Row(1, 25, 296),
      500 -> Row(1, 7.6, 165)
    ))
  }
  /** @param rows Seq[số năm -> Row] */
  case class GWP(ref: String, selectedIdx: Int, rows: Seq[(Int, GWP.Row)]) {
    def value = rows(selectedIdx)._2
  }
}

case class GasData(powers: Seq[GasData.PowerRow] = Nil,
                   gas: GasData.GasRatioTable = GasData.GasRatioTable.instance,
                   gwp: GasData.GWP = GasData.GWP.instance) {
  def power = powers.foldLeft((0D, 0D)) {
    case ((m3, days), r) => (m3 + r.m3, days + r.days)
  } match {
    case (m3, days) => m3 / days
  }
}