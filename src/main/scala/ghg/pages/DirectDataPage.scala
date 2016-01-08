package ghg.pages

import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.DirectTable._

object DirectDataPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.d)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my

      def genericTbl(implicit d: GenericData) = {
        @inline implicit def dispatch: GenericData => Callback = my.dispatch

        table(
          <.tr(ThongSo, DonVi, GiaTri, GhiChu),
          <.tr(td("Q", "o,v"), <.td("m3/day"), <.td(P().info.power),
            <.td("Lưu lượng dòng vào hệ thống xử lý")
          ),
          <.tr(td("S", "o,v"), <.td("mg/l"),
            tdInput(GenericData.s),
            <.td("Nồng độ cơ chất dòng vào hệ thống")
          ),
          <.tr(td("TKN", "v"), <.td("mg/l"),
            tdInput(GenericData.tkn),
            <.td()
          ),
          <.tr(<.td("TSS"), <.td("mg/l"),
            tdInput(GenericData.tss),
            <.td()
          )
        )
      }

      def primaryTbl(implicit d: PrimaryPoolData) = {
        @inline implicit def dispatch: PrimaryPoolData => Callback = my.dispatch

        table(
          <.tr(ThongSo, DonVi, GiaTri, GhiChu),
          <.tr(td("Pr", "bl,BOD"), <.td("*100%"),
            tdInput(PrimaryPoolData.prBOD),
            <.td("Tỉ lệ khử BOD5 trong bể lắng sơ cấp")
          ),
          <.tr(td("Pr", "bl,SS"), <.td("*100%"),
            tdInput(PrimaryPoolData.prSS),
            <.td("Tỉ lệ khử SS trong bể lắng sơ cấp")
          ),
          <.tr(td("Q", "bl"), <.td("m3/day"),
            tdInput(PrimaryPoolData.q),
            <.td("Lưu lượng xả bùn ở bể lắng sơ cấp")
          )
        )
      }

      def poolTbl(implicit d: PoolData) = {
        @inline implicit def dispatch: PoolData => Callback = my.dispatch

        table(
          <.tr(ThongSo, DonVi, GiaTri, GhiChu),
          <.tr(<.td("Nhiệt độ"), <.td("°C"),
            tdInput(PoolData.t),
            <.td()
          ),
          <.tr(<.td("SRT"), <.td("days"),
            tdInput(PoolData.srt),
            <.td("Tuổi bùn")
          ),
          <.tr(
            <.td("HRT"), <.td("hours"),
            tdInput(PoolData.hrt),
            <.td("Thời gian lưu thủy lực")
          )
        )
      }

      val d = my()
      <.div(
        <.h3("1. Thông số dòng vào hệ thống xử lý"),
        genericTbl(d.generic),
        <.h3("2. Thông số bể xử lý sơ cấp"),
        <.p(<.i("Tài liệu tham khảo: Metcalf and Eddy (2002)")),
        primaryTbl(d.primaryPool),
        d.anaerobicPool ? (pool => Seq(
          <.h3("3. Thông số bể yếm khí"),
          poolTbl(pool)
        )),
        d.aerobicPool ? (pool => Seq(
          <.h3("4. Thông số bể hiếu khí"),
          poolTbl(pool)
        )),
        Seq(
          <.h3("5. Thông số bể phân hủy yếm khí "),
          poolTbl(d.decayPool)
        )
      )
    }
  }

  val component = ReactComponentB[Props]("DirectData")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
