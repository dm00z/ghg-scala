package ghg.components

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{Bien2Output, GhgData}
import model.KineticCoefficientData.Nitrate
import tex.TeX._
import ghg.Utils._
import scala.scalajs.js.JSNumberOps._

object Bien2Aerobic {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = <.div(d.direct.d.aerobicPool ? { pool =>
      val b1 = d.bien1
      val b2 = d.bien2Ae
      val pPool = d.direct.d.primaryPool
      val relation = d.direct.relation

      val ae = d.direct.coef.aerobic
      val nit = d.direct.coef.nitrate

      Seq(
        <.h3("2. Đường biên 2 - Hệ xử lý hiếu khí"),
        <.h4("2.1. Tính toán nồng độ cơ chất trong bể hiếu khí"),
        <.div("Công thức tính: `S = (K_s * (1 + K_d * SRT))/(SRT * (Y * k - k_d) - 1)`".teX),
        dataTbl(
          tr("S", b2.s, "mg/l"),
          tr("K", "S", ae.ks_, "mg/l"),
          tr("k", "d", ae.kd_, Day1),
          tr("Y", ae.y_, "mg/mg"),
          tr("k", ae.k_),
          tr("SRT", pool.srt, "day")
        ),

        <.h4("2.2. Tính toán nồng độ sinh khối dị dưỡng trong bể hiếu khí"),
        <.div("Công thức tính: `X = ((SRT) / (HRT) ) * ((Y * (S_v - S)) / (1 + k_d * SRT))`".teX),
        <.div(s"a. Tính `Q_v = Q_(o,v) - Q_(bl) = ${d.info.power} - ${pPool.q} = ${b2.q_v}`(m3/day)".teX),
        <.div("b. Tính `S_v = S_(o,v) - (BOD_(khu,bl)) / Q_v`".teX),
        dataTbl(
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", "o,v", d.direct.d.streamIn.so, "mg/l"),
          tr("BOD", "khu,bl", b1.bod_khuBl, "g/day"),
          tr("Q", "v", b2.q_v, "m3/day")
        ),
        <.div("c. Tính X"),
        dataTbl(
          tr("X", b2.X, "mg/l"),
          tr("SRT", pool.srt, "ngày"),
          tr("HRT", pool.hrt, "ngày"),
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", b2.s, "mg/l"),
          tr("Y", ae.y_, "mg/mg"),
          tr("k", "d", ae.kd_, Day1)
        ),

        <.h4("2.3. Tính nồng độ Xnb"),
        <.div("Công thức tính: `X_(nb) = f_d * k_d * X * SRT + (X_(nb,v) *  SRT) / (HRT)`".teX),
        <.h5("a. Tính VSS vào bể sinh học"),
        <.div("Công thức tính: `SS_v = X_(o,v) - (SS_(khu,bl)) / Q_v`".teX),
        dataTbl(2,
          tr("SS", "v", b2.ss_v, "mg/l"),
          tr("`VSS_v = 0.85 * SS_v`".teX, b2.vss_v, "mg/l"),
          tr("SS", "o,v", b1.ss_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "v", b2.q_v, "m3/day")
        ),
        <.h5("b. Tính `X_(nb,v)`".teX),
        <.div(s"Công thức tính: `X_(nb,v) = VSS * (1 - (bpCOD) / (pCOD)) = ${b2.X_nbV}`(mg/l)".teX),
        <.h5("b. Tính `X_(nb)`".teX),
        dataTbl(
          tr("X", "nb", b2.X_nb, "mg/l"),
          tr("f", "d", ae.fd),
          tr("k", "d", ae.kd_),
          tr("X", b2.X, "mg/l"),
          tr("X", "nb,v", b2.X_nbV, "mg/l"),
          tr("SRT", pool.srt, "day"),
          tr("HRT", pool.hrt, "hour")
        ),

        <.h4("2.4. Tính `X_(nit)`".teX),
        <.div("Công thức tính: `X_(nit) = ((SRT_(nit)) / (HRT) ) * ((Y_(nit) * N) / (1 + K_(d,nit) * SRT_(nit)))`".teX),
        <.h5(s"Tính `X_(nit)`".teX),
        dataTbl(
          tr("X", "nit", b2.x_nit, "mg/l"),
          tr("SRT", "nit", b2.srtNit, "day"),
          tr("HRT", pool.hrt, "hour"),
          tr("Y", "nit", nit.y_),
          tr("k", "d,nit", nit.kd_, Day1),
          tr("N", b2.N, "mg/l")
        ),

        <.h4("2.5. Tính lượng bùn tạo ra"),
        <.div("Công thức tính: `P_(X) = (X_(tổng,SS) * V) / (SRT) = P_(X,BOD) + P_(X,manhtebao) + P_(X,nbVSS) + P_(X,nit)`".teX),
        <.h5("a. Lượng bùn tạo ra do khử BOD"),
        <.div("Công thức tính: `P_(X,BOD) = (X * V) / (SRT)`".teX),
        dataTbl(
          tr("P", "X,BOD", b2.p_XBod, "g/day"),
          tr("X", b2.X, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("SRT", pool.srt, "day")
        ),
        <.h5("b. Lượng bùn tạo ra do phân hủy mảnh vụn tế bào"),
        <.div("Công thức tính: `P_(X,manhTeBao) = f_d * k_d * X * V`".teX),
        dataTbl(
          tr("P", "X,manhTeBao", b2.p_ssManhTeBao, "g/day"),
          tr("X", b2.X, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("f", "d", ae.fd),
          tr("k", "d", ae.kd_, Day1)
        ),
        <.h5("c. Lượng bùn do sinh khối không phân hủy"),
        <.div("Công thức tính: `P_(X,nbVSS) = (X_(nb,v) * V) / (HRT)`".teX),
        dataTbl(
          tr("P", "X,nbVSS", b2.p_ssNbVss, "g/day"),
          tr("X", "nb,v", b2.X_nbV, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("HRT", pool.hrt, "hour")
        ),
        <.h5("d. Lượng bùn tạo ra do nitrat hóa"),
        <.div("Công thức tính: `P_(X,nit) = (X_(nit) * V) / (SRT_(nit))`".teX),
        dataTbl(
          tr("P", "X,nit", b2.p_ssNit, "g/day"),
          tr("X", "nit", b2.x_nit, "mg/l"),
          tr("V", b2.V, "mg/l"),
          tr("SRT", "nit", b2.srtNit, "day")
        ),
        <.h5(s"e. Tổng lượng bùn sinh ra `P_(X) = ${b2.p_ss.toFixed(3)}`(g/day)".teX),
        <.h5(s"f. Tổng lượng bùn sinh ra do phân hủy sinh học `P_(X,bio) = P_(X) - Q_v * X_(nb,VSS) = ${b2.p_XBio.toFixed(3)}`(g/day)".teX),

        <.h4("2.6. Tính nồng độ Nito bị ô xi hóa trong bể xử lý sinh học"),
        <.div("Công thức tính: `N = N_v - N_r - (0.12 * P_(X,bio)) / Q_v`".teX),
        dataTbl(
          tr("N", b2.Ncalc, "mg/l"),
          tr("N","v", d.direct.d.streamIn.n, "mg/l"),
          tr("N", "r", d.direct.d.streamOut.n, "mg/l"),
          tr("P", "SS,bio", b2.p_XBio, "g/day"),
          tr("Q", "v", b2.q_v, "m3/day")
        ),

        <.h4("2.7. Tính lượng khí CO2 sinh ra trong bể xử lý sinh học"),
        <.h5("2.7.1 Tính lượng CO2 sinh ra do khử BOD"),
        <.div("Công thức tính: `M_(CO2,khuBOD) = Y_(CO2) * (Q_v * (S_v - S) - r_(O2,phanhuy) * P_(X,BOD))`".teX),
        dataTbl(
          tr("M", "CO2, khuBOD", b2.bod_ox, "g/day"),
          tr("Y", "CO2", relation.yCO2, "gCO2/gBOD"),
          tr("Q", "v", b2.q_v, "m3/day"),
          tr("S", "v", b2.s_v, "mg/l"),
          tr("S", b2.s, "mg/l"),
          tr("r", "O2,phanhuy", relation.rCO2Decay, "g/g"),
          tr("P", "X,BOD", b2.p_XBod, "g/day")
        ),

        <.h5("2.7.2 Lượng CO2 do phân hủy nội bào sinh khối"),
        <.div("a. Lượng sinh khối  phân hủy nội bào"),
        <.div("Công thức tính: `VSS_(phanhuy) = 0.85 * V * (k_d * X + k_(d,nit) * X_(nit))`".teX),
        dataTbl(
          tr("VSS", "phanhuy", b2.vss_phanhuy, "g/day"),
          tr("V", b2.V, "mg/l"),
          tr("X", b2.X, "mg/l"),
          tr("X", "nit", b2.x_nit, "mg/l"),
          tr("k", "d", ae.kd_, Day1),
          tr("k", "d,nit", nit.kd_, Day1)
        ),
        <.div("b. Lượng CO2 do phân hủy nội bào sinh khối"),
        <.div("Công thức tính: `M_(CO_(2,phanhuy)) = Y_(CO2,phanhuy) * VSS_(phanhuy)`".teX),
        dataTbl(
          tr("M", "CO2,VSSphanhuy", b2.co2_phanhuy, "g/day"),
          tr("Y", "CO2,phanhuy", relation.yCO2Decay),
          tr("VSS", "phanhuy", b2.vss_phanhuy, "g/day")
        ),

        <.h5("2.7.3 Lượng khí CO2 do khử nito"),
        <.div("Công thức tính: `M_(CO_2,dnt) = Y_(CO2,dnt) * N * Q_v`".teX),
        dataTbl(
          tr("M", "CO2,dnt", b2.co2_dnt, "g/day"),
          tr("Y", "CO2,dnt", relation.yCO2Dnt),
          tr("N", b2.N),
          tr("Q", "v", b2.q_v, "m3/day")
        ),

        <.h5("2.7.4 Lượng CO2 tiêu thụ do nitrat hóa"),
        <.div("Công thức tính: `M_(CO_2,tieuThuNit) = r_(CO2,nit) * N * Q_v`".teX),
        dataTbl(
          tr("CO", "2,tieuThuNit", b2.co2_tieuThuNit, "g/day"),
          tr("r", "CO2,nit", relation.rCO2Nit),
          tr("N", b2.N),
          tr("Q", "v", b2.q_v, "m3/day")
        ),

        <.h5("2.7.5 Lượng CO2 phát sinh từ bể xử lý sinh học"),
        <.div("Công thức tính: `M_(CO_2,besinhhoc) = M_(CO_2,khuBOD) + M_(CO_2,VSSphanhuy) + M_(CO_2,dnt) - M_(CO_2,tieuthunit)`".teX),
        dataTbl(
          tr("M","CO2,besinhhoc", b2.co2_quaTrinh, "g/ngày"),
          tr("M", "CO2,khuBOD", b2.bod_ox, "g/ngày"),
          tr("M", "CO2,VSSphanhuy", b2.co2_phanhuy,"g/ngày"),
          tr("M", "C02,dnt", b2.co2_dnt,"g/ngày"),
          tr("M", "Co2,tieuthunit", b2.co2_tieuThuNit, "g/day")
        )
//        <.h5("2.7.6 Tỷ lệ phát thải CO2 trong bể xử lý sinh học"),
//        <.div(f"Tỷ lệ phát thải `M_(CO2)/Q: ${b2.tyLePhatThai}%,.2f`".teX)
      )
    })
  }

  val component = ReactComponentB[Props]("Bien2Aerobic")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
