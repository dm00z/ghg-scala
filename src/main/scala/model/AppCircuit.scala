package model

import diode.{ActionHandler, Circuit}
import DirectTable.{PoolData, PrimaryPoolData, GenericData}
import GasData.{GasRatioTable, GWP}, GWP.{Row => GWPRow}
import GasRatioTable.{Group => GasGroup, Row => GasRow}
import ElectricData.{CountryPowerStruct, CalcMethod, PowerSupply, D1, D2, D3}
import diode.react.ReactConnector

object AppCircuit extends Circuit[GhgData] with ReactConnector[GhgData]{
  protected var model = testData()

  private val infoHandler = new ActionHandler(zoomRW(_.info)((d, v) => d.copy(info = v))) {
    def handle = {
      case x: Plant => updated(value.copy(f = x))
      case x: InfoData => updated(x)
    }
  }

  private val electricRw = zoomRW(_.indirect.electric)((d, v) => d.copy(indirect = d.indirect.copy(electric = v)))
  private val electricHandler = new ActionHandler(electricRw) {
    def handle = {
      case x: ElectricData.D1 => updated(value.copy(_1 = x))
      case x: ElectricData.D2 => updated(value.copy(_2 = x))
      case x: CalcMethod.Value => updated(value.copy(method = x))
    }
  }

  private val krRw = zoomRW(_.direct.relation)((d, v) => d.copy(direct = d.direct.copy(relation = v)))
  private val krHandler = new ActionHandler(krRw) {
    import KineticRelationData.Data
    def handle = {
      case (WaterType.Domestic, x: Data) => updated(value.copy(domestic = x))
      case (WaterType.Industrial, x: Data) => updated(value.copy(industrial = x))
    }
  }

  private val coefRw = zoomRW(_.direct.coef)((d, v) => d.copy(direct = d.direct.copy(coef = v)))
  private val coefHandler = new ActionHandler(coefRw) {
    import KineticCoefficientData._
    def handle = {
      case x: Aerobic => updated(value.copy(aerobic = x))
      case x: Nitrate => updated(value.copy(nitrate = x))
      case x: Anaerobic => updated(value.copy(anaerobic = x))
    }
  }
  private val directDataRw = zoomRW(_.direct.d)((d, v) => d.copy(direct = d.direct.copy(d = v)))
  private val directDataHandler = new ActionHandler(directDataRw) {
    import DirectTable._
    def handle = {
      case x: GenericData => updated(value.copy(generic = x))
      case x: PrimaryPoolData => updated(value.copy(primaryPool = x))
    }
  }

  protected val actionHandler = combineHandlers(infoHandler, electricHandler, krHandler, coefHandler, directDataHandler)

  private def testData() = {
    val testAnaerobicPool = PoolData(40, 20, 15)
    val testAerobicPool = PoolData(25, 10, 8)

    GhgData(
      InfoData(
        Plant(
          "Nước thải công nghiệp",
          "Nhà máy xử lý nước thải cho công ty Giấy Bãi Bằng",
          "Thị trấn Phong Châu, huyện Phù Ninh, tỉnh Phú Thọ"),
        8000
      ),
      IndirectData(
        ElectricData(
          powerStruct = CountryPowerStruct(
            "Việt Nam", "Báo cáo thường niên của EVN năm 2013",
            Seq(
              PowerSupply("Thủy điện", 10, R(16, 410), "Rasha and Hammad, 2000") -> 48.78,
              PowerSupply("Hạt nhân", 9, R(9, 30), "Andesta et al., 1998") -> 0,
              PowerSupply("Than", 877, R(860, 1290), "IPCC,2001") -> 23.07,
              PowerSupply("Khí tự nhiên", 640, R(460, 1234), "") -> 0,
              PowerSupply("Sinh học, gió, thủy triều", 11, R(11, 279), "") -> 0.43,
              PowerSupply("Nhiên liệu khác", 604, R(600, 890), "IPCC,2001") -> 27.72
            )),
          CalcMethod.Method1,
          D1(), D2(Nil), D3(Nil)
        ),
        GasData(Nil,
          GasRatioTable("MS: 9/070514. Nguồn: Picard, 1999", Seq(
            GasGroup("Sản xuất khí tự nhiên", Seq(GasRow(1.9, 2.1, .000022))),
            GasGroup("Chế biến khí tự nhiên", Seq(GasRow(.0021, .072, 0))),
            GasGroup("Vận chuyển khí tự nhiên", Seq(
              GasRow(7.2, 110, 0, "Cô đặc"),
              GasRow(430, 0, 0, "Hóa lỏng")
            )),
            GasGroup("Sản xuất dầu nhiên liệu", Seq(GasRow(68000, 1800, .64))),
            GasGroup("Vận chuyển dầu nhiên liệu", Seq(
              GasRow(.49, 5.4, 0, "Đường ống"),
              GasRow(2.3, 25, 0, "Xe chở dầu")
            )))),
          GWP("Nguồn: IPCC, 2006", 1, Seq(
            20 ->  GWPRow(1, 72, 275),
            100 -> GWPRow(1, 25, 296),
            500 -> GWPRow(1, 7.6, 165)
          ))
        ),
        MaterialData()
      ),
      DirectData(
        DirectTable(
          GenericData(350, 30, 1000),
          PrimaryPoolData(.25, .40, 150),
          PoolData(30, 20, 20),
          Some(testAnaerobicPool),
          Some(testAerobicPool)
        ),
        KineticCoefficientData(
          KineticCoefficientData.Aerobic.default,
          KineticCoefficientData.Nitrate.default,
          KineticCoefficientData.Anaerobic.default
        ),
        KineticRelationData(
          WaterType.Industrial,
          KineticRelationData.Data( //sinh hoạt - chưa dùng
            0.33, 0.422, 0, 0.4,
            1.56, 0, 1.42,
            0.28, 0.235, 0.035, 0.108,
            0.58, 0.35, 0.39,
            0.55, 0.98, 4.32, 0.247, 0,
            0.175, 2.767, 3.952, 2.059, 1.9,
            0.28, 0.23, 0.042, 0.105,
            0.58, 0.35, 0.39,
            2.75),
          KineticRelationData.Data( //công nghiệp -
            0.49, 0.43, 0.03, 0.41,
            1.56, 0.44,	1.42,
            0.428, 0.236, 0.058, 0.192,
            0.58, 0.35, 0.39,
            0.31, 0.96, 3.96, 0.48, 7,
            0.175, 3.228, 4.231, 0.250, 1.9,
            0.45, 0.24, 0.04, 0.2,
            0.58, 0.35, 0.39,
            2.75)
        )
      )
    )
  }
}
