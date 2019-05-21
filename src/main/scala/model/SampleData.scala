package model

import DirectTable.{PoolData, PrimaryPoolData, StreamInData, StreamOutData}
import model.GasData.{GWP, GasRatio}
import ElectricData.{CalcMethod, CountryPowerStruct, D1, D2, D3, PowerSupply}

object SampleData {
  val sampleElectricD2 = D2(List(
    D2.Row("01/01/15", "31/01/15", 176800),
    D2.Row("01/02/15", "28/02/15", 152600),
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
    D3.Row("Bơm nước thải bể điều hòa", 3.7, 2, 12),
    D3.Row("Bơm nước thải bể thoát nước", 3.7, 2, 12),
    D3.Row("Máy thổi khí trên cạn bể hiếu khí", 15, 1, 24),
    D3.Row("Máy thổi khí bể điều hòa", 4, 1, 24),
    D3.Row("Bơm bùn", 1.5, 1, 12),
    D3.Row("Máy khuấy chìm", 0.75, 1, 24),
    D3.Row("Bơm định lượng", 0.18, 1, 24),
    D3.Row("Động cơ khuấy trộn hóa chất", 0.4, 1, 24),
    D3.Row("Quạt hút mùi", 0.75, 1, 24)
  ))

  val sampleElectricD3BaiBang = D3(List(
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
    D3.Row("Động cơ gạt bọt", 0.4, 2, 24),
    D3.Row("Bơm bùn bể bùn sau DAF", 2.2, 2, 12),
    D3.Row("Bơm tuần hoàn UASB", 11, 1, 2),
    D3.Row("Bơm bùn máy ép bùn", 2.2, 1, 24),
    D3.Row("Máy ép bùn", 3, 1, 24)
  ))

  val sampleGas = List(
    GasData.PowerRow("01/01/15", "31/01/15", 25667.109),
    GasData.PowerRow("01/02/15", "29/02/15", 26353.334),
    GasData.PowerRow("01/03/15", "31/03/15", 21165.693),
    GasData.PowerRow("01/04/15", "30/04/15", 10372.461),
    GasData.PowerRow("01/05/15", "31/05/15", 1869.470),
    GasData.PowerRow("01/06/15", "30/06/15", 534.604),
    GasData.PowerRow("01/07/15", "31/07/15", 24890.514),
    GasData.PowerRow("01/08/15", "31/08/15", 7929.723),
    GasData.PowerRow("01/09/15", "30/09/15", 13300.922),
    GasData.PowerRow("01/10/15", "31/10/15", 25339.529),
    GasData.PowerRow("01/11/15", "30/11/15", 13300.922),
    GasData.PowerRow("01/12/15", "31/12/15", 25339.529)
  )

//  val gasRatioTable = GasRatioTable("MS: 9/070514. Nguồn: Picard, 1999", Seq(
//    GasGroup("Sản xuất khí tự nhiên", Seq(GasRow(1.9, 2.1, .000022))),
//    GasGroup("Chế biến khí tự nhiên", Seq(GasRow(.0021, .072, 0))),
//    GasGroup("Vận chuyển khí tự nhiên", Seq(
//      GasRow(7.2, 110, 0, "Cô đặc"),
//      GasRow(430, 0, 0, "Hóa lỏng")
//    )),
//    GasGroup("Sản xuất dầu nhiên liệu", Seq(GasRow(68000, 1800, .64))),
//    GasGroup("Vận chuyển dầu nhiên liệu", Seq(
//      GasRow(.49, 5.4, 0, "Đường ống"),
//      GasRow(2.3, 25, 0, "Xe chở dầu")
//    ))))

//  val gwp = GWP("Nguồn: IPCC, 2006", 1, Seq(
//    20 ->  GWPRow(1, 72, 275),
//    100 -> GWPRow(1, 25, 296),
//    500 -> GWPRow(1, 7.6, 165)
//  ))

