package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{Bien2Output, GhgData}
import model.KineticCoefficientData.Nitrate
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
      val b1 = d.bien1

      val pPool = d.direct.d.primaryPool
      val aPool = d.direct.d.anaerobicPool.get //FIXME
      val relation = d.direct.relation.value

      val p2Aerobic = d.direct.d.aerobicPool ? { pool =>
        val b2 = d.bien2
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
            tr("X", "o,v", b1.x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
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
          <.h5("a. Tính `mu_(nit)`".teX),
          <.div("Công thức tính: `mu_(nit) = ( (mu_(m,nit) * N) / (K_N + N) ) * ( (DO) / (K_(DO) + DO) ) - k_(d,nit)`".teX),
          dataTbl(
            tr("μ", "nit", b2.m_nit, Day1),
            tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
            tr("μ", "m,nit", nit.m_),
            tr("K", "N", nit.kn_),
            tr("k", "d,nit", nit.kd_, Day1),
            tr("K", "DO", Nitrate.Kdo),
            tr("DO", Nitrate.DO),
            tr("N", b2.N, "mg/l")
          ),
          <.h5(s"b. Tính `SRT_(nit) = 1/mu_(nit) = ${b2.srtNit}`(day)".teX),
          <.h5(s"c. Tính `X_(nit)`".teX),
          dataTbl(
            tr("X", "nit", b2.x_nit, "mg/l"),
            tr("SRT", "nit", b2.srtNit, "day"),
            tr("HRT", pool.hrt, "hour"),
            tr("Y", "nit", nit.y_),
            tr("k", "d,nit", nit.kd_, Day1),
            tr("N", b2.N, "mg/l")
          ),

          <.h4("2.5. Tính lượng bùn tạo ra"),
          <.div("Công thức tính: `P_(SS) = (X_(tong,SS) * V) / (SRT) = P_(SS,BOD) + P_(SS,nit) + P_(SS,manhtebao) + P_(SS,nbVSS)`".teX),
          <.h5("a. Lượng bùn tạo ra do khử BOD"),
          <.div("Công thức tính: `P_(SS,BOD) = (X * V) / (SRT)`".teX),
          dataTbl(
            tr("P", "SS,BOD", b2.p_ssBod, "g/day"),
            tr("X", b2.X, "mg/l"),
            tr("V", b2.V, "mg/l"),
            tr("SRT", pool.srt, "day")
          ),
          <.h5("b. Lượng bùn tạo ra do nitrat hóa"),
          <.div("Công thức tính: `P_(SS,nit) = (X_(nit) * V) / (SRT_(nit))`".teX),
          dataTbl(
            tr("P", "SS,nit", b2.p_ssNit, "g/day"),
            tr("X", "nit", b2.x_nit, "mg/l"),
            tr("V", b2.V, "mg/l"),
            tr("SRT", "nit", b2.srtNit, "day")
          ),
          <.h5("c. Lượng bùn tạo ra do phân hủy mảnh vụn tế bào"),
          <.div("Công thức tính: `P_(SS,manhTeBao) = f_d * k_d * X * V`".teX),
          dataTbl(
            tr("P", "SS,manhTeBao", b2.p_ssManhTeBao, "g/day"),
            tr("X", b2.X, "mg/l"),
            tr("V", b2.V, "mg/l"),
            tr("f", "d", ae.fd),
            tr("k", "d", ae.kd_, Day1)
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

          <.h4("2.6. Tính nồng độ Nito bị ô xi hóa trong bể xử lý sinh học"),
          <.div("Công thức tính: `N = TN_v - N_r - (0.12 * P_(SS,bio)) / Q_v`".teX),
          dataTbl(
            tr("N", b2.Ncalc, "mg/l"),
            tr("TN", d.direct.d.streamIn.tkn, "mg/l"),
            tr("N", "r", d.direct.d.streamOut.n, "mg/l"),
            tr("P", "SS,bio", b2.p_ssBio, "g/day"),
            tr("Q", "v", b2.q_v, "m3/day")
          ),

          <.h4("2.7. Tính lượng khí CO2 sinh ra trong bể hiếu khí"),
          <.h5("2.7.1 Tính lượng CO2 sinh ra do khử BOD"),
          <.div("a. Lượng BOD bị oxi hóa"),
          <.div("Công thức tính: `BOD_(ox) = Q_v * (S_v - S) - r_(O2,phanhuy) * (P_(SS) - Q_v * X_(nb,v) )`".teX),
          dataTbl(
            tr("BOD", "ox", b2.bod_ox, "g/day"),
            tr("Q", "v", b2.q_v, "m3/day"),
            tr("S", "v", b2.s_v, "mg/l"),
            tr("S", b2.s, "mg/l"),
            tr("r", "O2,phanhuy", relation.rCO2Decay, "g/g"),
            tr("P", "SS,bio", b2.p_ssBio, "g/day")
          ),
          <.div("b. Lượng BOD bị khử trong quá trình denitrat hóa"),
          <.div("Công thức tính: `BOD_(o x,dnt) = r_(BOD,dnt) * N * Q_v`".teX),
          dataTbl(
            tr("BOD", "ox,dnt", b2.bod_ox_dnt, "g/day"),
            tr("r", "BOD,dnt", relation.rBODDnt, "g/g"),
            tr("N", b2.N, "mg/l"),
            tr("Q", "v", b2.q_v, "m3/day")
          ),
          <.div(s"c. Lượng BOD bị khử thực = ${b2.bod_khuThuc} (g/day)"),
          <.div("d. Lượng phát thải CO2 do khử BOD"),
          <.div("Công thức tính: `CO_(2,khuBOD) = Y_(CO2) * (BOD_(o x) - BOD_(o x,dnt))`".teX),
          dataTbl(
            tr("CO2", "khu,BOD", b2.co2_khu_bod, "g/day"),
            tr("Y", "CO2", relation.yCO2),
            tr("BOD", "khuthuc", b2.bod_khuThuc)
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
          <.div("Công thức tính: `CO_(2,phanhuy) = Y_(CO2,phanhuy) * VSS_(phanhuy)`".teX),
          dataTbl(
            tr("CO", "2,VSSphanhuy", b2.co2_phanhuy, "g/day"),
            tr("Y", "CO2,phanhuy", relation.yCO2Decay),
            tr("VSS", "phanhuy", b2.vss_phanhuy, "g/day")
          ),

          <.h5("2.7.3 Lượng khí CO2 do khử nito"),
          <.div("Công thức tính: `CO_(2,dnt) = Y_(CO2,dnt) * N * Q_v`".teX),
          dataTbl(
            tr("CO", "2,dnt", b2.co2_dnt, "g/day"),
            tr("Y", "CO2,dnt", relation.yCO2Dnt),
            tr("N", b2.N),
            tr("Q", "v", b2.q_v, "m3/day")
          ),

          <.h5("2.7.4 Lượng CO2 tiêu thụ do nitrat hóa"),
          <.div("Công thức tính: `CO_(2,tieuThuNit) = r_(CO2,nit) * N * Q_v`".teX),
          dataTbl(
            tr("CO", "2,tieuThuNit", b2.co2_tieuThuNit, "g/day"),
            tr("r", "CO2,nit", relation.rCO2Nit),
            tr("N", b2.N),
            tr("Q", "v", b2.q_v, "m3/day")
          ),

          <.h5("2.7.5 Lượng CO2 phát sinh trong quá trình hiếu khí "),
          <.div(
            f"""Công thức tính: `CO_(2,quaTri nhHieuKhi)
               = CO_(2,khuBOD) + CO_(2,VSSphanHuy) + CO_(2,dnt) - CO_(2,tieuThuNit)
               = ${b2.co2_quaTrinhHieuKhi}%,.2f`(g/day)""".teX),

          <.h5("2.7.6 Lượng khí N2O phát sinh trong quá trình nitrat và khử nitrat"),
          <.div("Công thức tính: `N_2O = Q_v * TN_v * R_(N_2O)`".teX),
          dataTbl(2,
            tr(<.span("N", <.sub("2"), "O"), b2.n2o, "g/day"),
            tr("CO", "2,N2OphatThai", b2.co2_n2o, "g/day"),
            tr("Q", "v", b2.q_v, "m3/day"),
            tr("TN", "v", d.direct.d.streamIn.tkn, "mg/l"),
            tr("R", "N2O", Bien2Output.r_n2o)
          )
        )
      }

      val b2 = d.bien2 //fixme anaerobic
      val b3 = d.bien3
      val ane = d.direct.coef.anaerobic
      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp"),
        <.h4("1.1. Lượng BOD bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `BOD_(khu,bl) = Pr_(bl,BOD) * Q_(o,v) * S_(o,v)`".teX),
        dataTbl(
          tr("BOD", "khu,bl", b1.bod_khuBl, "g/day"), //Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
          tr("S", "o,v", b1.s_ov, "mg/l"), //Nồng độ `BOD_5` dòng vào ban đầu
          tr("Pr", "bl,BOD", b1.pr_blBod, "%") //Phần trăm khư `BOD_5` trong bể lắng sơ cấp
        ),
        <.h4("1.2. Lượng SS bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `SS_(khu,bl) = Pr_(bl,SS) * Q_(o,v) * S_(o,v)`".teX),
        dataTbl(
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
          tr("Q", "o,v", d.info.power, "m3/day"), //Công suất dòng vào ban đầu
          tr("X", "o,v", b1.x_ov, "mg/l"), //Nồng độ SS dòng vào ban đầu
          tr("Pr", "bl,SS", b1.pr_blSs, "%") //Phần trăm khư SS trong bể lắng sơ cấp
        ),
        p2Aerobic,

        <.h3("3. Bể phân hủy yếm khí"),
        <.h4("3.1. Tổng lượng bùn vào bể phân hủy"),
        <.div("Công thức tính: `P_(VSS,dr) = SS_(khu,bl) + P_(SS)`".teX),
        dataTbl(
          tr("P", "VSS,dr", b3.p_vss_dr, "g/day"),
          tr("SS", "khu,bl", b1.ss_khuBl, "g/day"),
          tr("P", "SS", d.p_ss, "g/day")
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
          tr("SRT", "dr", aPool.srt, "day")
        ),

        <.h4("3.5. Tổng lượng bùn sinh học trong bể phân hủy"),
        <.h5("a. Tính lượng bùn do khử BOD trong bể phân hủy"),
        <.div("Công thức tính: `S_(SS,BOD,dr) = (Q_(v,dr) * Y_(dr) * (S_(v,dr) - S_(dr))) / (1 + k_(d,dr) * SRT_(dr))`".teX),
        dataTbl(
          tr("P", "SS,BOD,dr", b3.p_ssBodDr, "g/day"),
          tr("Y", "dr", ane.y, "mg/mg"),
          tr("k", "d,dr", ane.kd, Day1),
          tr("SRT", "dr", aPool.srt, "day"),
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
          tr("SRT", "dr", aPool.srt, "day"),
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
        <.h5(s"b.2. Lượng khí CH4 thu hồi = ${b3.ch4_panHuy_thuHoi}(g/day)"),
        <.h5(s"b.3. Lượng khí CH4 rò rỉ = ${b3.ch4_panHuy_roRi}(g/day)"),
        <.h5("b.4. Lượng khí CO2 tương đương của khí CH4"),
        <.div(s"Công thức tính: `C0_(2,phanHuyMetan) = Y_(CH_4,dot) * CH_(4,phanHuyThuHoi) + 25 * CH_(4,phanHuyRoRi) = ${b3.co2_phanHuyMetan}`(g/day)".teX),
        <.h5("c. Tổng lượng KNK phát sinh từ bể phân hủy yếm khí"),
        table(
          tr("CO", "2,bePhanHuy", b3.co2_bePhanHuy, "g/day"),
          tr("CO", "2,phanHuyMetan", b3.co2_phanHuyMetan, "g/day"),
          tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal, "g/day"),
          tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal / 1000, "kg/day")
        ),
        <.h3("4. Tổng lượng KNK phát sinh từ hệ xử lý hiếu khí"),
        table(
          tr("CO", "2,quaTrinhHieuKhi", b2.co2_quaTrinhHieuKhi, "g/day"),
          tr("CO", "2,N2OphatThai", b2.co2_n2o, "g/day"),
          tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct / 1000, "kg/day")
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
