package ghg.routes

import ghg.pages._
import japgolly.scalajs.react.extra.router.RouterConfigDsl

object MuiRouteModule {
  case object Info extends LeftRoute("1. Thông tin chung", "info", InfoPage.apply,
                                     "Thông tin chung")

  case object Indirect extends LeftRoute("2. Gián tiếp", "indirect", IndirectPage.apply,
                                         "Tính toán phát thải khí nhà kính gián tiếp")
  case object Electric extends LeftRoute("a. Điện", "electric", ElectricPage.apply,
                                         "Tính toán phát thải khí nhà kính gián tiếp",
                                         Some("Phát thải KNK từ tiêu thụ điện năng"))
  case object Gas      extends LeftRoute("b. Nhiên liêu", "gas", GasPage.apply,
                                         "Tính toán phát thải khí nhà kính gián tiếp",
                                         Some("Phát thải KNK từ sản xuất và vận chuyển khí tự nhiên"))
  case object Material extends LeftRoute("c. Nguyên Liệu", "material", MaterialPage.apply,
                                         "Tính toán phát thải khí nhà kính gián tiếp",
                                         Some("Phát thải KNK từ sử dụng hóa chất"))

  case object Direct   extends LeftRoute("3. Trực tiếp", "direct", DirectPage.apply,
                                         "Tính toán phát thải khí nhà kính trực tiếp")
  case object KineticRelation     extends LeftRoute("a. Quan hệ động học", "kinetic-relation", KineticRelationPage.apply,
                                         "Tính toán phát thải khí nhà kính trực tiếp",
                                         Some("Thông số quan hệ động học của các quá trình"))
  case object KineticCoefficient  extends LeftRoute("b. Hệ số động học", "kinetic-coefficient", KineticCoefficientPage.apply,
                                         "Tính toán phát thải khí nhà kính trực tiếp",
                                         Some("Hệ số động học của các quá trình"))
  case object DirectData          extends LeftRoute("c. Dữ liệu", "direct-data", DirectDataPage.apply,
                                         "Tính toán phát thải khí nhà kính trực tiếp",
                                         Some("Thông số dòng vào"))
  case object Aerobic             extends LeftRoute("d. Hệ hiếu khí", "aerobic", AerobicPage.apply,
                                         "Tính toán phát thải khí nhà kính trực tiếp",
                                         Some("Tính toán hệ hiếu khí"))

  val menu: List[LeftRoute] = List(
    Info,
    Indirect, Electric, Gas, Material,
    Direct, KineticRelation, KineticCoefficient, DirectData, Aerobic
  )

  val routes = RouterConfigDsl[LeftRoute].buildRule { dsl =>
    import dsl._
    menu.map(i =>
      staticRoute(i.route, i) ~> renderR(r => MuiPage(i, r))
    ).reduce(_ | _)
  }
}
