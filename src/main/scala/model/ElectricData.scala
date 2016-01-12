package model

import model.ElectricData.CountryPowerStruct
import monocle.macros.Lenses
import org.widok.moment.{Moment, Date}

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
    @Lenses case class Row(from: Date, to: Date, kwh: Double) {
      def days = to.diff(from, "days", asFloat = true) + 1
      def average = kwh / days
    }
    object Row {
      def apply(from: String, to: String, kwh: Double): Row =
        Row(Moment(from, "DD/MM/YYYY"), Moment(to, "DD/MM/YY"), kwh)
    }
  }
  /** dates.length must == kwh.length + 1 */
  case class D2(rows: List[D2.Row]) {
    def power = rows.foldLeft((0D, 0D)) {
      case ((kwh, days), r) => (kwh + r.kwh, days + r.days)
    } match {
      case (kwh, days) => kwh / days
    }
  }

  object D3 {
    @Lenses case class Row(device: String, kw: Double, quantity: Int, workHoursPerDay: Double) {
      def operateMode = if (workHoursPerDay < 24) "Gián đoạn" else "Liên tục"
      def kwhPerDay = kw * quantity * workHoursPerDay
    }
    val RatioOther = RNorm(R(.5, 1), .5)
    val EtieuThu = RNorm(R(0, 1), .85)
  }
  @Lenses case class D3(rows: List[D3.Row], ratioOther: Double = D3.RatioOther.norm, etieuThu: Double = D3.EtieuThu.norm) {
    lazy val powerCalc = rows.map(_.kwhPerDay).sum
    def powerOther = powerCalc * ratioOther / 100
    def powerTheory = powerOther + powerCalc
    def power = powerTheory * etieuThu
  }

  @Lenses case class PowerSupply(tpe: String, EFi: Int, EFiRange: R[Int], PFi: Double, ref: String) {
    val mul = EFi * PFi / 100
  }
  /** supplies(x) = PFi of x (a Supply) */
  @Lenses case class CountryPowerStruct(country: String, ref: String, supplies: Seq[PowerSupply]) {
    /** = sum(EFi * PFi) */
    val totalRatio = supplies.map(_.mul).sum
  }
}

case class ElectricData(powerStruct: CountryPowerStruct,
                        method: ElectricData.CalcMethod.Value,
                        _1: ElectricData.D1,
                        _2: ElectricData.D2,
                        _3: ElectricData.D3)
