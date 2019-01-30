package ghg.components

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import tex.TeX._
import ghg.Utils._
import scala.scalajs.js.JSNumberOps._

object Bien4 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b1 = d.bien1
      val b4 = d.bien4
      val b2 = d.bien2Ae
      val relation = d.direct.relation

      <.div(^.className := "calcTable",
        <.h3("4. Lượng KNK từ phân hủy yếm khí"),
        <.h4("4.1. Lượng bùn phân hủy sinh học"),
        <.div("Công thức tính: `P_(bunsinhhoc) = 0.6 * P_(X, bio)`".teX),
        dataTbl(
          tr("P","bunsinhhoc", b4.P_bunSinhHoc, "g/ngày"), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
          //tr("Pr", "CH4,rori",   b4.Pr_ch4_roRi , ""), //Nồng độ `BOD_5` dòng vào ban đầu
          tr("P","bio", b2.p_XBio, "g/ngày")
        ),
        <.h4(f"4.2. Lượng phát thải CO2 từ bể phân hủy yếm khí"),
        <.div("Công thức tính: `M_(C0_2,bephanhuy) = (Y^(dr))_(CO_2,phanhuy) * P_(bunsinhhoc)`".teX),
        dataTbl(
          tr("M","CO2,bephanhuy", b4.M_co2PhanHuy, "g/ngày"),
          tr("Y^(dr)","CO2,phanhuy", relation.yCO2DrDecay, "g/ngày"),
          tr("P", "bunsinhhoc",   b4.P_bunSinhHoc , "")
        ),
        <.h4(f"4.3. Lượng phát thải CH4 từ bể phân huỷ yếm khí"),
        <.div("Công thức tính: `M_(CH_4,bephanhuy) = (Y^(dr))_(CH_4,phanhuy) * P_(bunsinhhoc)`".teX),
        dataTbl(
          tr("M","CH4,bephanhuy", b4.M_ch4PhanHuy, "g/ngày"),
          tr("Y^(dr)","CH4,phanhuy", relation.yCH4DrDecay, "g/ngày"),
          tr("P", "bunsinhhoc",   b4.P_bunSinhHoc, "")
        ),

        <.h4("4.4.a Lượng KNK từ bể phân huỷ yếm khí trong trường hợp phóng không"),
        <.div("Công thức tính: `M_(CO2-td,bephânhuỷphongkhong) = M_(CO2,bephanhuy) + 25 * M_(CH4,bephânhuỷ)`".teX),
        dataTbl(
          tr("M","CO2,bephânhuỷphongkhong", b4.M_co2tdPhanHuyPhongKhong, "g/ngày"),
          tr("M","CO2,bephanhuy", b4.M_co2PhanHuy, "g/ngày"),
          tr("M","CH4,bephanhuy", b4.M_ch4PhanHuy, "g/ngay")
        ),

        <.h4("4.4.b Lượng KNK từ bể phân huỷ yếm khí trong trường hợp thu hồi và đốt"),
        <.h4("4.4.b1 Lượng khí CH4 phân huỷ rò rỉ"),
        <.div("Công thức tính: `M_(CH2,phânhuỷròrỉ) = Pr_(CH4,ròrỉ) * M_(CH4,bephânhuỷ)`".teX),
        dataTbl(
          tr("M","CH4,phânhuỷròrỉ", b4.M_ch4PhanHuyRoRi, "g/ngày"),
          tr("Pr","CH4,rori", b4.Pr_ch4_roRi, "g/ngày"),
          tr("M","CH4,bephanhuy", b4.M_ch4PhanHuy, "g/ngày")
        ),
        <.h4("4.4.b2 Lượng khí CH4 phân huỷ thu hồi"),
        <.div("Công thức tính: `M_(CH2,phânhuỷthuhoi) = M_(CH4,bephanhuy) * M_(CH4,phânhuỷròrỉ)`".teX),
        dataTbl(
          tr("M","CH4,phânhuỷthuhoi", b4.M_ch4PhanHuyThuHoi, "g/ngày"),
          tr("M","CH4,bephanhuy", b4.M_ch4PhanHuy, "g/ngày"),
          tr("M","CH4,phânhuỷròrỉ", b4.M_ch4PhanHuyRoRi, "g/ngày")
        ),
        <.h4("4.4.b3 Lượng CO2-td khi đốt khí CH4"),
        <.div("Công thức tính: `M_(CO2-td,phânhuỷmetan) = Y_(CH4,đốt) * M_(CH4,phânhuỷthuhoi) + 25 * M_(Ch4,phanhuyròrỉ)`".teX),
        dataTbl(
          tr("M","CO2-td,phânhuỷmêtan", b4.M_co2tdPhanHuyMetan, "g/ngày"),
          tr("Y","CH4,dot", relation.yCH4Combusion, "g/ngày"),
          tr("M","CH4,phanhuythuhoi", b4.M_ch4PhanHuyThuHoi, "g/ngày"),
          tr("M","CH4,phânhuỷròrỉ", b4.M_ch4PhanHuyRoRi, "g/ngày")
        ),
        <.h4("4.4.b4 Lượng KNK từ bể phân huỷ yếm khí trong trường hợp thu hồi và đốt"),
        <.div("Công thức tính: `M_(CO2-td,bephânhuỷđốt) = M_(CO2,bephânhuỷ) + M_(CO2-td,phanhuymêtan)`".teX),
        dataTbl(
          tr("M","CO2-td,bephânhuỷdot", b4.M_co2tdPhanHuyDot, "g/ngày"),
          tr("M","CO2,bephanhuy", b4.M_co2PhanHuy, "g/ngày"),
          tr("M","CO2-td,phânhuỷmêtan", b4.M_co2tdPhanHuyMetan, "g/ngày")
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Bien4")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