  //Nước thải Sinh hoạt - Hiếu khí Yên Sở
  val dataGoldmark = GhgData(
      InfoData(
        Plant(
          WaterType.Domestic,
          "Hệ thống xử lý nước thải khu chung cư Goldmark City",
          "Hà Nội"),
        850, TechMethod.Ae
      ),
      IndirectData(
        ElectricData(
          powerStruct = CountryPowerStruct(
            "Việt Nam", "Báo cáo thường niên của EVN năm 2013",
            Seq(
              PowerSupply("Thủy điện", 16, R(16, 410), 38, "Rasha and Hammad, 2000"),
              PowerSupply("Hạt nhân", 9, R(9, 30), 0, "Andesta et al., 1998"),
              PowerSupply("Than", 860, R(860, 1290), 33.50, "IPCC,2001"),
              PowerSupply("Khí tự nhiên", 460, R(460, 1234), 20.7, ""),
              PowerSupply("Sinh học, gió, thủy triều", 11, R(11, 279), 0, ""),
              PowerSupply("Nhiên liệu khác", 690, R(689, 890), 7.8, "IPCC,2001")
            )),
          CalcMethod.Method3,
          D1(), sampleElectricD2, sampleElectricD3
        ),
        GasData(sampleGas,
          GasRatio("Nguồn: Picard, 1999", 431.9, 2.1, 0.000022),
          GWP("Nguồn: IPCC, 2006", 1, 25, 296)
        ),
        MaterialData()
      ),
      DirectData(
        DirectTable(
          StreamInData(250, 24.74, 186.33, 200, 250, 90, 40, 700),
          PrimaryPoolData(0, 0, 0, 850, 27, 200),
          PoolData(22, 7, 560f/850f, 560),
          None,
          Some(PoolData(22, 7, 560f/850f, 560)),
          StreamOutData(10, 23.584, 10, 10, 30, 25.5)
        ),
        KineticCoefficientData(
          KineticCoefficientData.Aerobic.goldmark,
          KineticCoefficientData.Nitrate.default,
          KineticCoefficientData.Anaerobic.default
        ),
        KineticRelationData.dataDomestic
      )
    )


  val dataBaiBang = GhgData(
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
              PowerSupply("Thủy điện", 16, R(16, 410), 38, "Rasha and Hammad, 2000"),
              PowerSupply("Hạt nhân", 9, R(9, 30), 0, "Andesta et al., 1998"),
              PowerSupply("Than", 860, R(860, 1290), 33.5, "IPCC,2001"),
              PowerSupply("Khí tự nhiên", 460, R(460, 1234), 20.7, ""),
              PowerSupply("Sinh học, gió, thủy triều", 11, R(11, 279), 0, ""),
              PowerSupply("Nhiên liệu khác", 690, R(689, 890), 7.8, "IPCC,2001")
            )),
          CalcMethod.Method1,
          D1(), sampleElectricD2, sampleElectricD3BaiBang
        ),
        GasData(sampleGas,
          GasRatio("Nguồn: Picard, 1999", 431.9, 2.1, 0.000022),
          GWP("Nguồn: IPCC, 2006", 1, 25, 296)
        ),
        MaterialData()
      ),
      DirectData(
        DirectTable(
          StreamInData(650, 24.74, 186.33, 400, 455, 90, 0, 700),
          PrimaryPoolData(.3, .65, 0, 8000, 27, 140),
          PoolData(30, 35, 2633f/8000f, 2633),
          Some(PoolData(30, 35, 2633f/8000f, 2633)),
          Some(PoolData(30, 35, 2633f/8000f, 2633)),
          StreamOutData(30, 121.58, 0, 10, 117.65, 25.5)
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
    "goldmark" -> dataGoldmark,
    "bai_bang" -> dataBaiBang
  )
  var selected = "goldmark"
  @inline def data = all(selected)
}
