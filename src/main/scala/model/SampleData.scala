package model

import DirectTable.{PoolData, PrimaryPoolData, StreamInData}
import GasData.{GasRatioTable, GWP}, GWP.{Row => GWPRow}
import GasRatioTable.{Group => GasGroup, Row => GasRow}
import ElectricData.{CountryPowerStruct, CalcMethod, PowerSupply, D1, D2, D3}

object SampleData {
  //Nước thải Sinh hoạt - Hiếu khí Yên Sở
  val dataYenSo = {
    val testAnaerobicPool = PoolData(40, 20, 15)
    val testAerobicPool = PoolData(22, 5, 5)

    GhgData(
      InfoData(
        Plant(
          "Nước thải sinh hoạt",
          "Nhà máy xử lý nước thải sinh hoạt Yên Sở",
          "Hà Nội"),
        120000, TechMethod.Ae
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
          StreamInData(45, 34, 51),
          PrimaryPoolData(.30, .40, 0),
          PoolData(30, 20, 5),
          Some(testAnaerobicPool),
          Some(testAerobicPool)
        ),
        KineticCoefficientData(
          KineticCoefficientData.Aerobic.yenSo,
          KineticCoefficientData.Nitrate.default,
          KineticCoefficientData.Anaerobic.default
        ),
        KineticRelationData(
          WaterType.Domestic,
          KineticRelationData.Data( //sinh hoạt
            0.33, 0.422, 0, 0.4,
            1.56, 0, 1.42,
            0.28, 0.235, 0.035, 0.108,
            0.58, 0.35, 0.39,
            0.55, 0.98, 4.32, 0.247, 0,
            0.175, 2.767, 3.952, 2.059, 1.9,
            0.28, 0.23, 0.042, 0.105,
            0.58, 0.35, 0.39,
            2.75),
          KineticRelationData.Data( //công nghiệp
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
  val dataBaiBang = {
    val testAnaerobicPool = PoolData(30, 20, 8, .1)
    val testAerobicPool = PoolData(25, 10, 8) //None

    GhgData(
      InfoData(
        Plant(
          "Nước thải công nghiệp",
          "Nhà máy xử lý nước thải cho công ty Giấy Bãi Bằng",
          "Thị trấn Phong Châu, huyện Phù Ninh, tỉnh Phú Thọ"),
        8000, TechMethod.An
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
          StreamInData(550, 30, 1100),
          PrimaryPoolData(.3, .65, 100),
          PoolData(30, 20, 5),
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

  var all = Map(
    "yen_so" -> dataYenSo,
    "bai_bang" -> dataBaiBang
  )
  var selected = "yen_so"
  @inline def data = all(selected)
}
