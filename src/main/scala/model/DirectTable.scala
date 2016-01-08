package model

import monocle.macros.Lenses

object DirectTable {

  /** Thông số dòng vào hệ thống xử lý
    *
    * @note q: Double = 8000 (m3/day) is taken from InfoData
    * @param s mg/l
    * @param tkn mg/l
    * @param tss mg/l */
  @Lenses case class StreamInData(s: Double, tkn: Double, tss: Double)

  /** Thông số bể xử lý sơ cấp
    * Tài liệu tham khảo 	Metcalf and Eddy (2002)
    *
    * @param prBOD (%) value in rang [0,1]
    * @param prSS (%) value in rang [0,1]
    * @param q m3/day
    */
  @Lenses case class PrimaryPoolData(prBOD: Double, prSS: Double, q: Double)

  /** Thông số bể yếm khí hoặc hiếu khí
    *
    * @param t oC
    * @param srt day
    * @param hrt hour
    * @note sv, s, x, q are derivative
    *       v and hrt have a relation (v = hrt * q), we must provide only one value.
    *       But for simply, hrt is input & v is calculated based on hrt
    */
  @Lenses case class PoolData(t: Double, srt: Double, hrt: Double) {
    val hrtDay = hrt / 24
    /** m3, = hrtDay * q */
//    def v: Double
  }

  /** Thông số dòng ra hệ thống xử lý
    *
    * @param t oC
    * @param n mg/l
    * @param vss mg/l
    * @param s (mg/l) là thông số dòng ra, là giá trị được tính toán nhưng hiện tại cho input :D. */
  @Lenses case class StreamOutData(t: Double, s: Double, n: Double, vss: Double)
}

import DirectTable._
/** @note Nhà máy có thể chỉ có bể yếm, hoặc hiếu khí, hoặc cả 2.
  *       Nếu có cả 2 thì thông số dòng ra của bể yếm khí sẽ được dùng làm thông số dòng vào cho hiếu khí
  *       (không nhập dữ liệu cho thông số dòng vào cho bể hiếu khí)
  */
case class DirectTable(streamIn: StreamInData, primaryPool: PrimaryPoolData,
                       decayPool: PoolData,
                       anaerobicPool: Option[DirectTable.PoolData] = None,
                       aerobicPool: Option[DirectTable.PoolData] = None,
                       streamOut: DirectTable.StreamOutData = DirectTable.StreamOutData(22, 10, 8, 10))
