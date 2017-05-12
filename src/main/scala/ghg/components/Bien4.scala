package ghg.components

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import tex.TeX._
import ghg.Utils._

object Bien4 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b1 = d.bien1
      val b4 = d.bien4
      <.div(
        <.h3("4. Lượng KNK từ phân hủy và chôn lấp bùn sinh học"),
        <.h4(f"4.1. Lượng bùn phân hủy: `0.6 *P_(X,BOD) = ${b4.bun_phanHuy}%,.2f` g/ng".teX),
        <.h4(f"4.2. Lượng phát thải CO2: ${b4.co2_phatThai}%,.2f g/ngày"),
        <.h4(f"4.3. Lượng phát thải CH4: ${b4.ch4_phatThai}%,.2f g/ngày"),
        <.h5("4.3.1 Lượng khí CH4 rò rỉ"),
        <.div("Công thức tính: `CH_(4,phânhuỷròrỉ) = Pr_(CH_(4,rori)) * CH_(4,phânhuỷthuhồi)`".teX),
        dataTbl(
          tr("CH","4phanhuyrori", b4.ch4_phanHuyRoRi, "g/ngày"), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
          tr("Pr", "CH4,rori",   b4.Pr_ch4_roRi , ""), //Nồng độ `BOD_5` dòng vào ban đầu
          tr("CH","4,phânhuỷthugom", b4.ch4_phanHuyThuGom, "g/ngày")
        ),
        <.h5(s"4.3.2. Lượng khí CH4 thu hồi: ${b4.ch4_phanHuyThuHoi}"),
        <.h4("4.4. Lượng khí CO2 tương đương của khí CH4"),
        <.div("Công thức tính: `CO_(2,phânhuỷmêtan) = Y_(CH_(4,đốt)) * CH_(4,phânhuỷthuhồi) * 23 * CH_(4,phânhuỷròrỉ)`".teX),
        dataTbl(
          tr("CO","2,phânhuỷmêtan", b4.co2_phanHuyMetan, "g/ngày"),
          tr("Y","CH4,dot", b4.y_ch4dot, "g/ngày"),
          tr("CH","4,phânhuỷthuhồi", b4.ch4_phanHuyThuHoi, ""),
          tr("CH","4,phânhuỷròrỉ", b4.ch4_phanHuyRoRi, "g/ngày")
        ),
        <.h4("4.5. Lượng CO2 từ bể phân hủy bùn"),
        <.div(s"`P_(CO_2,bunphanhuy) = ${b4.Pco2_bunPhanHuy}` g/ng".teX)
      )
    }
  }

  val component = ReactComponentB[Props]("Bien4")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
