package ghg.components

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import tex.TeX._
import ghg.Utils._

object Bien3 {
  case class Props(d: GhgData, isAerobic: Boolean)
  case class Backend($: BackendScope[Props, _]) {
    def render(props: Props) = {
      val d = props.d
      val b1 = d.bien1
      val pPool = d.direct.d.primaryPool
      val dPool = d.direct.d.decayPool
      val relation = d.direct.relation
      val (b2, b3) = if(props.isAerobic) (d.bien2Ae, d.bien3) else (d.bien2Ana, d.bien3Ana)
      val ane = d.direct.coef.anaerobic

      <.div(
        <.h3("3. Bể phân hủy yếm khí"),
        <.h4("3.1. Tổng lượng bùn vào bể phân hủy"),
        <.div("Công thức tính: `P_(VSS,dr) = SS_(khu,bl) + P_(SS)`".teX),
        dataTbl(
          tr("P", "VSS,dr", b3.p_vss_dr, "g/day"),
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"),
          tr("P", "SS", b2.p_ss, "g/day")
        ),
        <.h4("3.2. Lưu lượng bùn vào bể phân hủy"),
        <.h5("a. Tính lưu lượng bùn xả ở bể hiếu khí"),
        <.div(s"`Q_(xa) = Q_(ratio) * Q_v = ${b3.q_xa}`(m3/day)".teX),
        <.h5("b. Tính lưu lượng bùn vào bể phân hủy"),
        <.div(s"`Q_(v,dr) = Q_(bl) + Q_(xa) = ${pPool.q} + ${b3.q_xa} = ${b3.q_v_dr}`".teX),

        <.h4("3.3. Tính nồng độ cơ chất vào bể phân hủy"),
        <.div(s"`Công thức tính: S_v^(dr) = (BOD_(khu,bl) + Q_(xa) * S) / Q_(v,dr)`".teX),
        dataTbl(
          tr(<.span("S", <.sub("v"), <.sup("dr")), b3.s_v_dr, "mg/l"),
          tr("BOD", "khu,bl", b1.bod_khuBl, "g/day"),
          tr("Q", "xa", b3.q_xa, "m3/day"),
          tr("Q", "v,dr", b3.q_v_dr, "m3/day"),
          tr("S", b2.s, "mg/l")
        ),

        <.h4("3.4. Tính nồng độ cơ chất dòng ra bể phân hủy"),
        <.div("Công thức tính: `S^(dr) = (K_s^(dr) * (1 + K_d^(dr) * SRT^(dr)))/(SRT^(dr) * (Y^(dr) * k^(dr) - k_d^(dr)) - 1)`".teX),
        dataTbl(
          tr("S", "dr", b3.s_dr, "mg/l"),
          tr("K", "S,dr", ane.ks(ane.t_dr), "mg/l"),
          tr("k", "d,dr", ane.kd, Day1),
          tr("Y", "dr", ane.y, "mg/mg"),
          tr("k", "dr", ane.k(ane.t_dr)),
          tr("SRT", "dr", dPool.srt, "day")
        ),

        <.h4("3.5. Tổng lượng bùn sinh học trong bể phân hủy"),
        <.h5("a. Tính lượng bùn do khử BOD trong bể phân hủy"),
        <.div("Công thức tính: `S_(SS,BOD,dr) = (Q_(v,dr) * Y_(dr) * (S_(v,dr) - S_(dr))) / (1 + k_(d,dr) * SRT_(dr))`".teX),
        dataTbl(
          tr("P", "SS,BOD,dr", b3.p_ssBodDr, "g/day"),
          tr("Y", "dr", ane.y, "mg/mg"),
          tr("k", "d,dr", ane.kd, Day1),
          tr("SRT", "dr", dPool.srt, "day"),
          tr("S", "v,dr", b3.s_v_dr, "mg/l"),
          tr("S", "dr", b3.s_dr, "mg/l"),
          tr("Q", "v,dr", b3.q_v_dr, "m3/day")
        ),
        <.h5("b. Tính lượng bùn do phân hủy mảnh tế bào trong bể phân hủy"),
        <.div("Công thức tính: `P_(SS,manhTeBao,dr) = f_(d,dr) * k_(d,dr) * SRT_(dr) * P_(SS,BOD,dr)`".teX),
        dataTbl(
          tr("P", "SS,manhTeBao,dr", b3.p_ssManhTeBaoDr, "g/day"),
          tr("f", "d,dr", ane.fd),
          tr("k", "d,dr", ane.kd, Day1),
          tr("SRT", "dr", dPool.srt, "day"),
          tr("P", "SS,BOD,dr", b3.p_ssBodDr, "g/day")
        ),
        <.h5("c. Lượng bùn sinh học trong bể phân hủy"),
        <.div(s"`P_(SS,bio,dr) = P_(SS,BOD,dr) + P_(SS,manhTeBao,dr) = ${b3.p_ssBodDr} + ${b3.p_ssManhTeBaoDr} = ${b3.p_ssBioDr}`(g/day)".teX),

        <.h4("3.6. Lượng BOD bị khử trong bể phân hủy"),
        <.div("Công thức tính: `BOD_(khu,dr) = Q_(v,dr) * (S_(v,dr) - S_(dr)) - 1.42 * P_(SS,bio,dr)`".teX),
        dataTbl(
          tr("BOD", "khu,dr", b3.bod_khu_dr, "g/day"),
          tr("Q", "v,dr", b3.q_v_dr, "m3/day"),
          tr("S", "v,dr", b3.s_v_dr, "mg/l"),
          tr("S", "dr", b3.s_dr, "mg/l"),
          tr("P", "SS,bio,dr", b3.p_ssBioDr, "g/day")
        ),
        <.h4("3.7. Nồng độ sinh khối trong bể phân hủy"),
        <.div("Công thức tính: `X_(dr) = (P_(SS,BOD,dr) + P_(VSS,dr)) / Q_(v,dr)`".teX),
        dataTbl(
          tr("X", "dr", b3.x_dr, "mg/l"),
          tr("P", "SS,BOD,dr", b3.p_ssBodDr, "g/day"),
          tr("P", "VSS,dr", b3.p_vss_dr, "g/day"),
          tr("Q", "v,dr", b3.q_v_dr, "m3/day")
        ),
        <.h4("3.8. Sinh khối phân hủy nội bào trong bể phân hủy"),
        <.h5("a. Thể tích bể phân hủy yếm khí"),
        <.div(s"`V_(dr) = HRT_(dr) * Q_(v,dr) = ${b3.v_dr}`(m3)".teX),
        <.h5("b. Sinh khối phân hủy nội bào trong bể phân hủy"),
        <.div(s"Công thức tính: `VSS_(decay,dr) = V_(dr) * kd_(dr) * X_(dr)`".teX),
        dataTbl(
          tr("VSS", "decay,dr", b3.vss_decay_dr, "g/day"),
          tr("V", "dr", b3.v_dr, "m3"),
          tr("k", "d,dr", ane.kd, Day1),
          tr("X", "dr", b3.x_dr, "mg/l")
        ),
        <.h4("3.9. Lượng khí nhà kính phát sinh từ bể phân hủy yếm khí"),
        <.h5("a. Lượng khí CO2 từ bể phân hủy yếm khí"),
        <.div(s"Công thức tính: `CO_(2,bePhanHuy) = Y_(CO_2,dr) * BOD_(khu,dr) + Y_(CO_2,phanHuy,dr) * VSS_(phanHuy,dr)`".teX),
        dataTbl(
          tr("CO", "2,bePhanHuy", b3.co2_bePhanHuy, "g/day"),
          tr("Y", "CO2,dr", relation.yCO2Dr),
          tr("BOD", "khu,dr", b3.bod_khu_dr, "g/day"),
          tr("Y", "CO2,phanHuy,dr", relation.yCO2DrDecay),
          tr("VSS", "decay,dr", b3.vss_decay_dr, "g/day")
        ),
        <.h5("b. Lượng khí CH4 từ bể phân hủy yếm khí"),
        <.h5("b.1. Lượng khí CH4 phát sinh"),
        <.div(s"Công thức tính: `CH_(4,bePhanHuy) = Y_(CH_4,dr) * BOD_(khu,dr) + Y_(CH_4,phanHuy,dr) * VSS_(phanHuy,dr)`".teX),
        dataTbl(
          tr("CH", "4,bePhanHuy", b3.ch4_bePhanHuy, "g/day"),
          tr("Y", "CH4,dr", relation.yCH4Dr),
          tr("BOD", "khu,dr", b3.bod_khu_dr, "g/day"),
          tr("Y", "CH4,phanHuy,dr", relation.yCH4DrDecay),
          tr("VSS", "decay,dr", b3.vss_decay_dr, "g/day")
        ),
        <.h5(s"b.2. Lượng khí CH4 thu hồi = ${b3.ch4_phanHuy_thuHoi}(g/day)"),
        <.h5(s"b.3. Lượng khí CH4 rò rỉ = ${b3.ch4_phanHuy_roRi}(g/day)"),
        <.h5("b.4. Lượng khí CO2 tương đương của khí CH4"),
        <.div(s"Công thức tính: `C0_(2,phanHuyMe tan) = Y_(CH_4,d ot) * CH_(4,phanHuyThuHoi) + 25 * CH_(4,phanHuyRoRi) = ${b3.co2_phanHuyMetan}`(g/day)".teX),
        if(! props.isAerobic) EmptyTag else Seq(
          <.h5("c. Tổng lượng KNK phát sinh từ bể phân hủy yếm khí"),
          table(
            tr("CO", "2,bePhanHuy", b3.co2_bePhanHuy, "g/day"),
            tr("CO", "2,phanHuyMetan", b3.co2_phanHuyMetan, "g/day"),
            tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal, "g/day"),
            tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal / 1000, "kg/day")
          )
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Bien3")
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}
