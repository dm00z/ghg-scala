package model

object DirectData {

  /** Thông số dòng vào hệ thống xử lý
    * @note q: Double = 8000 (m3/day) is taken from InfoData
    * @param s mg/l
    * @param tkn mg/l
    * @param tss mg/l */
  case class GenericData(s: Double = 350, tkn: Double = 30, tss: Double = 1000)

  /** Thông số bể xử lý sơ cấp
    * Tài liệu tham khảo 	Metcalf and Eddy (2002)
    * @param prBOD (%) value in rang [0,1]
    * @param prSS (%) value in rang [0,1]
    * @param q m3/day
    */
  case class PrimaryPoolData(prBOD: Double = .25, prSS: Double = .40, q: Double = 150)

  /** Thông số bể yếm khí hoặc hiếu khí
    * @param t oC
    * @param srt day
    * @param hrtOpt hour
    * @param vOpt m3
    *
    * @note sv, s, x, q are derivative
    *       v and hrt have a relation (v = hrt / q), we must provide only one value.
    */
  case class PoolData(t: Int, srt: Double, hrtOpt: Option[Double], vOpt: Option[Double] = None)
  object PoolData {
    lazy val testAnaerobic = PoolData(40, 20, Some(15))
    lazy val testAerobic = PoolData(20, 10, Some(5))
    lazy val testDecayPool = PoolData(30, 20, Some(20))
  }

  /** Thông số dòng ra hệ thống xử lý
    * @param t oC
    * @param n mg/l
    * @param vss mg/l
    * @note Sr (mg/l) là thông số dòng ra, là giá trị được tính toán. */
  case class Output(t: Int, n: Double, vss: Double)
}

import DirectData._
/** @note Nhà máy có thể chỉ có bể yếm, hoặc hiếu khí, hoặc cả 2.
  *       Nếu có cả 2 thì thông số dòng ra của bể yếm khí sẽ được dùng làm thông số dòng vào cho hiếu khí
  *       (không nhập dữ liệu cho thông số dòng vào cho bể hiếu khí)
  */
case class DirectData(generic: GenericData, primaryPool: PrimaryPoolData,
                      decayPool: DirectData.PoolData,
                      anaerobicPool: Option[DirectData.PoolData] = None,
                      aerobicPool: Option[DirectData.PoolData] = None)
