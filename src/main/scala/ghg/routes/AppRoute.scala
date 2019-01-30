package ghg.routes

import diode.react.ModelProxy
import ghg.components.KRData
import ghg.pages._
import japgolly.scalajs.react.ReactElement
import model.{GhgData, InfoData}

abstract class AppRoute(val name: String, val route: String, val render: ModelProxy[GhgData] => ReactElement,
                        val group: String, val subGroup: String = null)

object AppRoutes {
  object Info extends AppRoute("1. Thông tin chung", "info", InfoPage.apply, "Thông tin chung")

  //object Indirect extends AppRoute("2. Gián tiếp", "indirect", IndirectPage.apply, "Tính toán phát thải khí nhà kính gián tiếp")
  object Electric extends AppRoute("2. Phát thải KNK từ tiêu thụ điện năng phục vụ HTXLNT", "electric", ElectricPage.apply,
  //  "Tính toán phát thải khí nhà kính gián tiếp",
    "Phát thải khí nhà kính từ tiêu thụ điện năng")
  //object Gas      extends AppRoute("b. Nhiên liêu", "gas", GasPage.apply,
  //  "Tính toán phát thải khí nhà kính gián tiếp",
  //  "Phát thải KNK từ sản xuất và vận chuyển khí tự nhiên")
//  object Material extends AppRoute("c. Nguyên L", "material", MaterialPage.apply,
//    "Tính toán phát thải khí nhà kính gián tiếp",
//    "Phát thải KNK từ sử dụng hóa chất")

  object Direct   extends AppRoute("3. Phát thải KNK từ quá trình xử lý", "direct", DirectPage.apply, "Phát thải khí nhà kính từ quá trình xử lý")
  object KineticRelation     extends AppRoute("3.1. Hệ số năng suất Y", "kinetic-relation", KRData.apply,
    "Phát thải khí nhà kính từ quá trình xử lý",
    "Hệ số năng suất Y")
  object KineticCoefficient  extends AppRoute("3.2. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply,
    "Phát thải khí nhà kính từ quá trình xử lý",
    "Hệ số động học của các quá trình")
  object DirectData          extends AppRoute("3.3. Thông số hệ thống xử lý", "direct-data", DirectDataPage.apply,
    "Phát thải khí nhà kính từ quá trình xử lý",
    "Thông số dòng vào")


  object Aerobic             extends AppRoute("3.4. Tính toán phát thải KNK từ quá trình xử lý", "aerobic", AerobicPage.apply,
    "Phát thải khí nhà kính từ quá trình xử lý  ",
    "Tính toán phát thải khí nhà kính")
  object Anaerobic           extends AppRoute("3.4. Tính toán phát thải KNK từ quá trình xử lý ", "anaerobic", AnaerobicPage.apply,
    "Phát thải khí nhà kính từ quá trình xử lý  ",
    "Tính toán phát thải khí nhà kính")
//  object AerobicUnstable     extends AppRoute("e. Hệ hiếu khí không ổn định", "aerobic-unstable", AerobicPage.apply,
//    "Tính toán phát thải khí nhà kính trực tiếp",
//    "Tính toán hệ hiếu khí không ổn định")
  object Overview            extends AppRoute("4. Tổng hợp kết quả", "overal", OverviewPage.apply, "Tổng hợp kết quả")
  object Retrieve            extends AppRoute("4.1. Trường hợp thu hồi và đốt CH4", "retrieve", RetrievePage.apply, "Tổng hợp kết quả", "Thu hồi và đốt CH4")
  object Release             extends AppRoute("4.2. Trường hợp phóng không CH4", "release", ReleasePage.apply, "Tổng hợp kết quả", "Phóng không CH4")
  object Graph               extends AppRoute("5. Biểu đồ các yếu tố ảnh hưởng", "graph", GraphPage.apply, "Biểu đồ các yếu tố ảnh hưởng")
  object TemperatureGraph    extends AppRoute("5.1 Biến thiên theo nhiệt độ", "tempgraph", TemperatureGraphPage.apply, "Biểu đồ các yếu tố ảnh hưởng", "Biểu đồ nhiệt độ")
  object SubstanceGraph      extends AppRoute("5.2 Biến thiên theo nồng độ cơ chất dòng vào", "subgraph", SubstanceGraphPage.apply, "Biểu đồ các yếu tố ảnh hưởng", "Biểu đồ cơ chất dòng vào")
  object SRTGraph            extends AppRoute("5.3 Biến thiên theo tuổi bùn", "srtgraph", SRTGraphPage.apply, "Biểu đồ các yếu tố ảnh hưởng", "Biểu đồ cơ chất dòng vào")

  val all: List[AppRoute] = List(
    Info,
    Electric,
    Direct, KineticRelation, KineticCoefficient, DirectData, Aerobic, Anaerobic,
    Overview, Retrieve, Release,
    Graph, TemperatureGraph, SubstanceGraph, SRTGraph
  )
}
