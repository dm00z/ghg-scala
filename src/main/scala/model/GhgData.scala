package model

import model.KineticCoefficientData.Nitrate
//import model.RungeKutta

import scala.annotation.tailrec

case class R[T](min: T, max: T) {
  def text = s"$min - $max"
}
case class RNorm(range: R[Double], norm: Double)

case class GhgData(info: InfoData, indirect: IndirectData, direct: DirectData) {
  def electricPower: Double = {
    import ElectricData.CalcMethod._, indirect.electric._
    method match {
      case Method1 => info.power * _1.ratio
      case Method2 => _2.power
      case Method3 => _3.power
    }
  }

  def ghgElectric = electricPower * indirect.electric.powerStruct.totalRatio / 1000 //kg

  lazy val bien1: Bien1Output = {
    val pPool = direct.d.primaryPool

    val ss_ov = direct.d.streamIn.sso
    val pr_blSs = pPool.prSS
    val ss_khuBl = info.power * ss_ov * pr_blSs

    val s_ov = direct.d.streamIn.so
    val pr_blBod = pPool.prBOD
    val bod_khuBl = info.power * s_ov * pr_blBod

    Bien1Output(
      ss_ov,
      pr_blBod,
      bod_khuBl,
      s_ov,
      pr_blSs,
      ss_khuBl
    )
  }

  // UNUSED ------------------------------------------------------------------------------------

  lazy val bien2Ana: Bien2Output = {
    val pool = direct.d.anaerobicPool.get
    val ef = direct.coef.anaerobic

    val b1 = bien1
    val s = ef.ks(ef.t_an) * (1 + ef.kd * pool.srt) / (pool.srt * (ef.y * ef.k(ef.t_an) - ef.kd) - 1)

    val pPool = direct.d.primaryPool
    val q_v = info.power - pPool.q

    //val s_ov = b1.s_ov
    val s_v = direct.d.streamIn.so - b1.bod_khuBl / q_v

    val X = pool.srt / pool.hrtDay * ef.y * (s_v - s) / (1 + ef.kd * pool.srt)

    val ss_v = b1.ss_ov - b1.ss_khuBl / q_v
    val vss_v = 0.85 * ss_v

    val X_nbV = 0 //vss_v * (1 - 60D/ 73)
    val X_nb = ef.fd * ef.kd *  X * pool.srt + X_nbV * pool.srt / pool.hrtDay


    //val V = pool.hrtDay * q_v
    val V = pool.v
    val p_XBod = X * V / pool.srt

    val p_ssManhTeBao = ef.fd * ef.kd * X * V
    val p_ssNbVss = X_nbV * V / pool.hrtDay

    val nit = direct.coef.nitrate
    def m_nit(N: Double) = nit.m_ * N / (nit.kn_ + N) * Nitrate.DO / (Nitrate.Kdo + Nitrate.DO) - nit.kd_

    //@inline def srtNit(N: Double) = 1 / m_nit(N)
    def srtNit = pool.srt

    def x_nit(N: Double) = (srtNit / pool.hrtDay) * (nit.y_ * N) / (1 + nit.kd_ * srtNit)
    def p_ssNit(N: Double) = x_nit(N) * V / srtNit
    def p_ss(N: Double) =p_XBod + p_ssNit(N) + p_ssManhTeBao + p_ssNbVss
    def p_ssBio(N: Double) = p_ss(N) - p_ssNbVss //=..
    def calcN(N: Double) = direct.d.streamIn.n - direct.d.streamOut.n - .12 * p_ssBio(N) / q_v

    /** Tính ratio N/ TN */
    def calcNRatio(epsilon: Double, maxLoop: Int): Double = {
      @tailrec
      def calc(rmin: Double, rmax: Double, loop: Int): Double = {
        val len = rmax - rmin
        val r = (rmin + rmax) / 2
        val n0 = r * direct.d.streamIn.n
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

    val N = calcNRatio(.001, 1000) * direct.d.streamIn.n

    val relation = direct.relation
    //val bod_ox = relation.yCO2 * (q_v* (s_v - s) - relation.rCO2Decay * p_ssBio(N))
    val bod_ox = relation.yCO2 * (q_v* (s_v - s) - relation.rCO2Decay * p_XBod)
    val bod_ox_dnt = relation.rBODDnt * N * q_v
    val bod_khuThuc = if (bod_ox < bod_ox_dnt) bod_ox else bod_ox - bod_ox_dnt
    val co2_khu_bod = relation.yCO2 * bod_khuThuc
    val vss_phanhuy = .85 * V * ef.kd * X
    val co2_phanhuy = relation.yCO2Decay * vss_phanhuy
    val co2_dnt =  relation.yCO2Dnt * N * q_v
    val co2_tieuThuNit = relation.rCO2Nit * N * q_v

    val co2_quaTrinhHieuKhi = bod_ox + co2_phanhuy + co2_dnt - co2_tieuThuNit
    val n2o = q_v * direct.d.streamIn.n * Bien2Output.r_n2o
    val co2_n2o = 296 * n2o //fixme hardcode

    val tyLePhatThai = co2_quaTrinhHieuKhi / q_v

    Bien2Output(s, q_v, s_v, X, ss_v, vss_v, X_nbV, X_nb, V, p_XBod, p_ssManhTeBao, p_ssNbVss,
      N, calcN(N), m_nit(N), srtNit, x_nit(N), p_ssNit(N), p_ssBio(N), p_ss(N),
      bod_ox, bod_ox_dnt, bod_khuThuc, co2_khu_bod, vss_phanhuy, co2_phanhuy, co2_dnt, co2_tieuThuNit,
      co2_quaTrinhHieuKhi, n2o, co2_n2o, 0, tyLePhatThai)
  }

  /** required: direct.d.aerobicPool.isDefined */
  lazy val bien2Ae: Bien2Output = {
    val pool = direct.d.aerobicPool.get
    val ef = direct.coef.aerobic

    val b1 = bien1
    val s = ef.ks_ * (1 + ef.kd_ * pool.srt) / (pool.srt * (ef.y_ * ef.k_ - ef.kd_) - 1)

    val pPool = direct.d.primaryPool
    val q_v = info.power - pPool.q

    //val s_ov = b1.s_ov
    val s_v = direct.d.streamIn.so - b1.bod_khuBl / q_v

    val X = (pool.srt / pool.hrt) * (ef.y_ * (s_v - s)) / (1 + ef.kd_ * pool.srt)

    val ss_v = b1.ss_ov - b1.ss_khuBl / q_v
    val vss_v = 0.85 * ss_v

    val X_nbV = 0 //vss_v * (1 - 60D/ 73)
    val X_nb = ef.fd * ef.kd_ *  X * pool.srt + X_nbV * pool.srt / pool.hrtDay


    //val V = pool.hrtDay * q_v
    val V = pool.v
    val p_XBod = X * V / pool.srt

    val p_ssManhTeBao = ef.fd * ef.kd_ * X * V
    val p_ssNbVss = X_nbV * V / pool.hrtDay

    val nit = direct.coef.nitrate
    def m_nit(N: Double) = nit.m_ * N / (nit.kn_ + N) * Nitrate.DO / (Nitrate.Kdo + Nitrate.DO) - nit.kd_

    //@inline def srtNit(N: Double) = 1 / m_nit(N)
    def srtNit = pool.srt

    def x_nit(N: Double) = (srtNit / pool.hrtDay) * (nit.y_ * N) / (1 + nit.kd_ * srtNit)
    def p_ssNit(N: Double) = x_nit(N) * V / srtNit
    def p_ss(N: Double) =p_XBod + p_ssNit(N) + p_ssManhTeBao + p_ssNbVss
    def p_ssBio(N: Double) = p_ss(N) - p_ssNbVss //=..
    def calcN(N: Double) = direct.d.streamIn.n - direct.d.streamOut.n - .12 * p_ssBio(N) / q_v

    /** Tính ratio N/ TN */
    def calcNRatio(epsilon: Double, maxLoop: Int): Double = {
      @tailrec
      def calc(rmin: Double, rmax: Double, loop: Int): Double = {
        val len = rmax - rmin
        val r = (rmin + rmax) / 2
        val n0 = r * direct.d.streamIn.n
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

    val N = calcNRatio(.001, 1000) * direct.d.streamIn.n

    val relation = direct.relation
    //val bod_ox = relation.yCO2 * (q_v* (s_v - s) - relation.rCO2Decay * p_ssBio(N))
    val bod_ox = relation.yCO2 * (q_v* (s_v - s) - relation.rCO2Decay * p_XBod)
    val bod_ox_dnt = relation.rBODDnt * N * q_v
    val bod_khuThuc = if (bod_ox < bod_ox_dnt) bod_ox else bod_ox - bod_ox_dnt
    val co2_khu_bod = relation.yCO2 * bod_khuThuc
    val vss_phanhuy = .85 * V * (ef.kd_ * X + nit.kd_ * x_nit(N))
    val co2_phanhuy = relation.yCO2Decay * vss_phanhuy
    val co2_dnt =  relation.yCO2Dnt * N * q_v
    val co2_tieuThuNit = relation.rCO2Nit * N * q_v

    val co2_quaTrinhHieuKhi = bod_ox + co2_phanhuy + co2_dnt - co2_tieuThuNit
    val n2o = q_v * direct.d.streamIn.n * Bien2Output.r_n2o
    val co2_n2o = 296 * n2o //fixme hardcode

    val tyLePhatThai = co2_quaTrinhHieuKhi / q_v

    Bien2Output(s, q_v, s_v, X, ss_v, vss_v, X_nbV, X_nb, V, p_XBod, p_ssManhTeBao, p_ssNbVss,
      N, calcN(N), m_nit(N), srtNit, x_nit(N), p_ssNit(N), p_ssBio(N), p_ss(N),
      bod_ox, bod_ox_dnt, bod_khuThuc, co2_khu_bod, vss_phanhuy, co2_phanhuy, co2_dnt, co2_tieuThuNit,
      co2_quaTrinhHieuKhi, n2o, co2_n2o, 0, tyLePhatThai)
  }

  lazy val bien3: Bien3Output = calcBien3(true)
  lazy val bien3Ana: Bien3Output = calcBien3(false)

  private def calcBien3(isAerobic: Boolean): Bien3Output = {
    val b1 = bien1
    val b2 = bien2Ae
    val relation = direct.relation

    val p_vss_dr = b1.ss_khuBl + b2.p_ss

    val pool = if (isAerobic) direct.d.aerobicPool.get else direct.d.anaerobicPool.get

//    val q_xa_ae = direct.d.aerobicPool.fold(0D)(p => p.qxRatio * b2.q_v)
//    val q_xa_ane = 0 //fixme
//    val q_xa = q_xa_ae + q_xa_ane
    val q_xa = pool.qxRatio * b2.q_v

    val pPool = direct.d.primaryPool
    val q_v_dr = pPool.q + q_xa

    val s_v_dr = (b1.bod_khuBl + q_xa * b2.s) / q_v_dr

    val ane = direct.coef.anaerobic
    val dPool = direct.d.decayPool
    val s_dr = ane.ks(ane.t_dr) * (1 + ane.kd * dPool.srt) / (dPool.srt * (ane.y * ane.k(ane.t_dr) - ane.kd) - 1)

    val p_ssBodDr = if (s_v_dr < s_dr) 0D else q_v_dr * ane.y * (s_v_dr - s_dr) / (1 + ane.kd * dPool.srt)

    val p_ssManhTeBaoDr = ane.fd * ane.kd * dPool.srt * p_ssBodDr

    val p_ssBioDr = p_ssBodDr + p_ssManhTeBaoDr

    val bod_khu_dr = q_v_dr * (s_v_dr - s_dr) - 1.42 * p_ssBioDr match {
      case x if x < 0 => 0
      case x => x
    }

    val x_dr = (p_ssBodDr + p_vss_dr) / q_v_dr

    val v_dr = dPool.hrtDay * q_v_dr

    val vss_decay_dr = v_dr * ane.kd * x_dr
    val co2_bePhanHuy = relation.yCO2Dr * bod_khu_dr + relation.yCO2DrDecay * vss_decay_dr
    val ch4_bePhanHuy = relation.yCH4Dr * bod_khu_dr + relation.yCH4DrDecay * vss_decay_dr
    val ch4_phanHuy_thuHoi = .95 * (ch4_bePhanHuy + b2.ch4_beYemKhi)
    val ch4_phanHuy_roRi = .5 * (ch4_bePhanHuy + b2.ch4_beYemKhi)
    val co2_phanHuyMetan = relation.yCH4Combusion * ch4_phanHuy_thuHoi + 25 * ch4_phanHuy_roRi
    val co2_phanHuyTotal = co2_bePhanHuy + co2_phanHuyMetan

    val knk_direct = b2.co2_quaTrinh + b2.co2_n2o + co2_phanHuyTotal

    val m_CO2BODra = (relation.yCO2 + relation.yVSS * relation.yCO2Decay) * b2.s * b2.q_v

    Bien3Output(
      p_vss_dr, q_xa, q_v_dr, s_v_dr, s_dr, p_ssBodDr, p_ssManhTeBaoDr, p_ssBioDr, bod_khu_dr, x_dr,
      v_dr, vss_decay_dr, co2_bePhanHuy, ch4_bePhanHuy, ch4_phanHuy_thuHoi, ch4_phanHuy_roRi,
      co2_phanHuyMetan, co2_phanHuyTotal, knk_direct, m_CO2BODra)
  }

  lazy val bien4: Bien4Output = {
    val b2 = bien2Ae
    val relation = direct.relation
    val ef = direct.coef.aerobic

    val bun_phanHuy = 0.6 * b2.p_XBod
    val co2_phatThai = relation.yCO2DrDecay * bun_phanHuy
    val ch4_phatThai = relation.yCH4DrDecay * bun_phanHuy


    val ch4_phanHuyThuGom = ch4_phatThai
    val ch4_phanHuyRoRi = ch4_phanHuyThuGom

    val y_ch4dot = relation.yCH4Combusion
    val ch4_phanHuyThuHoi = ch4_phanHuyThuGom - ch4_phanHuyRoRi
    val co2_phanHuyMetan = y_ch4dot * ch4_phanHuyThuHoi + 23 * ch4_phanHuyRoRi

    val Pco2_bunPhanHuy = co2_phatThai + co2_phanHuyMetan

    //-------------NEW
    val P_bunSinhHoc = b2.p_XBio * 0.6

    val M_co2PhanHuy = P_bunSinhHoc * relation.yCO2DrDecay

    val M_ch4PhanHuy = P_bunSinhHoc * relation.yCH4DrDecay

    val M_co2tdPhanHuyPhongKhong = M_co2PhanHuy + M_ch4PhanHuy * 25

    val Pr_ch4_roRi = 0.05
    val M_ch4PhanHuyRoRi = Pr_ch4_roRi * M_ch4PhanHuy

    val M_ch4PhanHuyThuHoi = M_ch4PhanHuy - M_ch4PhanHuyRoRi

    val M_co2tdPhanHuyMetan = relation.yCH4Combusion * M_ch4PhanHuyThuHoi + 25 * M_ch4PhanHuyRoRi

    val M_co2tdPhanHuyDot = M_co2PhanHuy + M_co2tdPhanHuyMetan

    Bien4Output(bun_phanHuy, co2_phatThai, ch4_phatThai, ch4_phanHuyRoRi, Pr_ch4_roRi, ch4_phanHuyThuGom, y_ch4dot,
    ch4_phanHuyThuHoi, co2_phanHuyMetan, Pco2_bunPhanHuy, P_bunSinhHoc, M_co2PhanHuy, M_ch4PhanHuy, M_co2tdPhanHuyPhongKhong,
    M_ch4PhanHuyRoRi, M_ch4PhanHuyThuHoi, M_co2tdPhanHuyMetan, M_co2tdPhanHuyDot)
  }

  lazy val bien5: Bien5Output = {
    val b2 = bien2Ae
    val b3 = bien3
    val b4 = bien4

    val co2_tongPhatThai = b2.co2_quaTrinh + b3.m_CO2BODra + b4.Pco2_bunPhanHuy

    val N_dongra = direct.d.streamOut.n * direct.d.primaryPool.qv
    val EF_dongra = 0.01
    val phatThaiGianTiep_n2o = N_dongra * EF_dongra * 44f/28f

    val pop = math.round(info.power/0.12)
    val T_hlxlnt = 0.9
    val EF_hlxlnt = 3.2/365
    val F_IND_COM = 1.25
    val phatThaiTrucTiep_n2o = pop * T_hlxlnt * EF_hlxlnt * F_IND_COM

    val tongPhatThai_n2o = phatThaiGianTiep_n2o + phatThaiTrucTiep_n2o
    val phatThai_co2td_n2o = tongPhatThai_n2o * 296

    Bien5Output(co2_tongPhatThai, N_dongra, EF_dongra, phatThaiGianTiep_n2o, pop, T_hlxlnt, EF_hlxlnt, F_IND_COM, phatThaiTrucTiep_n2o, tongPhatThai_n2o, phatThai_co2td_n2o)
  }

  lazy val bien6: Bien6Output = {
    val relation = direct.relation
    val pPool = direct.d.primaryPool

    val n_dongRa = info.power * direct.d.streamOut.n
    val ef_dongRa = .005
    val n2o_phatThaiGianTiep = n_dongRa * ef_dongRa * 44/28

    val soDanSo_p = info.power / .2
    val ef_htxlnt = 3.2/365
    val cf = 1.14
    val n2o_phatThaiTrucTiep = soDanSo_p * ef_htxlnt * cf

    val n2o_tongPhatThai = n2o_phatThaiGianTiep + n2o_phatThaiTrucTiep
    val co2_tuongDuongNito = n2o_tongPhatThai * 296

    Bien6Output(n_dongRa, ef_dongRa, n2o_phatThaiGianTiep, soDanSo_p, ef_htxlnt, cf, n2o_phatThaiTrucTiep,
      n2o_tongPhatThai, co2_tuongDuongNito)
  }

  lazy val retrieveResult: retrieveResultOutput = {
    val b2 = bien2Ae
    val b4 = bien4
    val b3 = bien3
    val b5 = bien5

    val elecPower = ghgElectric // to be changed

    val M_co2_quaTrinh = b2.co2_quaTrinh/1000
    val M_co2tdPhanHuyDot = b4.M_co2tdPhanHuyDot/1000
    val M_co2BODra = b3.m_CO2BODra/1000
    val phatThai_co2td_n2o = b5.phatThai_co2td_n2o/1000

    val sumKNKByElecPower = elecPower
    val sumKNKByWasteDisposal = (M_co2_quaTrinh + M_co2tdPhanHuyDot + M_co2BODra + phatThai_co2td_n2o)

    val tyle_co2_quaTrinh = M_co2_quaTrinh/sumKNKByWasteDisposal
    val tyle_co2tdPhanHuyDot = M_co2tdPhanHuyDot/sumKNKByWasteDisposal
    val tyle_co2BODra = M_co2BODra/sumKNKByWasteDisposal
    val tyle_phatThai_co2td_n2o = phatThai_co2td_n2o/sumKNKByWasteDisposal

    val sumKNKAll = (sumKNKByElecPower + sumKNKByWasteDisposal)
    val tyle_elecPower = sumKNKByElecPower/sumKNKAll
    val tyle_wasteDisposal = sumKNKByWasteDisposal/sumKNKAll

    retrieveResultOutput(elecPower, M_co2_quaTrinh, M_co2tdPhanHuyDot, M_co2BODra, phatThai_co2td_n2o, sumKNKByElecPower,
      sumKNKByWasteDisposal, tyle_co2_quaTrinh, tyle_co2tdPhanHuyDot, tyle_co2BODra, tyle_phatThai_co2td_n2o, sumKNKAll,
      tyle_elecPower, tyle_wasteDisposal)
  }

  lazy val releaseResult = {
    val b2 = bien2Ae
    val b3 = bien3
    val b4 = bien4
    val b5 = bien5


    val elecPower = ghgElectric // to be changed

    val M_co2_quaTrinh = b2.co2_quaTrinh/1000
    val M_co2tdPhanHuyPhongKhong = b4.M_co2tdPhanHuyPhongKhong/1000
    val M_co2BODra = b3.m_CO2BODra/1000
    val phatThai_co2td_n2o = b5.phatThai_co2td_n2o/1000

    val sumKNKByElecPower = elecPower
    val sumKNKByWasteDisposal = (M_co2_quaTrinh + M_co2tdPhanHuyPhongKhong + M_co2BODra + phatThai_co2td_n2o)

    val tyle_co2_quaTrinh = M_co2_quaTrinh/sumKNKByWasteDisposal
    val tyle_co2tdPhanHuyPhongKhong = M_co2tdPhanHuyPhongKhong/sumKNKByWasteDisposal
    val tyle_co2BODra = M_co2BODra/sumKNKByWasteDisposal
    val tyle_phatThai_co2td_n2o = phatThai_co2td_n2o/sumKNKByWasteDisposal

    val sumKNKAll = (sumKNKByElecPower + sumKNKByWasteDisposal)
    val tyle_elecPower = sumKNKByElecPower/sumKNKAll
    val tyle_wasteDisposal = sumKNKByWasteDisposal/sumKNKAll

    releaseResultOutput(elecPower, M_co2_quaTrinh, M_co2tdPhanHuyPhongKhong, M_co2BODra, phatThai_co2td_n2o, sumKNKByElecPower,
      sumKNKByWasteDisposal, tyle_co2_quaTrinh, tyle_co2tdPhanHuyPhongKhong, tyle_co2BODra, tyle_phatThai_co2td_n2o, sumKNKAll,
      tyle_elecPower, tyle_wasteDisposal)
  }
}

case class Bien1Output(ss_ov: Double,
                       pr_blBod: Double,
                       bod_khuBl: Double,
                       s_ov: Double,
                       pr_blSs: Double,
                       ss_khuBl: Double)

object Bien2Output {
  val r_n2o = .004 //fixme hardcode
}

final case class Bien2Output(s: Double,
                             q_v: Double,
                             s_v: Double,
                             X: Double,
                             ss_v: Double,
                             vss_v: Double,
                             X_nbV: Double = 0,
                             X_nb: Double,
                             V: Double,
                             p_XBod: Double,
                             p_ssManhTeBao: Double,
                             p_ssNbVss: Double,
                             N: Double,
                             Ncalc: Double,
                             m_nit: Double,
                             srtNit: Double,
                             x_nit: Double,
                             p_ssNit: Double,
                             p_XBio: Double,
                             p_ss: Double,
                             bod_ox: Double,
                             bod_ox_dnt: Double,
                             bod_khuThuc: Double,
                             co2_khu_bod: Double,
                             vss_phanhuy: Double,
                             co2_phanhuy: Double,
                             co2_dnt: Double,
                             co2_tieuThuNit: Double,
                             co2_quaTrinh: Double, //qua trinh hieu / yem khi
                             n2o: Double,
                             co2_n2o: Double,
                             ch4_beYemKhi: Double = 0,
                             tyLePhatThai: Double
                      ) {
  @inline def bod_khu_an = bod_ox
}

case class Bien3Output(p_vss_dr: Double,
                       q_xa: Double,
                       q_v_dr: Double,
                       s_v_dr: Double,
                       s_dr: Double,
                       p_ssBodDr: Double,
                       p_ssManhTeBaoDr: Double,
                       p_ssBioDr: Double,
                       bod_khu_dr: Double,
                       x_dr: Double,
                       v_dr: Double,
                       vss_decay_dr: Double,
                       co2_bePhanHuy: Double,
                       ch4_bePhanHuy: Double,
                       ch4_phanHuy_thuHoi: Double,
                       ch4_phanHuy_roRi: Double,
                       co2_phanHuyMetan: Double,
                       co2_phanHuyTotal: Double,
                       knk_direct: Double,
                       m_CO2BODra: Double
                      )
case class Bien4Output(bun_phanHuy: Double,
                      co2_phatThai: Double,
                      ch4_phatThai: Double,
                      ch4_phanHuyRoRi: Double,
                      Pr_ch4_roRi: Double = 0.05,
                      ch4_phanHuyThuGom: Double,
                      y_ch4dot: Double,
                      ch4_phanHuyThuHoi: Double,
                      co2_phanHuyMetan: Double,
                      Pco2_bunPhanHuy: Double,
                      P_bunSinhHoc: Double,
                       M_co2PhanHuy: Double,
                       M_ch4PhanHuy: Double,
                       M_co2tdPhanHuyPhongKhong: Double,
                       M_ch4PhanHuyRoRi: Double,
                       M_ch4PhanHuyThuHoi: Double,
                       M_co2tdPhanHuyMetan: Double,
                       M_co2tdPhanHuyDot: Double
                      )
case class Bien5Output(co2_tongPhatThai: Double,
                       N_dongra: Double,
                       EF_dongra: Double,
                       phatThaiGianTiep_n2o: Double,
                       pop: Double,
                       T_hlxlnt: Double,
                       EF_hlxlnt: Double,
                       F_IND_COM: Double,
                       phatThaiTrucTiep_n2o: Double,
                       tongPhatThai_n2o: Double,
                       phatThai_co2td_n2o: Double
                      )

case class Bien6Output(n_dongRa: Double,
                      ef_dongRa: Double,
                      n2o_phatThaiGianTiep: Double,
                      soDanSo_p: Double,
                      ef_htxlnt: Double,
                      cf: Double,
                      n2o_phatThaiTrucTiep: Double,
                      n2o_tongPhatThai: Double,
                      co2_tuongDuongNito: Double)

case class Bien2UnstablOutput(s_v: Double,
                             x_v: Double,
                             hrt: Double,
                             srt: Double,
                             y: Double,
                             micro_m: Double,
                             k_s: Double,
                             k_d: Double,
                             S: Double,
                             X: Double,
                             t: Double)

case class retrieveResultOutput(elecPower: Double,
                                M_co2_quaTrinh: Double,
                                M_co2tdPhanHuyDot: Double,
                                M_co2BODra: Double,
                                phatThai_co2td_n2o: Double,
                                sumKNKByElecPower: Double,
                                sumKNKByWasteDisposal: Double,
                                tyle_co2_quaTrinh: Double,
                                tyle_co2tdPhanHuyDot: Double,
                                tyle_co2BODra: Double,
                                tyle_phatThai_co2td_n2o: Double,
                                sumKNKAll: Double,
                                tyle_elecPower: Double,
                                tyle_wasteDisposal: Double
                               )

case class releaseResultOutput(elecPower: Double,
                                M_co2_quaTrinh: Double,
                                M_co2tdPhanHuyPhongKhong: Double,
                                M_co2BODra: Double,
                                phatThai_co2td_n2o: Double,
                                sumKNKByElecPower: Double,
                                sumKNKByWasteDisposal: Double,
                                tyle_co2_quaTrinh: Double,
                                tyle_co2tdPhanHuyPhongKhong: Double,
                                tyle_co2BODra: Double,
                                tyle_phatThai_co2td_n2o: Double,
                                sumKNKAll: Double,
                                tyle_elecPower: Double,
                                tyle_wasteDisposal: Double
                               )