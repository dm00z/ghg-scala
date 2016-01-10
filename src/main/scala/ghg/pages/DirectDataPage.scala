package ghg.pages

import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.DirectTable._
import tex.TeX._

object DirectDataPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.d)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my

      def streamInTbl(implicit d: StreamInData) = {
        @inline implicit def dispatch: StreamInData => Callback = my.dispatch

        table(
          <.tr(ThongSo, DonVi, GiaTri, GhiChu),
          <.tr(td("Q", "o,v"), <.td("m3/day"), <.td(P().info.power),
            <.td("Lưu lượng dòng vào hệ thống xử lý")
          ),
          <.tr(td("S", "o,v"), <.td("mg/l"),
            tdInput(StreamInData.s),
            <.td("Nồng độ cơ chất dòng vào hệ thống")
          ),
          <.tr(td("TKN", "v"), <.td("mg/l"),
            tdInput(StreamInData.tkn),
            <.td()
          ),
          <.tr(<.td("TSS"), <.td("mg/l"),
            tdInput(StreamInData.tss),
            <.td()
          )
        )
      }

      def streamOutTbl(implicit d: StreamOutData) = {
        @inline implicit def dispatch: StreamOutData => Callback = my.dispatch

        table(
          <.tr(ThongSo, DonVi, GiaTri),
          <.tr(td("S", "r"), <.td("mg/l"),
            tdInput(StreamOutData.s)
          ),
          <.tr(td("N", "r"), <.td("mg/l"),
            tdInput(StreamOutData.n)
          ),
          <.tr(td("VSS", "r"), <.td("mg/l"),
            tdInput(StreamOutData.vss)
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

      def poolTbl(tpe: String)(implicit d: PoolData) = {
        @inline implicit def dispatch: PoolData => Callback = pd => my.dispatch(tpe -> pd)

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
          ),
          if (tpe == "decay") EmptyTag else <.tr(
            <.td("Q_ratio"), <.td(),
            tdInput(PoolData.qxRatio),
            <.td("Tỉ lệ `Q_(xa) / Q_v`".teX)
          )
        )
      }

      val d = my()
      <.div(
        <.h3("1. Thông số dòng vào hệ thống xử lý"),
        streamInTbl(d.streamIn),
        <.h3("2. Thông số bể xử lý sơ cấp"),
        <.p(<.i("Tài liệu tham khảo: Metcalf and Eddy (2002)")),
        primaryTbl(d.primaryPool),
        d.anaerobicPool ? (pool => Seq(
          <.h3("3. Thông số bể yếm khí"),
          poolTbl("ane")(pool)
        )),
        d.aerobicPool ? (pool => Seq(
          <.h3("4. Thông số bể hiếu khí"),
          poolTbl("ae")(pool)
        )),
        <.h3("5. Thông số bể phân hủy yếm khí"),
        poolTbl("decay")(d.decayPool),
        <.h3("6. Thông số dòng ra hệ thống xử lý"),
        streamOutTbl(d.streamOut)
      )
    }
  }

  val component = ReactComponentB[Props]("DirectData")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
