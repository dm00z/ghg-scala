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

      def dataTbl(tpe: String)(implicit  dataIn: StreamInData, dataOut: StreamOutData, dataPrimaryPool: PrimaryPoolData, dataPool: PoolData) = {
        @inline implicit def dispatchIn: StreamInData => Callback = my.dispatch
        @inline implicit def dispatchOut: StreamOutData => Callback = my.dispatch
        @inline implicit def dispatchPriPool: PrimaryPoolData => Callback = my.dispatch
        @inline implicit def dispatch: PoolData => Callback = pd => my.dispatch(tpe -> pd)

        table4(
          <.tr(ThongSo, DonVi, GiaTri, GhiChu),
          <.tr(td("Q", "o,v"), <.td("m3/day"),
            <.td(P().info.power),
            <.td("")
          ),
          <.tr(td("S", "o,v"), <.td("mg/l"),
            tdInput(StreamInData.so),
            <.td("")
          ),
          <.tr(td("SS", "o,v"), <.td("mg/l"),
            tdInput(StreamInData.sso),
            <.td("")
          ),
          <.tr(td("SS", "v"), <.td("mg/l"),
            tdInput(PrimaryPoolData.ssv),
            <.td("")
          ),
          <.tr(td("SS", "r"), <.td("mg/l"),
            tdInput(StreamOutData.ss),
            <.td()
          ),
          <.tr(td("N", "v"), <.td("mg/l"),
            tdInput(StreamInData.n),
            <.td("")
          ),
          <.tr(td("N", "r"), <.td("mg/l"),
            tdInput(StreamOutData.n),
            <.td()
          ),
          <.tr(td("Q", "bl"), <.td("m3/day"),
            tdInput(PrimaryPoolData.q),
            <.td("")
          ),
          <.tr(td("Q", "v"), <.td("m3/day"),
            tdInput(PrimaryPoolData.qv),
            <.td("")
          ),
//          <.tr(td("Q", "x"), <.td("m3/day"),
//            tdInput(PrimaryPoolData.qx),
//            <.td("")
//          ),
          <.tr(td("S", "v"), <.td("ml/g"),
            tdInput(StreamInData.s),
            <.td("")
          ),
          <.tr(td("S", "r"), <.td("ml/g"),
            tdInput(StreamOutData.s),
            <.td("")
          ),
//          <.tr(td("X", "v"), <.td("ml/g"),
//            tdInput(StreamInData.x),
//            <.td("")
//          ),
//          <.tr(td("X", "r"), <.td("ml/g"),
//            tdInput(StreamOutData.x),
//            <.td("")
//          ),
//          <.tr(td("X", "T"), <.td("ml/g"),
//            tdInput(StreamInData.xt),
//            <.td("")
//          ),
          <.tr(td("V", ""), <.td("m3"),
            tdInput(PoolData.v),
            <.td("")
          ),
          <.tr(
            <.td("HRT"), <.td("ngày"),
            tdInput(PoolData.hrt),
            <.td("")
          ),
          <.tr(
            <.td("SRT"), <.td("days"),
            tdInput(PoolData.srt),
            <.td("")
          ),
          <.tr(
            <.td("Nhiệt độ"), <.td("°C"),
            tdInput(PoolData.t),
            <.td()
          ),

          /*---------------------------------------------*/
          <.tr(td("Pr", "bl,BOD"), <.td("*100%"),
            tdInput(PrimaryPoolData.prBOD),
            <.td("")
          ),
          <.tr(td("Pr", "bl,SS"), <.td("*100%"),
            tdInput(PrimaryPoolData.prSS),
            <.td("")
          )
//          <.tr(td("TN", "v"), <.td("mg/l"),
//            tdInput(StreamInData.tn),
//            <.td()
//          ),
//          <.tr(<.td("TSS"), <.td("mg/l"),
//            tdInput(StreamInData.tss),
//            <.td()
//          ),
//          /*---------------------------------------------*/
//
//
//          <.tr(td("VSS", "r"), <.td("mg/l"),
//            tdInput(StreamOutData.vss),
//            <.td()
//          ),
//          /*---------------------------------------------*/
//
//          <.tr(<.td("SRT"), <.td("days"),
//            tdInput(PoolData.srt),
//            <.td("Tuổi bùn")
//          ),
//          <.tr(
//            <.td("HRT"), <.td("hours"),
//            tdInput(PoolData.hrt),
//            <.td("Thời gian lưu thủy lực")
//          ),
//          if (tpe == "decay") EmptyTag else <.tr(
//            <.td("Q_ratio"), <.td(),
//            tdInput(PoolData.qxRatio),
//            <.td("Tỉ lệ `Q_(xa) / Q_v`".teX)
//          )
        )

      }


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
          <.tr(td("TN", "v"), <.td("mg/l"),
            tdInput(StreamInData.tn),
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
          <.tr(td("S", "r"), <.td("mg/l"),
            tdInput(StreamOutData.s),
            <.td()
          ),
          <.tr(td("N", "r"), <.td("mg/l"),
            tdInput(StreamOutData.n),
            <.td()
          ),
          <.tr(td("VSS", "r"), <.td("mg/l"),
            tdInput(StreamOutData.vss),
            <.td()
          )
        )
      }

      def primaryTbl(implicit d: PrimaryPoolData) = {
        @inline implicit def dispatch: PrimaryPoolData => Callback = my.dispatch

        table(
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
          dataTbl("decay")(d.streamIn, d.streamOut, d.primaryPool, d.decayPool)
//        streamInTbl(d.streamIn),
//        primaryTbl(d.primaryPool),
//        d.anaerobicPool ? (pool => Seq(
//          poolTbl("ane")(pool)
//        )),
//        d.aerobicPool ? (pool => Seq(
//          poolTbl("ae")(pool)
//        )),
//        poolTbl("decay")(d.decayPool),
//        streamOutTbl(d.streamOut)
      )
    }
  }

  val component = ReactComponentB[Props]("DirectData")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
