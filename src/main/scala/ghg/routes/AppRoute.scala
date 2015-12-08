package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.ReactElement

abstract class AppRoute(val name: String, val route: String, val render: () => ReactElement, val indentLevel: Int = 0)

object AppRoutes {
  object Info extends AppRoute("1. Thông tin chung", "info", InfoPage.apply)

  object Indirect extends AppRoute("2. Gián tiếp", "indirect", IndirectPage.apply)
  object Electric extends AppRoute("a. Điện", "electric", ElectricPage.apply, 1)
  object Gas      extends AppRoute("b. Nhiên liêu", "gas", GasPage.apply, 1)
  object Material extends AppRoute("c. Nguyên Liệu", "material", MaterialPage.apply, 1)

  object Direct   extends AppRoute("3. Trực tiếp", "direct", DirectPage.apply)
  object KineticRelation     extends AppRoute("a. Quan hệ động học", "kinetic-relation", KineticRelationPage.apply, 1)
  object KineticCoefficient  extends AppRoute("b. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply, 1)
  object DirectData          extends AppRoute("c. Dữ liệu", "direct-data", DirectDataPage.apply, 1)
  object Aerobic             extends AppRoute("d. Hệ hiếu khí", "aerobic", AerobicPage.apply, 1)
  object Anaerobic           extends AppRoute("e. Hệ yếm khí", "anaerobic", AnaerobicPage.apply, 1)

  val all: List[AppRoute] = List(
    Info,
    Indirect, Electric, Gas, Material,
    Direct, KineticRelation, KineticCoefficient, DirectData, Aerobic, Anaerobic
  )
}
