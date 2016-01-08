package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.KineticCoefficientData.Nitrate

//import model.KineticCoefficientData.Aerobic
import tex.TeX._
import ghg.Utils._

object DirectPage {
  type Props = ModelProxy[GhgData]

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val d = P()

      val pPool = d.direct.d.primaryPool

      val s_ov = d.direct.d.streamIn.s
      val pr_blBod = pPool.prBOD
      val bod_khuBl = d.info.power * s_ov * pr_blBod
      def tbl1_1() = {
        table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("Q", "o,v"), <.td(d.info.power), <.td("m3/day")), //Công suất dòng vào ban đầu
          <.tr(td("S", "o,v"), <.td(s_ov), <.td("mg/l")), //Nồng độ `BOD_5` dòng vào ban đầu
          <.tr(td("Pr", "bl,BOD"), <.td(pr_blBod), <.td("%")), //Phần trăm khư `BOD_5` trong bể lắng sơ cấp

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("BOD", "khu,bl"), <.td(bod_khuBl), <.td("g/day")), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
          <.tr(td("BOD", "khu,bl"), <.td(bod_khuBl / 1000), <.td("kg/day")) //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (kg/day)
        )
      }

      val x_ov = d.direct.d.streamIn.tss
      val pr_blSs = pPool.prSS
      val ss_khuBl = d.info.power * x_ov * pr_blSs
      def tbl1_2() = {

        table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("Q", "o,v"), <.td(d.info.power), <.td("m3/day")), //Công suất dòng vào ban đầu
          <.tr(td("X", "o,v"), <.td(x_ov), <.td("mg/l")), //Nồng độ SS dòng vào ban đầu
          <.tr(td("Pr", "bl,SS"), <.td(pr_blSs), <.td("%")), //Phần trăm khư SS trong bể lắng sơ cấp

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("SS", "khu,bl"), <.td(ss_khuBl), <.td("g/day")), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          <.tr(td("SS", "khu,bl"), <.td(ss_khuBl / 1000), <.td("kg/day")) //Lượng SS bị khử trong bể lắng sơ cấp (kg/day)
        )
      }

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

        val p21S = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("K", "S"), <.td(ae.ks_), <.td("mg/l")),
          <.tr(td("k", "d"), <.td(ae.kd_), Ngay1),
          <.tr(<.td("Y"), <.td(ae.y_), <.td("mg/mg")),
          <.tr(<.td("k"), <.td(ae.k_), <.td()),
          <.tr(<.td("SRT"), <.td(pool.srt), <.td("day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(<.td("S"), <.td(s), <.td("mg/l"))
        )


        val q_v = d.info.power - pPool.q
        val qv_21a = <.div(s"a. Tính `Q_v = Q_(o,v) - Q_(bl) = ${d.info.power} - ${pPool.q} = $q_v`(m3/day)".teX)

        val s_v = d.direct.d.streamIn.s - bod_khuBl / q_v
        val sv_21b = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("S", "o,v"), <.td(d.direct.d.streamIn.s), <.td("mg/l")),
          <.tr(td("BOD", "khu,bl"), <.td(bod_khuBl), <.td("g/day")),
          <.tr(td("Q", "v"), <.td(q_v), <.td("m3/day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("S", "v"), <.td(s_v), <.td("mg/l"))
        )

        val X = pool.srt / pool.hrtDay * ae.y_ * (s_v - s) / (1 + ae.kd_ * pool.srt)
        val x_21c = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(<.td("SRT"), <.td(pool.srt), <.td("day")),
          <.tr(<.td("HRT"), <.td(pool.hrt), <.td("hour")),
          <.tr(td("S", "v"), <.td(s_v), <.td("mg/l")),
          <.tr(<.td("S"), <.td(s), <.td("mg/l")),
          <.tr(<.td("Y"), <.td(ae.y_), <.td("mg/mg")),
          <.tr(td("k", "d"), <.td(ae.kd_), Ngay1),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(<.td("X"), <.td(X), <.td("mg/l"))
        )

        val ss_v = x_ov - ss_khuBl / q_v
        val vss_v = 0.85 * ss_v
        val vss_23a = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("X", "o,v"), <.td(x_ov), <.td("mg/l")), //Nồng độ SS dòng vào ban đầu
          <.tr(td("SS", "khu,bl"), <.td(ss_khuBl), <.td("g/day")), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          <.tr(td("Q", "v"), <.td(q_v), <.td("m3/day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("SS", "v"), <.td(ss_v), <.td("mg/l")),
          <.tr(<.td("`VSS_v = 0.85 * SS_v`".teX), <.td(vss_v), <.td("mg/l"))
        )

        val X_nbV = vss_v * (1 - 60D/ 73)

        val X_nb = ae.fd * ae.kd_ *  X * pool.srt + X_nbV * pool.srt / pool.hrtDay
        val X_nb_23c = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("f", "d"), <.td(ae.fd), <.td()),
          <.tr(td("k", "d"), <.td(ae.kd_), <.td()),
          <.tr(<.td("X"), <.td(X), <.td("mg/l")),
          <.tr(td("X", "nb,v"), <.td(X_nbV), <.td("mg/l")),
          <.tr(<.td("SRT"), <.td(pool.srt), <.td("day")),
          <.tr(<.td("HRT"), <.td(pool.hrt), <.td("hour")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("X", "nb"), <.td(X_nb), <.td("mg/l"))
        )

        val nit = d.direct.coef.nitrate
        def m_nit(N: Double) = nit.m_ * N / (nit.kn_ + N) * Nitrate.DO / (Nitrate.Kdo + Nitrate.DO) - nit.kd_
        def mu_24a(N: Double) = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(<.td("TN"), <.td(d.direct.d.streamIn.tkn), <.td("mg/l")),
          <.tr(td("μ", "m,nit"), <.td(nit.m_), <.td()),
          <.tr(td("K", "N"), <.td(nit.kn_), <.td()),
          <.tr(td("k", "d,nit"), <.td(nit.kd_), Ngay1),
          <.tr(td("K", "DO"), <.td(Nitrate.Kdo), <.td()),
          <.tr(<.td("DO"), <.td(Nitrate.DO), <.td()),
          <.tr(<.td("N"), <.td(N), <.td("mg/l")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("μ", "nit"), <.td(m_nit(N)), Ngay1)
        )

        @inline def srtNit(N: Double) = 1 / m_nit(N)

        def Xnit(N: Double) = srtNit(N) / pool.hrtDay * nit.y_ * N / (1 + nit.kd_ * srtNit(N))

        def Xnit_24c(N: Double) = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("SRT", "nit"), <.td(srtNit(N)), <.td("day")),
          <.tr(<.td("HRT"), <.td(pool.hrt), <.td("hour")),
          <.tr(td("Y", "nit"), <.td(nit.y_), <.td()),
          <.tr(td("k", "d,nit"), <.td(nit.kd_), Ngay1),
          <.tr(<.td("N"), <.td(N), <.td("mg/l")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("X", "nit"), <.td(Xnit(N)), <.td("mg/l"))
        )

        val V = pool.hrtDay * q_v
        val p_ssBod = X * V / pool.srt
        val p_ssBod_25a = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(<.td("X"), <.td(X), <.td("mg/l")),
          <.tr(<.td("V"), <.td(V), <.td("mg/l")),
          <.tr(<.td("SRT"), <.td(pool.srt), <.td("day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("P", "SS,BOD"), <.td(p_ssBod), <.td("g/day"))
        )

        def p_ssNit(N: Double) = Xnit(N) * V / srtNit(N)

        def p_ssNit_25b(N: Double) = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("X", "nit"), <.td(Xnit(N)), <.td("mg/l")),
          <.tr(<.td("V"), <.td(V), <.td("mg/l")),
          <.tr(td("SRT", "nit"), <.td(srtNit(N)), <.td("day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("P", "SS,nit"), <.td(p_ssNit(N)), <.td("g/day"))
        )

        val p_ssManhTeBao = ae.fd * ae.kd_ * X * V
        val p_ssManhTeBao_25c = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(<.td("X"), <.td(X), <.td("mg/l")),
          <.tr(<.td("V"), <.td(V), <.td("mg/l")),
          <.tr(td("f", "d"), <.td(ae.fd), <.td()),
          <.tr(td("k", "d"), <.td(ae.kd_), Ngay1),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("P", "SS,manhTeBao"), <.td(p_ssManhTeBao), <.td("g/day"))
        )

        val p_ssNbVss = X_nbV * V / pool.hrtDay
        val p_ssNbVss_25d = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(td("X", "nb,v"), <.td(X_nbV), <.td("mg/l")),
          <.tr(<.td("V"), <.td(V), <.td("mg/l")),
          <.tr(<.td("HRT"), <.td(pool.hrt), <.td("hour")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(td("P", "SS,nbVSS"), <.td(p_ssNbVss), <.td("g/day"))
        )

        def p_ss(N: Double) = p_ssBod + p_ssNit(N) + p_ssManhTeBao + p_ssNbVss
        def p_ssBio(N: Double) = p_ss(N) - p_ssNbVss //=..

        def calcN(N: Double) = d.direct.d.streamIn.tkn - d.direct.d.streamOut.n - .12 * p_ssBio(N) / q_v
        def N_26(N: Double) = table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          <.tr(<.td("TN"), <.td(d.direct.d.streamIn.tkn), <.td("mg/l")),
          <.tr(td("N", "r"), <.td(d.direct.d.streamOut.n), <.td("mg/l")),
          <.tr(td("P", "SS,bio"), <.td(p_ssBio(N)), <.td("g/day")),
          <.tr(td("Q", "v"), <.td(q_v), <.td("m3/day")),

          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          <.tr(<.td("N"), <.td(calcN(N)), <.td("mg/l"))
        )

        val N = 24.2284

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
          Xnit_24c(N),

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
          N_26(N)
        )
      }

      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp"),
        <.h4("1.1. Lượng BOD bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `BOD_(khu,bl) = Pr_(bl,BOD) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_1(),
        <.h4("1.2. Lượng SS bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `SS_(khu,bl) = Pr_(bl,SS) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_2(),
        p2Aerobic
      )
    }
  }

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
