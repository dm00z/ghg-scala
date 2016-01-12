package model

import DirectTable.{PoolData, PrimaryPoolData, StreamInData}
import GasData.{GasRatioTable, GWP}, GWP.{Row => GWPRow}
import GasRatioTable.{Group => GasGroup, Row => GasRow}
import ElectricData.{CountryPowerStruct, CalcMethod, PowerSupply, D1, D2, D3}

object SampleData {
  val sampleElectricD2 = D2(List(
    D2.Row("01/01/15", "31/01/15", 176800),
    D2.Row("01/02/15", "29/02/15", 152600),
    D2.Row("01/03/15", "31/03/15", 166600),
    D2.Row("01/04/15", "30/04/15", 138800),
    D2.Row("01/05/15", "31/05/15", 154600),
    D2.Row("01/06/15", "30/06/15", 167200),
    D2.Row("01/07/15", "31/07/15", 172000),
    D2.Row("01/08/15", "31/08/15", 165000),
    D2.Row("01/09/15", "30/09/15", 170000),
    D2.Row("01/10/15", "31/10/15", 168000),
    D2.Row("01/11/15", "30/11/15", 158000),
    D2.Row("01/12/15", "31/12/15", 145000)
  ))

  val sampleElectricD3 = D3(List(
    D3.Row("Máy tách rác trống quay", 4, 1, 24),
    D3.Row("Bơm bể gom", 7.1, 4, 12),
    D3.Row("Bơm bể điều hoà", 11, 3, 12),
    D3.Row("Máy khuấy PAC", 0.75, 2, 6),
    D3.Row("Máy khuấy Polymer", 0.4, 4, 6),
    D3.Row("Bơm định lượng phèn nhôm/PAC", 0.4, 2, 24),
    D3.Row("Bơm định lượng Polymer", 0.4, 4, 24),
    D3.Row("Bơm tuần hoàn DAF", 18, 2, 24),
    D3.Row("Bơm bể trung gian", 15, 4, 12),
    D3.Row("Máy thổi khí DAF", 2.2, 2, 24),
    D3.Row("Động cơ gạt bọt", 0.4, 2, 24),
    D3.Row("Bơm bùn bể bùn sau daf", 2.2, 2, 12),
    D3.Row("Bơm tuần hoàn UASB", 11, 1, 2),
    D3.Row("Bơm bùn máy ép bùn", 2.2, 1, 24),
    D3.Row("Máy ép bùn", 3, 1, 24)
  ))

  //Nước thải Sinh hoạt - Hiếu khí Yên Sở
  val dataYenSo =
    GhgData(
      InfoData(
        Plant(
          WaterType.Domestic,
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
          CalcMethod.Method3,
          D1(), sampleElectricD2, sampleElectricD3
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
          None,
          Some(PoolData(22, 5, 5))
        ),
        KineticCoefficientData(
          KineticCoefficientData.Aerobic.yenSo,
          KineticCoefficientData.Nitrate.default,
          KineticCoefficientData.Anaerobic.default
        ),
        KineticRelationData.dataDomestic
      )
    )

  val dataBaiBang =
    GhgData(
      InfoData(
        Plant(
          WaterType.Industrial,
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
          D1(), sampleElectricD2, sampleElectricD3
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
          Some(PoolData(30, 20, 8, .1)),
          None
        ),
        KineticCoefficientData(
          KineticCoefficientData.Aerobic.default,
          KineticCoefficientData.Nitrate.default,
          KineticCoefficientData.Anaerobic.default
        ),
        KineticRelationData.dataIndustrial
      )
    )

  var all = Map(
    "yen_so" -> dataYenSo,
    "bai_bang" -> dataBaiBang
  )
  var selected = "yen_so"
  @inline def data = all(selected)
}
