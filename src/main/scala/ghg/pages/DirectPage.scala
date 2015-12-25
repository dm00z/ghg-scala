package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData
import model.KineticCoefficientData.Aerobic
import tex.TeX._

object DirectPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      import ghg.Utils._
      val d = P()

      def tbl1_1() = {
        val sov = d.direct.d.generic.s
        val pr = d.direct.d.primaryPool.prBOD
        val ss = d.info.power * sov * pr

        table(
          <.tr(ThongSo,
            <.td(<.b("Công suất dòng vào ban đầu")),
            <.td("Nồng độ `BOD_5` dòng vào ban đầu".teXb),
            <.td("Phần trăm khư `BOD_5` trong bể lắng sơ cấp".teXb),
            <.td("Lượng `BOD_5` bị khử trong bể lắng sơ cấp (g/day)".teXb),
            <.td("Lượng `BOD_5` bị khử trong bể lắng sơ cấp (kg/day)".teXb)
          ),
          <.tr(KyHieu, td("Q", "o,v"), td("S", "o,v"), td("Pr", "bl,BOD"), td("BOD", "khu,bl"), td("BOD", "khu,bl")),
          <.tr(DonVi, <.td("m3/day"), <.td("mg/l"), <.td("%"), <.td("g/day"), <.td("kg/day")),
          <.tr(GiaTri, <.td(d.info.power),
            <.td(sov),
            <.td(pr),
            <.td(ss), <.td(ss / 1000)
          )
        )
      }

      def tbl1_2() = {
        val tss = d.direct.d.generic.tss
        val pr = d.direct.d.primaryPool.prSS
        val ss = d.info.power * tss * pr
        table(
          <.tr(ThongSo, <.td(<.b("Công suất dòng vào ban đầu")),
            <.td(<.b("Nồng độ SS dòng vào ban đầu")),
            <.td(<.b("Phần trăm khư SS trong bể lắng sơ cấp")),
            <.td(<.b("Lượng SS bị khử trong bể lắng sơ cấp (g/day)")),
            <.td(<.b("Lượng SS bị khử trong bể lắng sơ cấp (kg/day)"))
          ),
          <.tr(KyHieu, td("Q", "o,v"), td("X", "o,v"), td("Pr", "bl,SS"), td("SS", "khu,bl"), td("SS", "khu,bl")),
          <.tr(DonVi, <.td("m3/day"), <.td("mg/l"), <.td("%"), <.td("g/day"), <.td("kg/day")),
          <.tr(GiaTri, <.td(d.info.power),
            <.td(tss),
            <.td(pr),
            <.td(ss), <.td(ss / 1000)
          )
        )
      }

      def tbl2(implicit d: Aerobic) = {
        @inline implicit def dispatch: Aerobic => Callback = P.my.dispatch
        val m = d.m(d.t)
        val y = d.y(d.t)

        table(
          <.tr(<.td("Nhiệt độ (°C)"), <.td(Muy), <.td("k = ", Muy, "/Y"), td("K", "s"), td("k", "d"), <.td("Y")),
          <.tr(tdInput(Aerobic.t), <.td(m), <.td(m/y), <.td(), <.td(), <.td(y))
        )
      }

      <.div(
        <.h3("1. Đường biên 1 - Bể lắng sơ cấp"),
        <.h3("1.1. Lượng BOD bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `BOD_(khu,bl) = Pr_(bl,BOD) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_1(),
        <.h3("1.2. Lượng SS bị khử trong bể lắng sơ cấp"),
        <.div("Công thức tính: `SS_(khu,bl) = Pr_(bl,SS) * Q_(o,v) * S_(o,v)`".teX),
        tbl1_2(),
        <.h3("1. Đường biên 2 - Hệ xử lý hiếu khí")
      )
    }
  }

  val component = ReactComponentB[Props]("Direct")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
