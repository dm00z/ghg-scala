package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.extra.router.RouterConfigDsl

object MuiRouteModule {
  case object Info     extends LeftRoute("1. Thông tin chung", "info", InfoPage.apply)

  case object Indirect extends LeftRoute("2. Gián tiếp", "indirect", IndirectPage.apply)
  case object Electric    extends LeftRoute("2.1. Điện", "electric", ElectricPage.apply)
  case object Gas         extends LeftRoute("2.2. Nhiên liêu", "gas", GasPage.apply)
  case object Material    extends LeftRoute("2.3. Nguyên Liệu", "material", MaterialPage.apply)

  case object Direct   extends LeftRoute("3. Trực tiếp", "direct", DirectPage.apply)
  case object KineticRelation     extends LeftRoute("3.1. Quan hệ động học", "kinetic-relation", KineticRelationPage.apply)
  case object KineticCoefficient  extends LeftRoute("3.2. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply)
  case object DirectData          extends LeftRoute("3.3. Dữ liệu", "direct-data", DirectDataPage.apply)
  case object Aerobic             extends LeftRoute("3.4. Hệ hiếu khí", "aerobic", AerobicPage.apply)

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
