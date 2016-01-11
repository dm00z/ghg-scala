package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import ghg.Utils._

object DirectPage {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b2 = d.bien2 //fixme anaerobic
      val b3 = d.bien3

      def d_t(t: Double) = d.copy(
        direct = d.direct.copy(
          coef = d.direct.coef.copy(
            aerobic = d.direct.coef.aerobic.copy(t = t),
            nitrate = d.direct.coef.nitrate.copy(t = t)
          )))
        .bien3.knk_direct

      val graph = <.div(
        <.div(^.marginTop := 10.px,
          <.span(^.color := "red", ^.marginLeft := 20.px,
            "KNK", <.sub("trực_tiếp"), " theo nhiệt độ của quá trình hiếu khí và nitrat")
        ),
        MGraph.one(d_t, 10 to 30)
      )

      <.div(
        <.h2("Tổng lượng KNK phát sinh từ hệ xử lý hiếu khí"),
        table(
          tr("CO", "2,quaTrinhHieuKhi", b2.co2_quaTrinhHieuKhi, "g/day"),
          tr("CO", "2,N2OphatThai", b2.co2_n2o, "g/day"),
          tr("Tổng CO", "2,bePhanHuy", b3.co2_phanHuyTotal, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct, "g/day"),
          tr("KNK", "trực_tiếp", b3.knk_direct / 1000, "kg/day")
        ),
        graph
      )
    }
  }

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
