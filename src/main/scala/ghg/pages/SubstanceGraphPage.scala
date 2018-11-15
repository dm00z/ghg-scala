package ghg.pages

import diode.react.ModelProxy
import ghg.components.MGraph
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.KineticCoefficientData.Nitrate.MT
import model.{GhgData, KineticCoefficientData, TechMethod}

import scala.scalajs.js
import tex.TeX._
import ghg.Utils._
import KineticCoefficientData._
import chandu0101.scalajs.react.components.materialui.MuiTextField
import reactd3._

object SubstanceGraphPage {
  type Props = ModelProxy[GhgData]
  implicit  final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.direct.d)
  }

  case class Backend($: BackendScope[GhgData, _]) {
    def graph(r: Range, f: Double => GhgData) = MGraph(
      <.div(
      ),
      r.map { temp =>
        val d = f(temp)
        val b2 = d.bien2Ae
        val b3 = d.bien3

        val release = d.releaseResult
        val retrieve = d.retrieveResult

        js.Dynamic.literal(
          "t" -> temp,
          "Nồng độ cơ chất (S)" -> b2.s,
          "Nồng độ sinh khối (X)" -> b2.X,
          "Bể sinh học" -> retrieve.M_co2_quaTrinh,
          "Bể bùn thu hồi - đốt" -> retrieve.M_co2tdPhanHuyDot,
          "Bể bùn phóng không" -> release.M_co2tdPhanHuyPhongKhong
        )
      }.toJsArray, true,
      ChartSerie("Nồng độ cơ chất (S)", "#008000"),
      ChartSerie("Nồng độ sinh khối (X)", "black")
    )

    def graph2(r: Range, f: Double => GhgData, large: Boolean) = {

      val otherProps = js.Dynamic.literal(
        width = if (large) 420 else 280,
        height = 260,
        margins = js.Dynamic.literal(
          top = 20, bottom = 20, right = 34,
          left = if (large) 100 else 38
        )
      )

      val data = r.map { temp =>
        val q = f(temp)
        val rel = q.releaseResult
        val rei = q.retrieveResult

        js.Dynamic.literal(
          "t" -> temp,
          "Bể sinh học" -> rei.M_co2_quaTrinh,
          "Bể bùn thu hồi - đốt" -> rei.M_co2tdPhanHuyDot,
          "Bể bùn phóng không" -> rel.M_co2tdPhanHuyPhongKhong
        )
      }.toJsArray

      val bioPoolRei = ChartSerie("Bể sinh học", "green")
      val combustPool = ChartSerie("Bể bùn thu hồi - đốt", "blue")
      val bioPoolRel = ChartSerie("Bể bùn phóng không", "red")

      val chartSerie = js.Array(bioPoolRei, combustPool, bioPoolRel)

      val chart = LineChart(data, (d: js.Dynamic) => d.t.asInstanceOf[Double], chartSerie)(otherProps): TagMod

      <.div(chart)
    }

    def graphTotal(r: Range, f: Double => GhgData, large: Boolean) = {

      val otherProps = js.Dynamic.literal(
        width = if (large) 420 else 280,
        height = 260,
        margins = js.Dynamic.literal(
          top = 20, bottom = 20, right = 34,
          left = if (large) 100 else 38
        )
      )

      val data = r.map { temp =>
        val q = f(temp)
        val rel = q.releaseResult
        val rei = q.retrieveResult

        js.Dynamic.literal(
          "t" -> temp,
          "Tổng KNK - thu hồi đốt CH4" -> rei.sumKNKAll,
          "Tổng KNK - phóng không CH4" -> rel.sumKNKAll
        )
      }.toJsArray

      val sumReiCH4 = ChartSerie("Tổng KNK - thu hồi đốt CH4", "blue")
      val sumRelCH4 = ChartSerie("Tổng KNK - phóng không CH4", "red")

      val chartSerie = js.Array(sumReiCH4, sumRelCH4)

      val chart = LineChart(data, (d: js.Dynamic) => d.t.asInstanceOf[Double], chartSerie)(otherProps): TagMod

      <.div(chart)
    }


    def render(d: GhgData) = {
      def d_bod(bod: Double) = d.copy(
        direct = d.direct.copy(
          d = d.direct.d.copy(
            streamIn = d.direct.d.streamIn.copy(so = bod)
          ),
          coef = d.direct.coef.copy(
            aerobic = d.direct.coef.aerobic.copy(t = 20)
          )
        )
      )

      <.div(
        <.h4("Biểu đồ biến thiên"),
        <.div(
          //MuiTextField(value = r)
        ),
        graph(50 to 400, d_bod),
        graph2(50 to 400, d_bod, true),
        graphTotal(50 to 400, d_bod, true)
      )
    }
  }

  val component = ReactComponentB[GhgData]("Graph")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
