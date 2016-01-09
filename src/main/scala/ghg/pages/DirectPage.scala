package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.KineticCoefficientData.Nitrate
import scala.annotation.tailrec
import tex.TeX._
import ghg.Utils._

object DirectPage {
  type Props = ModelProxy[GhgData]

  private def dataTbl(result: TagMod, dataRows: TagMod*) =
    table(
      <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
      dataRows,
      <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
      result
    )

  private def dataTbl(numResult: Int, rows: TagMod*) =
    rows.splitAt(numResult) match {
      case (results, dataRows) =>
        table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          dataRows,
          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          results
        )
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val d = P()

      val pPool = d.direct.d.primaryPool

      val s_ov = d.direct.d.streamIn.s
      val pr_blBod = pPool.prBOD
      val bod_khuBl = d.info.power * s_ov * pr_blBod
      val tbl1_1 = dataTbl(
        tr("BOD", "khu,bl", bod_khuBl, "g/day"), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
        tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
        tr("S", "o,v", s_ov, "mg/l"), //Nồng độ `BOD_5` dòng vào ban đầu
        tr("Pr", "bl,BOD", pr_blBod, "%") //Phần trăm khư `BOD_5` trong bể lắng sơ cấp
      )

      val x_ov = d.direct.d.streamIn.tss
      val pr_blSs = pPool.prSS
      val ss_khuBl = d.info.power * x_ov * pr_blSs
      val tbl1_2 = dataTbl(
        tr("SS", "khu,bl", ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
        tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
        tr("X", "o,v", x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
        tr("Pr", "bl,SS", pr_blSs, "%") //Phần trăm khư SS trong bể lắng sơ cấp
      )

//      def tbl2(implicit d: Aerobic) = {
//        @inline implicit def dispatch: Aerobic => Callback = P.my.dispatch
//        val m = d.m(d.t)
//        val y = d.y(d.t)
//
//        table(
//          <.tr(<.td("Nhiệt độ (°C)"), <.td(Muy), <.td("k = ", Muy, "/Y"), td("K", "s"), td("k", "d"), <.td("Y")),
//          <.tr(tdInput(Aerobic.t), <.td(m), <.td(m/y), <.td(), <.td(), <.td(y))
//        )
//      }

      val p2Aerobic = d.direct.d.aerobicPool ? { pool =>
        val ae = d.direct.coef.aerobic
        val s = ae.ks_ * (1 + ae.kd_ * pool.srt) / (pool.srt * (ae.y_ * ae.k_ - ae.kd_) - 1)

        val p21S = dataTbl(
          tr("S", s, "mg/l"),
          tr("K", "S", ae.ks_, "mg/l"),
          tr("k", "d", ae.kd_, Day1),
          tr("Y", ae.y_, "mg/mg"),
          tr("k", ae.k_),
          tr("SRT", pool.srt, "day")
        )

        val q_v = d.info.power - pPool.q
        val qv_21a = <.div(s"a. Tính `Q_v = Q_(o,v) - Q_(bl) = ${d.info.power} - ${pPool.q} = $q_v`(m3/day)".teX)

        val s_v = d.direct.d.streamIn.s - bod_khuBl / q_v
        val sv_21b = dataTbl(
          tr("S", "v", s_v, "mg/l"),
          tr("S", "o,v", d.direct.d.streamIn.s, "mg/l"),
          tr("BOD", "khu,bl", bod_khuBl, "g/day"),
          tr("Q", "v", q_v, "m3/day")
        )

        val X = pool.srt / pool.hrtDay * ae.y_ * (s_v - s) / (1 + ae.kd_ * pool.srt)
        val x_21c = dataTbl(
          tr("X", X, "mg/l"),
          tr("SRT", pool.srt, "day"),
          tr("HRT", pool.hrt, "hour"),
          tr("S", "v", s_v, "mg/l"),
          tr("S", s, "mg/l"),
          tr("Y", ae.y_, "mg/mg"),
          tr("k", "d", ae.kd_, Day1)
        )

        val ss_v = x_ov - ss_khuBl / q_v
        val vss_v = 0.85 * ss_v
        val vss_23a = dataTbl(2,
          tr("SS", "v", ss_v, "mg/l"),
          tr("`VSS_v = 0.85 * SS_v`".teX, vss_v, "mg/l"),
          tr("X", "o,v", x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("SS", "khu,bl", ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "v", q_v, "m3/day")
        )

        val X_nbV = vss_v * (1 - 60D/ 73)

        val X_nb = ae.fd * ae.kd_ *  X * pool.srt + X_nbV * pool.srt / pool.hrtDay
        val X_nb_23c = dataTbl(
          tr("X", "nb", X_nb, "mg/l"),
          tr("f", "d", ae.fd),
          tr("k", "d", ae.kd_),
          tr("X", X, "mg/l"),
          tr("X", "nb,v", X_nbV, "mg/l"),
          tr("SRT", pool.srt, "day"),
          tr("HRT", pool.hrt, "hour")
        )

        val nit = d.direct.coef.nitrate
        def m_nit(N: Double) = nit.m_ * N / (nit.kn_ + N) * Nitrate.DO / (Nitrate.Kdo + Nitrate.DO) - nit.kd_
        def mu_24a(N: Double) = dataTbl(
          tr("μ", "nit", m_nit(N), Day1),
          tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
          tr("μ", "m,nit", nit.m_),
          tr("K", "N", nit.kn_),
          tr("k", "d,nit", nit.kd_, Day1),
          tr("K", "DO", Nitrate.Kdo),
          tr("DO", Nitrate.DO),
          tr("N", N, "mg/l")
        )

        @inline def srtNit(N: Double) = 1 / m_nit(N)

        def x_nit(N: Double) = srtNit(N) / pool.hrtDay * nit.y_ * N / (1 + nit.kd_ * srtNit(N))

        def x_nit_24c(N: Double) = dataTbl(
          tr("X", "nit", x_nit(N), "mg/l"),
          tr("SRT", "nit", srtNit(N), "day"),
          tr("HRT", pool.hrt, "hour"),
          tr("Y", "nit", nit.y_),
          tr("k", "d,nit", nit.kd_, Day1),
          tr("N", N, "mg/l")
        )

        val V = pool.hrtDay * q_v
        val p_ssBod = X * V / pool.srt
        val p_ssBod_25a = dataTbl(
          tr("P", "SS,BOD", p_ssBod, "g/day"),
          tr("X", X, "mg/l"),
          tr("V", V, "mg/l"),
          tr("SRT", pool.srt, "day")
        )

        def p_ssNit(N: Double) = x_nit(N) * V / srtNit(N)

        def p_ssNit_25b(N: Double) = dataTbl(
          tr("P", "SS,nit", p_ssNit(N), "g/day"),
          tr("X", "nit", x_nit(N), "mg/l"),
          tr("V", V, "mg/l"),
          tr("SRT", "nit", srtNit(N), "day")
        )

        val p_ssManhTeBao = ae.fd * ae.kd_ * X * V
        val p_ssManhTeBao_25c = dataTbl(
          tr("P", "SS,manhTeBao", p_ssManhTeBao, "g/day"),
          tr("X", X, "mg/l"),
          tr("V", V, "mg/l"),
          tr("f", "d", ae.fd),
          tr("k", "d", ae.kd_, Day1)
        )

        val p_ssNbVss = X_nbV * V / pool.hrtDay
        val p_ssNbVss_25d = dataTbl(
          tr("P", "SS,nbVSS", p_ssNbVss, "g/day"),
          tr("X", "nb,v", X_nbV, "mg/l"),
          tr("V", V, "mg/l"),
          tr("HRT", pool.hrt, "hour")
        )

        def p_ss(N: Double) = p_ssBod + p_ssNit(N) + p_ssManhTeBao + p_ssNbVss
        def p_ssBio(N: Double) = p_ss(N) - p_ssNbVss //=..

        def calcN(N: Double) = d.direct.d.streamIn.tkn - d.direct.d.streamOut.n - .12 * p_ssBio(N) / q_v
        def N_26(N: Double) = dataTbl(
          tr("N", calcN(N), "mg/l"),
          tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
          tr("N", "r", d.direct.d.streamOut.n, "mg/l"),
          tr("P", "SS,bio", p_ssBio(N), "g/day"),
          tr("Q", "v", q_v, "m3/day")
        )

        /** Tính ratio N/ TN:
          * 1. Giả định = 0.5 ... */
        def calcNRatio(epsilon: Double, maxLoop: Int): Double = {
          @tailrec
          def calc(rmin: Double, rmax: Double, loop: Int): Double = {
            val len = rmax - rmin
            val r = (rmin + rmax) / 2
            val n0 = r * d.direct.d.streamIn.tkn
            val n = calcN(n0)
            if (loop >= maxLoop || Math.abs(n - n0) < epsilon) {
              r
            } else {
              val (rmin2, rmax2) = if (n > n0) (r, rmax) else (rmin, r)
              calc(rmin2, rmax2, loop + 1)
            }
          }
          calc(0, 1, 0)
        }

        val N = calcNRatio(.001, 30) * d.direct.d.streamIn.tkn

        val bod_ox = q_v * (s_v - s) - d.direct.relation.value.rCO2Decay * p_ssBio(N)
        val bod_ox_271a = dataTbl(
          tr("BOD", "ox", bod_ox, "g/day"),
          tr("Q", "v", q_v, "m3/day"),
          tr("S", "v", s_v, "mg/l"),
          tr("S", s, "mg/l"),
          tr("r", "O2,phanhuy", d.direct.relation.value.rCO2Decay, "g/g"),
          tr("P", "SS,bio", p_ssBio(N), "g/day")
        )

        val bod_ox_dnt = d.direct.relation.value.rBODDnt * N * q_v
        val bod_ox_dnt_271b = dataTbl(
          tr("BOD", "ox,dnt", bod_ox_dnt, "g/day"),
          tr("r", "BOD,dnt", d.direct.relation.value.rBODDnt, "g/g"),
          tr("N", N, "mg/l"),
          tr("Q", "v", q_v, "m3/day")
        )

        val bod_khuthuc = if (bod_ox < bod_ox_dnt) bod_ox else bod_ox - bod_ox_dnt

        val co2_khu_bod = d.direct.relation.value.yCO2 * bod_khuthuc
        val co2_khu_bod_271d = dataTbl(
          tr("CO2", "khu,BOD", co2_khu_bod, "g/day"),
          tr("Y", "CO2", d.direct.relation.value.yCO2),
          tr("BOD", "khuthuc", bod_khuthuc)
        )

        val vss_phanhuy = .85 * V * (ae.kd_ * X + nit.kd_ * x_nit(N))
        val vss_phanhuy_272a = dataTbl(
          tr("VSS", "phanhuy", vss_phanhuy, "g/day"),
          tr("V", V, "mg/l"),
          tr("X", X, "mg/l"),
          tr("X", "nit", x_nit(N), "mg/l"),
          tr("k", "d", ae.kd_, Day1),
          tr("k", "d,nit", nit.kd_, Day1)
        )

        val co2_phanhuy = d.direct.relation.value.yCO2Decay * vss_phanhuy
        val co2_phanhuy_272b = dataTbl(
          tr("CO", "2,VSSphanhuy", co2_phanhuy, "g/day"),
          tr("Y", "CO2,phanhuy", d.direct.relation.value.yCO2Decay),
          tr("VSS", "phanhuy", vss_phanhuy, "g/day")
        )

        Seq(
          <.h3("2. Đường biên 2 - Hệ xử lý hiếu khí"),
          <.h4("2.1. Tính toán nồng độ cơ chất trong bể hiếu khí"),
          <.div("Công thức tính: `S = (K_s * (1 + K_d * SRT))/(SRT * (Y * k - k_d) - 1)`".teX),
          p21S,

          <.h4("2.2. Tính toán nồng độ sinh khối dị dưỡng trong bể hiếu khí"),
          <.div("Công thức tính: `X = ((SRT) / (HRT) ) * ((Y * (S_v - S)) / (1 + K_d * SRT))`".teX),
          qv_21a,
          <.div("b. Tính `S_v = S_(o,v) - (BOD_(khu,bl)) / Q_v`".teX),
          sv_21b,
          <.div("c. Tính X"),
          x_21c,

          <.h4("2.3. Tính nồng độ Xnb"),
          <.div("Công thức tính: `X_(nb) = f_d * k_d * X * SRT + (X_(nb,v) *  SRT) / (HRT)`".teX),
          <.h5("a. Tính VSS vào bể sinh học"),
          <.div("Công thức tính: `SS_v = X_(o,v) - (SS_(khu,bl)) / Q_v`".teX),
          vss_23a,
          <.h5("b. Tính `X_(nb,v)`".teX),
          <.div(s"Công thức tính: `X_(nb,v) = VSS * (1 - (bpCOD) / (pCOD)) = $X_nbV`(mg/l)".teX),
          <.h5("b. Tính `X_(nb)`".teX),
          X_nb_23c,

          <.h4("2.4. Tính `X_(nit)`".teX),
          <.div("Công thức tính: `X_(nit) = ((SRT_(nit)) / (HRT) ) * ((Y_(nit) * N) / (1 + K_(d,nit) * SRT_(nit)))`".teX),
          <.h5("a. Tính `mu_(nit)`".teX),
          <.div("Công thức tính: `mu_(nit) = ( (mu_(m,nit) * N) / (K_N + N) ) * ( (DO) / (K_(DO) + DO) ) - k_(d,nit)`".teX),
          mu_24a(N),
          <.h5(s"b. Tính `SRT_(nit) = 1/mu_(nit) = ${srtNit(N)}`(day)".teX),
          <.h5(s"c. Tính `X_(nit)`".teX),
          x_nit_24c(N),

          <.h4("2.5. Tính lượng bùn tạo ra"),
          <.div("Công thức tính: `P_(SS) = (X_(tong,SS) * V) / (SRT) = P_(SS,BOD) + P_(SS,nit) + P_(SS,manhtebao) + P_(SS,nbVSS)`".teX),
          <.h5("a. Lượng bùn tạo ra do khử BOD"),
          <.div("Công thức tính: `P_(SS,BOD) = (X * V) / (SRT)`".teX),
          p_ssBod_25a,
          <.h5("b. Lượng bùn tạo ra do nitrat hóa"),
          <.div("Công thức tính: `P_(SS,nit) = (X_(nit) * V) / (SRT_(nit))`".teX),
          p_ssNit_25b(N),
          <.h5("c. Lượng bùn tạo ra do phân hủy mảnh vụn tế bào"),
          <.div("Công thức tính: `P_(SS,manhTeBao) = f_d * k_d * X * V`".teX),
          p_ssManhTeBao_25c,
          <.h5("d. Lượng bùn do sinh khối không phân hủy"),
          <.div("Công thức tính: `P_(SS,nbVSS) = (X_(nb,v) * V) / (HRT)`".teX),
          p_ssNbVss_25d,
          <.h5(s"e. Tổng lượng bùn sinh ra `P_(SS) = ${p_ss(N)}`(g/day)".teX),
          <.h5(s"f. Tổng lượng bùn sinh ra do phân hủy sinh học `P_(SS,bio) = P_(SS) - Q_v * X_(nb,VSS) = ${p_ssBio(N)}`(g/day)".teX),

          <.h4("2.6. Tính nồng độ Nito bị ô xi hóa trong bể xử lý sinh học"),
          <.div("Công thức tính: `N = TN_v - N_r - (0.12 * P_(SS,bio)) / Q_v`".teX),
          N_26(N),

          <.h4("2.7. Tính lượng khí CO2 sinh ra trong bể hiếu khí"),
          <.h5("2.7.1 Tính lượng CO2 sinh ra do khử BOD"),
          <.div("a. Lượng BOD bị oxi hóa"),
          <.div("Công thức tính: `BOD_(ox) = Q_v * (S_v - S) - r_(O2,phanhuy) * (P_(SS) - Q_v * X_(nb,v) )`".teX),
          bod_ox_271a,
          <.div("b. Lượng BOD bị khử trong quá trình denitrat hóa"),
          <.div("Công thức tính: `BOD_(o x,dnt) = r_(BOD,dnt) * N * Q_v`".teX),
          bod_ox_dnt_271b,
          <.div(s"c. Lượng BOD bị khử thực = $bod_khuthuc (g/day)"),
          <.div("d. Lượng phát thải CO2 do khử BOD"),
          <.div("Công thức tính: `CO_(2,khuBOD) = Y_(CO2) * (BOD_(o x) - BOD_(o x,dnt))`".teX),
          co2_khu_bod_271d,

          <.h5("2.7.2 Lượng CO2 do phân hủy nội bào sinh khối"),
          <.div("a. Lượng sinh khối  phân hủy nội bào"),
          <.div("Công thức tính: `VSS_(phanhuy) = 0.85 * V * (k_d * X + k_(d,nit) * X_(nit))`".teX),
          vss_phanhuy_272a,
          <.div("b. Lượng CO2 do phân hủy nội bào sinh khối"),
          <.div("Công thức tính: `CO_(2,phanhuy) = Y_(CO2,phanhuy) * VSS_(phanhuy)`".teX),
          co2_phanhuy_272b

        )
      }

      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp"),
        <.h4("1.1. Lượng BOD bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `BOD_(khu,bl) = Pr_(bl,BOD) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_1,
        <.h4("1.2. Lượng SS bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `SS_(khu,bl) = Pr_(bl,SS) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_2,
        p2Aerobic
      )
    }
  }

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
