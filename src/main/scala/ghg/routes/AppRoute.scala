package ghg.routes

import diode.react.ModelProxy
import ghg.components.KRData
import ghg.pages._
import japgolly.scalajs.react.ReactElement
import model.GhgData

abstract class AppRoute(val name: String, val route: String, val render: (ModelProxy[GhgData]) => ReactElement,
                        val group: String, val subGroup: String = null)

object AppRoutes {
  object Info extends AppRoute("1. Thông tin chung", "info", InfoPage.apply, "Thông tin chung")

  object Indirect extends AppRoute("2. Gián tiếp", "indirect", IndirectPage.apply, "Tính toán phát thải khí nhà kính gián tiếp")
  object Electric extends AppRoute("a. Điện", "electric", ElectricPage.apply,
    "Tính toán phát thải khí nhà kính gián tiếp",
    "Phát thải KNK từ tiêu thụ điện năng")
  //object Gas      extends AppRoute("b. Nhiên liêu", "gas", GasPage.apply,
  //  "Tính toán phát thải khí nhà kính gián tiếp",
  //  "Phát thải KNK từ sản xuất và vận chuyển khí tự nhiên")
  //object Material extends AppRoute("c. Nguyên L", "material", MaterialPage.apply,
  //  "Tính toán phát thải khí nhà kính gián tiếp",
  //  "Phát thải KNK từ sử dụng hóa chất")

  object Direct   extends AppRoute("3. Trực tiếp", "direct", DirectPage.apply, "Tính toán phát thải khí nhà kính trực tiếp")
  object KineticRelation     extends AppRoute("a. Quan hệ động học", "kinetic-relation", KRData.apply,
    "Tính toán phát thải khí nhà kính trực tiếp",
    "Thông số quan hệ động học của các quá trình")
  object KineticCoefficient  extends AppRoute("b. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply,
    "Tính toán phát thải khí nhà kính trực tiếp",
    "Hệ số động học của các quá trình")
  object DirectData          extends AppRoute("c. Dữ liệu", "direct-data", DirectDataPage.apply,
    "Tính toán phát thải khí nhà kính trực tiếp",
    "Thông số dòng vào")
  object Aerobic             extends AppRoute("d. Hệ hiếu khí ổn định", "aerobic", AerobicPage.apply,
    "Tính toán phát thải khí nhà kính trực tiếp",
    "Tính toán hệ hiếu khí")

  val all: List[AppRoute] = List(
    Info,
    Indirect, Electric,
    Direct, KineticRelation, KineticCoefficient, DirectData, Aerobic
  )
}
