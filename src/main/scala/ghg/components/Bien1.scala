package ghg.components

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import tex.TeX._
import ghg.Utils._

object Bien1 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b1 = d.bien1
      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp"),
        <.h4("1.1. Lượng SS bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `SS_(khu,bl) = Pr_(bl,SS) * Q_(o,v) * S_(o,v)`".teX),
        dataTbl(
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
          tr("X", "o,v", b1.x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("Pr", "bl,SS", b1.pr_blSs, "%") //Phần trăm khư SS trong bể lắng sơ cấp
        ),
        <.h4("1.2. Lượng BOD bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `BOD_(khu,bl) = Pr_(bl,BOD) * Q_(o,v) * S_(o,v)`".teX),
        dataTbl(
          tr("BOD", "khu,bl", b1.bod_khuBl, "g/day"), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
          tr("S", "o,v", b1.s_ov, "mg/l"), //Nồng độ `BOD_5` dòng vào ban đầu
          tr("Pr", "bl,BOD", b1.pr_blBod) //Phần trăm khư `BOD_5` trong bể lắng sơ cấp
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Bien1")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
