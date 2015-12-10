package model

import model.ElectricData.CountryPowerStruct
import org.widok.moment.Date

object ElectricData {
  object CalcMethod extends Enumeration {
    type CalcMethod = Value

    val Method1 = Value("Từ hệ số mặc đinh")
    val Method2 = Value("Từ dữ liệu tiêu thụ thực tế")
    val Method3 = Value("Từ các thiết bị trong hệ thống")
  }

  object D1 {
    /** unit: kwh/m3 */
    val Ratio = RNorm(R(.1, .45), .12)
  }
  case class D1(ratio: Double = D1.Ratio.norm)

  object D2 {
    case class Row(from: Date, to: Date, kwh: Double) {
      def days = to.diff(from, "days", asFloat = true)
      def average = kwh / days
    }
  }
  /** dates.length must == kwh.length + 1 */
  case class D2(rows: Seq[D2.Row]) {
    def power = rows.foldLeft((0D, 0D)) {
      case ((kwh, days), r) => (kwh + r.kwh, days + r.days)
    } match {
      case (kwh, days) => kwh / days
    }
  }

  object D3 {
    case class Row(device: String, kw: Double, quantity: Int, workHoursPerDay: Int) {
      def operateMode = if (workHoursPerDay < 24) "Gián đoạn" else "Liên tục"
      def kwhPerDay = kw * quantity * workHoursPerDay
    }
    val Ratio = RNorm(R(0, 1), .5)
  }
  case class D3(rows: Seq[D3.Row], ratio: Double = D3.Ratio.norm) {
    def powerCalc = rows.map(_.kwhPerDay).sum
    def power = ratio * powerCalc
  }

  case class PowerSupply(tpe: String, EFi: Int, EFiRange: R[Int], ref: String)
  /** supplies(x) = PFi of x (a Supply) */
  case class CountryPowerStruct(country: String, ref: String, supplies: Seq[(PowerSupply, Double)]) {
    /** = sum(EFi * PFi) */
    def totalRatio = supplies.map { case (s, pfi) => s.EFi * pfi }.sum
  }
}

case class ElectricData(powerStruct: CountryPowerStruct,
                        method: ElectricData.CalcMethod.Value,
                        _1: ElectricData.D1,
                        _2: ElectricData.D2,
                        _3: ElectricData.D3)
