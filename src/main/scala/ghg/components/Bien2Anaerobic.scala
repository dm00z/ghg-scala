package ghg.components

import ghg.Utils._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.KineticCoefficientData.Nitrate
import model.{Bien2Output, GhgData}
import tex.TeX._

object Bien2Anaerobic {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = <.div(d.direct.d.anaerobicPool ? { pool =>
      val b1 = d.bien1
      val b2 = d.bien2Ana
      val pPool = d.direct.d.primaryPool
      val relation = d.direct.relation.value

      val ana = d.direct.coef.anaerobic
      val nit = d.direct.coef.nitrate

      Seq(
        <.h3("2. Đường biên 2 - Hệ xử lý sinh học"),
        <.h4("2.1. Tính toán nồng độ cơ chất trong bể yếm khí"),
        <.div("Công thức tính: `S = (K_s * (1 + K_d * SRT))/(SRT * (Y * k - k_d) - 1)`".teX),
        dataTbl(
          tr("S", b2.s, "mg/l"),
          tr("K", "S", ana.ks(ana.t_an), "mg/l"),
          tr("k", "d", ana.kd, Day1),
          tr("Y", ana.y, "mg/mg"),
          tr("k", ana.k(ana.t_an)),
          tr("SRT", pool.srt, "day")
        ),

        <.h4("2.2. Tính toán nồng độ sinh khối dị dưỡng trong bể yếm khí"),
        <.div("Công thức tính: `X = ((SRT) / (HRT) ) * ((Y * (S_v - S)) / (1 + K_d * SRT))`".teX),
        <.div(s"a. Tính `Q_v = Q_(o,v) - Q_(bl) = ${d.info.power} - ${pPool.q} = ${b2.q_v}`(m3/day)".teX),
        <.div("b. Tính `S_v = S_(o,v) - (BOD_(khu,bl)) / Q_v`".teX),
        dataTbl(
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", "o,v", d.direct.d.streamIn.s, "mg/l"),
          tr("BOD", "khu,bl", b1.bod_khuBl, "g/day"),
          tr("Q", "v", b2.q_v, "m3/day")
        ),
        <.div("c. Tính X"),
        dataTbl(
          tr("X", b2.X, "mg/l"),
          tr("SRT", pool.srt, "day"),
          tr("HRT", pool.hrt, "hour"),
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", b2.s, "mg/l"),
          tr("Y", ana.y, "mg/mg"),
          tr("k", "d", ana.kd, Day1)
        ),

        <.h4("2.3. Tính nồng độ Xnb"),
        <.div("Công thức tính: `X_(nb) = f_d * k_d * X * SRT + (X_(nb,v) *  SRT) / (HRT)`".teX),
        <.h5("a. Tính VSS vào bể sinh học"),
        <.div("Công thức tính: `SS_v = X_(o,v) - (SS_(khu,bl)) / Q_v`".teX),
        dataTbl(2,
          tr("SS", "v", b2.ss_v, "mg/l"),
          tr("`VSS_v = 0.85 * SS_v`".teX, b2.vss_v, "mg/l"),
          tr("X", "o,v", b1.x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "v", b2.q_v, "m3/day")
        ),
        <.h5("b. Tính `X_(nb,v)`".teX),
        <.div(s"Công thức tính: `X_(nb,v) = VSS * (1 - (bpCOD) / (pCOD)) = ${b2.X_nbV}`(mg/l)".teX),
        <.h5("b. Tính `X_(nb)`".teX),
        dataTbl(
          tr("X", "nb", b2.X_nb, "mg/l"),
          tr("f", "d", ana.fd),
          tr("k", "d", ana.kd),
          tr("X", b2.X, "mg/l"),
          tr("X", "nb,v", b2.X_nbV, "mg/l"),
          tr("SRT", pool.srt, "day"),
          tr("HRT", pool.hrt, "hour")
        ),
//
//        <.h4("2.4. Tính `X_(nit)`".teX),
//        <.div("Công thức tính: `X_(nit) = ((SRT_(nit)) / (HRT) ) * ((Y_(nit) * N) / (1 + K_(d,nit) * SRT_(nit)))`".teX),
//        <.h5("a. Tính `mu_(nit)`".teX),
//        <.div("Công thức tính: `mu_(nit) = ( (mu_(m,nit) * N) / (K_N + N) ) * ( (DO) / (K_(DO) + DO) ) - k_(d,nit)`".teX),
//        dataTbl(
//          tr("μ", "nit", b2.m_nit, Day1),
//          tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
//          tr("μ", "m,nit", nit.m_),
//          tr("K", "N", nit.kn_),
//          tr("k", "d,nit", nit.kd_, Day1),
//          tr("K", "DO", Nitrate.Kdo),
//          tr("DO", Nitrate.DO),
//          tr("N", b2.N, "mg/l")
//        ),
//        <.h5(s"b. Tính `SRT_(nit) = 1/mu_(nit) = ${b2.srtNit}`(day)".teX),
//        <.h5(s"c. Tính `X_(nit)`".teX),
//        dataTbl(
//          tr("X", "nit", b2.x_nit, "mg/l"),
//          tr("SRT", "nit", b2.srtNit, "day"),
//          tr("HRT", pool.hrt, "hour"),
//          tr("Y", "nit", nit.y_),
//          tr("k", "d,nit", nit.kd_, Day1),
//          tr("N", b2.N, "mg/l")
//        ),

        <.h4("2.5. Tính lượng bùn tạo ra"), //???2.4
        <.div("Công thức tính: `P_(SS) = (X_(tong,SS) * V) / (SRT) = P_(SS,BOD) + P_(SS,manhtebao) + P_(SS,nbVSS)`".teX),
        <.h5("a. Lượng bùn tạo ra do khử BOD"),
        <.div("Công thức tính: `P_(SS,BOD) = (X * V) / (SRT)`".teX),
        dataTbl(
          tr("P", "SS,BOD", b2.p_ssBod, "g/day"),
          tr("X", b2.X, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("SRT", pool.srt, "day")
        ),
//        <.h5("b. Lượng bùn tạo ra do nitrat hóa"),
//        <.div("Công thức tính: `P_(SS,nit) = (X_(nit) * V) / (SRT_(nit))`".teX),
//        dataTbl(
//          tr("P", "SS,nit", b2.p_ssNit, "g/day"),
//          tr("X", "nit", b2.x_nit, "mg/l"),
//          tr("V", b2.V, "mg/l"),
//          tr("SRT", "nit", b2.srtNit, "day")
//        ),
        <.h5("c. Lượng bùn tạo ra do phân hủy mảnh vụn tế bào"), //???b
        <.div("Công thức tính: `P_(SS,manhTeBao) = f_d * k_d * X * V`".teX),
        dataTbl(
          tr("P", "SS,manhTeBao", b2.p_ssManhTeBao, "g/day"),
          tr("X", b2.X, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("f", "d", ana.fd),
          tr("k", "d", ana.kd, Day1)
        ),
        <.h5("d. Lượng bùn do sinh khối không phân hủy"),
        <.div("Công thức tính: `P_(SS,nbVSS) = (X_(nb,v) * V) / (HRT)`".teX),
        dataTbl(
          tr("P", "SS,nbVSS", b2.p_ssNbVss, "g/day"),
          tr("X", "nb,v", b2.X_nbV, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("HRT", pool.hrt, "hour")
        ),
        <.h5(s"e. Tổng lượng bùn sinh ra `P_(SS) = ${b2.p_ss}`(g/day)".teX),
        <.h5(s"f. Tổng lượng bùn sinh ra do phân hủy sinh học `P_(SS,bio) = P_(SS) - Q_v * X_(nb,VSS) = ${b2.p_ssBio}`(g/day)".teX),

//        <.h4("2.6. Tính nồng độ Nito bị ô xi hóa trong bể xử lý sinh học"),
//        <.div("Công thức tính: `N = TN_v - N_r - (0.12 * P_(SS,bio)) / Q_v`".teX),
//        dataTbl(
//          tr("N", b2.Ncalc, "mg/l"),
//          tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
//          tr("N", "r", d.direct.d.streamOut.n, "mg/l"),
//          tr("P", "SS,bio", b2.p_ssBio, "g/day"),
//          tr("Q", "v", b2.q_v, "m3/day")
//        ),

        <.h4("2.7. Tính lượng khí CO2 sinh ra trong bể yếm khí"),
        <.h5("2.7.1 Tính lượng CO2 sinh ra do khử BOD"),
        <.div("a. Lượng BOD bị oxi hóa"),
        <.div("Công thức tính: `BOD_(khu) = Q_v * (S_v - S) - r_(O2,phanhuy) * P_(SS,bio)`".teX),
        dataTbl(
          tr("BOD", "khu", b2.bod_ox, "g/day"),
          tr("Q", "v", b2.q_v, "m3/day"),
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", b2.s, "mg/l"),
          tr("r", "O2,phanhuy", relation.rCO2Decay, "g/g"),
          tr("P", "SS,bio", b2.p_ssBio, "g/day")
        ),
//        <.div("b. Lượng BOD bị khử trong quá trình denitrat hóa"),
//        <.div("Công thức tính: `BOD_(o x,dnt) = r_(BOD,dnt) * N * Q_v`".teX),
//        dataTbl(
//          tr("BOD", "ox,dnt", b2.bod_ox_dnt, "g/day"),
//          tr("r", "BOD,dnt", relation.rBODDnt, "g/g"),
//          tr("N", b2.N, "mg/l"),
//          tr("Q", "v", b2.q_v, "m3/day")
//        ),
//        <.div(s"c. Lượng BOD bị khử thực = ${b2.bod_khuThuc} (g/day)"),
        <.div("d. Lượng phát thải CO2 do khử BOD"),
        <.div("Công thức tính: `CO_(2,khuBOD) = Y_(CO2) * BOD_(khu)`".teX),
        dataTbl(
          tr("CO2", "khu,BOD", b2.co2_khu_bod, "g/day"),
          tr("Y", "CO2,an", relation.yCO2An),
          tr("BOD", "khu", b2.bod_ox)
        ),

        <.h5("2.7.2 Lượng CO2 do phân hủy nội bào sinh khối"),
        <.div("a. Lượng sinh khối  phân hủy nội bào"),
        <.div("Công thức tính: `VSS_(phanhuy) = 0.85 * V * k_d * X`".teX),
        dataTbl(
          tr("VSS", "phanhuy", b2.vss_phanhuy, "g/day"),
          tr("V", b2.V, "mg/l"),
          tr("X", b2.X, "mg/l"),
//          tr("X", "nit", b2.x_nit, "mg/l"),
          tr("k", "d", ana.kd, Day1)
//          tr("k", "d,nit", nit.kd_, Day1)
        ),
        <.div("b. Lượng CO2 do phân hủy nội bào sinh khối"),
        <.div("Công thức tính: `CO_(2,phanhuy) = Y_(CO2,phanhuy) * VSS_(phanhuy)`".teX),
        dataTbl(
          tr("CO", "2,VSSphanhuy", b2.co2_phanhuy, "g/day"),
          tr("Y", "CO2,phanhuy,an", relation.yCO2AnDecay),
          tr("VSS", "phanhuy", b2.vss_phanhuy, "g/day")
        ),

//        <.h5("2.7.3 Lượng khí CO2 do khử nito"),
//        <.div("Công thức tính: `CO_(2,dnt) = Y_(CO2,dnt) * N * Q_v`".teX),
//        dataTbl(
//          tr("CO", "2,dnt", b2.co2_dnt, "g/day"),
//          tr("Y", "CO2,dnt", relation.yCO2Dnt),
//          tr("N", b2.N),
//          tr("Q", "v", b2.q_v, "m3/day")
//        ),
//
//        <.h5("2.7.4 Lượng CO2 tiêu thụ do nitrat hóa"),
//        <.div("Công thức tính: `CO_(2,tieuThuNit) = r_(CO2,nit) * N * Q_v`".teX),
//        dataTbl(
//          tr("CO", "2,tieuThuNit", b2.co2_tieuThuNit, "g/day"),
//          tr("r", "CO2,nit", relation.rCO2Nit),
//          tr("N", b2.N),
//          tr("Q", "v", b2.q_v, "m3/day")
//        ),

        <.h5("2.7.5 Lượng CO2 phát sinh trong quá trình yếm khí"),
        <.div(
          f"""Công thức tính: `CO_(2,quaTri nhYemKhi)
             = CO_(2,khuBOD) + CO_(2,VSSphanHuy)
             = ${b2.co2_quaTrinh}%,.2f`(g/day)""".teX),

        <.h5("2.7.6 Lượng CH4 phát sinh trong quá trình yếm khí"),
        <.div("Công thức tính: `CH_(4,beYemKhi) = Y_(CH_4,an) * BOD_(khu,an) + Y_(CH_4,decay,an) * VSS_(decay,an)`".teX),
        dataTbl(2,
          tr("CH", "4,beYemKhi", b2.ch4_beYemKhi, "g/day"),
          tr("CH", "4,beYemKhi", b2.ch4_beYemKhi / 1000, "kg/day"),
          tr("Y", "CH_4,an", relation.yCH4An),
          tr("BOD", "khu,an", b2.bod_khu_an, "g/day"),
          tr("Y", "CH_4,decay,an", relation.yCH4AnDecay),
          tr("VSS", "decay,an", b2.vss_phanhuy, "g/day")
        )
      )
    })
  }

  val component = ReactComponentB[Props]("Bien2Aerobic")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
