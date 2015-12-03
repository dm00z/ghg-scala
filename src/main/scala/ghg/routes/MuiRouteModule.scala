package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.extra.router.RouterConfigDsl

object MuiRouteModule {
  case object Info     extends LeftRoute("1. Thông tin chung", "info", InfoPage.apply)

  case object Indirect extends LeftRoute("2. Gián tiếp", "indirect", IndirectPage.apply)
  case object Electric    extends LeftRoute("a. Điện", "electric", ElectricPage.apply, 1)
  case object Gas         extends LeftRoute("b. Nhiên liêu", "gas", GasPage.apply, 1)
  case object Material    extends LeftRoute("c. Nguyên Liệu", "material", MaterialPage.apply, 1)

  case object Direct   extends LeftRoute("3. Trực tiếp", "direct", DirectPage.apply)
  case object KineticRelation     extends LeftRoute("a. Quan hệ động học", "kinetic-relation", KineticRelationPage.apply, 1)
  case object KineticCoefficient  extends LeftRoute("b. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply, 1)
  case object DirectData          extends LeftRoute("c. Dữ liệu", "direct-data", DirectDataPage.apply, 1)
  case object Aerobic             extends LeftRoute("d. Hệ hiếu khí", "aerobic", AerobicPage.apply, 1)

  val menu: List[LeftRoute] = List(
    Info,
    Indirect, Electric, Gas, Material,
    Direct, KineticRelation, KineticRelation, KineticCoefficient, DirectData, Aerobic
  )

  val routes = RouterConfigDsl[LeftRoute].buildRule { dsl =>
    import dsl._
    menu.map(i =>
      staticRoute(i.route, i) ~> renderR(r => MuiPage(i, r))
    ).reduce(_ | _)
  }
}
