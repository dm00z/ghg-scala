package ghg.components

import ghg.Utils._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.GhgData
import tex.TeX._

object Bien2UnstableAerobic {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b1 = d.bien1
      <.div(
        <.h3("2."),
        <.h4("2.1. Tính nồng độ cơ chất và nồng độ sinh khối trong điều kiện động học"),
        <.div("Công thức tính: `N/A`".teX),
        dataTbl(
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
          tr("X", "o,v", b1.s_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("Pr", "bl,SS", b1.pr_blSs, "%") //Phần trăm khư SS trong bể lắng sơ cấp
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Bien1")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
