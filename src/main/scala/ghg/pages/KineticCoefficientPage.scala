package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{TechMethod, KineticCoefficientData, GhgData}
import reactd3.ChartSerie
import scala.scalajs.js
import tex.TeX._
import ghg.Utils._
import KineticCoefficientData._

object KineticCoefficientPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.coef)
  }

  @inline def td2(s: String) = <.td(^.rowSpan := 2, <.b(s))

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      def aerobicTbl(implicit d: Aerobic) = {
        import scala.scalajs.js.JSNumberOps._
        @inline implicit def dispatch: Aerobic => Callback = P.my.dispatch

        val tbl = table(
          <.tr(
            td2("Thông số"),
            td2("Đơn vị"),
            <.td(^.colSpan := 2, <.b("Giá trị tiêu chuẩn ở 20°C")),
            td2("Hệ số nhiệt độ θ"),
            td2("Giá trị")
          ),
          <.tr(),
          <.tr(<.td("k"),
            <.td(),
            <.td(),
            tdInput(Aerobic.m ^|-> KT.vNorm),
            tdInput(Aerobic.m ^|-> KT.coeff),
            <.td(d.k_.toFixed(3))),
          <.tr(<.td("Y"), <.td("mg/mg"), <.td(Aerobic.Y.range.text),
            tdInput(Aerobic.y ^|-> KT.vNorm, _.between(Aerobic.Y.range)),
            tdInput(Aerobic.y ^|-> KT.coeff),
            <.td(d.y_.toFixed(3))),
          <.tr(td("K", "S"), <.td("mg/l"), <.td(Aerobic.Ks.range.text),
            tdInput(Aerobic.ks ^|-> KT.vNorm, _.between(Aerobic.Ks.range)),
            <.td(),
            <.td(d.ks_.toFixed(3))
          ),
          <.tr(td("k", "d"), Ngay1, <.td(Aerobic.Kd.range.text),
            tdInput(Aerobic.kd ^|-> KT.vNorm, _.between(Aerobic.Kd.range)),
            tdInput(Aerobic.kd ^|-> KT.coeff),
            <.td(d.kd_.toFixed(3))),
          <.tr(td("f", "d"),
            <.td(), <.td(), <.td(),
            <.td(), tdInput(Aerobic.fd))
        )

        Seq(<.div("Nhiệt độ t°C: ", input(Aerobic.t)), tbl)
      }

      def nitratTbl(implicit d: Nitrate) = {
        import scala.scalajs.js.JSNumberOps._
        @inline implicit def dispatch: Nitrate => Callback = P.my.dispatch
        val tbl = table(
          <.tr(
            td2("Thông số"),
            td2("Đơn vị"),
            <.td(^.colSpan := 2, <.b("Giá trị tiêu chuẩn ở 20°C")),
            td2("Giá trị")
          ),
          <.tr(),
          <.tr(td("μ", "m,nit"), Ngay1,
            <.td(Nitrate.M.range.text),
            tdInput(Nitrate.m ^|-> MT.vNorm, _.between(Nitrate.M.range)),
            <.td(d.m_.toFixed(3))),
          <.tr(td("Y", "nit"), <.td("mg/mg"),
            <.td(Nitrate.Y.range.text),
            tdInput(Nitrate.y ^|-> KT.vNorm, _.between(Nitrate.Y.range)),
            <.td(d.y_.toFixed(3))),
          <.tr(td("k", "d,nit"), Ngay1,
            <.td(Nitrate.Kd.range.text),
            tdInput(Nitrate.kd ^|-> KT.vNorm, _.between(Nitrate.Kd.range)),
            <.td(d.kd_.toFixed(3)))
        )

        Seq(<.div("Nhiệt độ t°C: ", input(Nitrate.t)), tbl)
      }

      def anaerobicTbl(implicit d: Anaerobic) = {
        import scala.scalajs.js.JSNumberOps._
        @inline implicit def dispatch: Anaerobic => Callback = P.my.dispatch
        Seq(
          <.div("Nhiệt độ bể yếm khí t_an °C: ", input(Anaerobic.t_an)),
          <.div("Nhiệt độ bể phân hủy bùn t_dr °C: ", input(Anaerobic.t_dr)),
          table(
            <.tr(
              td2("Thông số"),
              td2("Đơn vị"),
              <.td(^.colSpan := 2, <.b("Giá trị tiêu chuẩn ở 20°C")), //25
              td2("Giá trị ở nhiệt độ t_an °C"),
              td2("Giá trị ở nhiệt độ t_dr °C")
            ),
            <.tr(Khoang, GiaTri),
            <.tr(<.td("μ", <.sub("m,an"), ", μ", <.sub("m,dr")), Ngay1,
              <.td(Anaerobic.M.range.text),
              tdInput(Anaerobic.m ^|-> KT.vNorm, _.between(Anaerobic.M.range)),
              <.td(d.m(d.t_an).toFixed(3)),<.td(d.m(d.t_dr).toFixed(3))),
            <.tr(<.td("Y", <.sub("an"), ", Y", <.sub("dr")), <.td("mg/mg"),
              <.td(Anaerobic.Y.range.text),
              tdInput(Anaerobic.y, _.between(Anaerobic.Y.range)),
              <.td(d.y),<.td(d.y)),
            <.tr(<.td("K", <.sub("s,an"), ", K", <.sub("s,dr")), <.td("mg/l"),
              <.td(Anaerobic.Ks.range.text),
              tdInput(Anaerobic.ks ^|-> KT.vNorm, _.between(Anaerobic.Ks.range)),
              <.td(d.ks(d.t_an).toFixed(3)),<.td(d.ks(d.t_dr).toFixed(3))),
            <.tr(<.td("k", <.sub("d,an"), ", k", <.sub("d,dr")), Ngay1,
              <.td(Anaerobic.Kd.range.text),
              tdInput(Anaerobic.kd, _.between(Anaerobic.Kd.range)),
              <.td(d.kd), <.td(d.kd)),
            <.tr(<.td("f", <.sub("d,an"), ", f", <.sub("d,dr")), <.td(),
              <.td(),
              tdInput(Anaerobic.fd),
              <.td(d.fd),<.td(d.fd))
          )
        )
      }

      def aerobicGraph(data: Aerobic) = MGraph(
        <.div(^.marginTop := 10.px,
          <.span(^.color := "green", ^.marginLeft := 140.px, "μ", <.sub("m")),
          <.span(^.color := "blue", ^.marginLeft := 260.px, "k", <.sub("d")),
          <.span(^.color := "red", ^.marginLeft := 240.px, "`k = mu_m / Y`".teX)
        ),
        (20 to 40).map(t => js.Dynamic.literal(
          "t" -> t,
          "m" -> data.m(t),
          "kd" -> data.kd(t),
          "k" -> data.k(t)
        )).toJsArray, false,
        ChartSerie("m", "green"),
        ChartSerie("kd", "blue"),
        ChartSerie("k", "red")
      )

      def nitratGraph(d: Nitrate) = MGraph(
        <.div(^.marginTop := 10.px,
          <.span(^.color := "green", ^.marginLeft := 20.px, s"`mu_(m,nit)(t) = mu_(m,nit)(${d.m.tNorm}) * e^(0.098 * (t-${d.m.tNorm}))`".teX),
          <.span(^.color := "blue", ^.marginLeft := 60.px, s"`k_(d,nit)(t) = k_(d,nit)(${d.kd.tNorm}) * theta ^ (t-${d.kd.tNorm})`".teX),
          <.span(^.color := "red", ^.marginLeft := 100.px, "`k = mu_(m,nit) / Y`".teX),
          <.span(^.color := "black", ^.marginLeft := 172.px, "`K_N(t) = 10^(0.05*t - 1.185)`".teX)
        ),
        (20 to 40).map(t => js.Dynamic.literal(
          "t" -> t,
          "m" -> d.m(t),
          "kd" -> d.kd(t),
          "k" -> d.k(t),
          "kn" -> d.kn(t)
        )).toJsArray, false,
        ChartSerie("m", "green"),
        ChartSerie("kd", "blue"),
        ChartSerie("k", "red"),
        ChartSerie("kn", "black")
      )

      def anaerobicGraph(d: Anaerobic) = <.div(

      )

      val tech = P().info.tech
      val p = P.my()

      <.div(
        if (tech == TechMethod.An) EmptyTag
        else Seq[TagMod](
          <.h3("Quá trình hiếu khí"),
          aerobicTbl(p.aerobic),
          //aerobicGraph(p.aerobic),
          <.h3("Quá trình nitrat và khử nitrat"),
          nitratTbl(p.nitrate)
          //nitratGraph(p.nitrate)
        )
//        <.h3("Quá trình yếm khí"),
//        anaerobicTbl(p.anaerobic),
//        anaerobicGraph(p.anaerobic)
      )
    }
  }

  val component = ReactComponentB[Props]("KineticCoefficient")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
