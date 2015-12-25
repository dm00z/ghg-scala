package ghg

import japgolly.scalajs.react._
import model.R
import monocle._
import org.scalajs.dom.html

object Utils {
  val decimalRegex = """^\-?[0-9]*\.?[0-9]+$"""
  implicit final class StringValidator(val v: String) extends AnyVal {
    @inline def decimal = v matches decimalRegex
    @inline def >#(x: Double) = v.matches(decimalRegex) && v.toDouble > x
    @inline def >=#(x: Double) = v.matches(decimalRegex) && v.toDouble >= x
    @inline def <#(x: Double) = v.matches(decimalRegex) && v.toDouble < x
    @inline def <=#(x: Double) = v.matches(decimalRegex) && v.toDouble <= x
    @inline def between(r: R[Double]) = v.matches(decimalRegex) && { val n = v.toDouble; n >= r.min && n <= r.max }
    @inline def >|(x: Double): Double = v >| (x, x)
    @inline def >=|(x: Double): Double = v >=| (x, x)
    @inline def >|(x: Double, d: Double): Double = if (v ># x) v.toDouble else d
    @inline def >=|(x: Double, d: Double): Double = if (v >=# x) v.toDouble else d
    @inline def <|(x: Double): Double = v <| (x, x)
    @inline def <=|(x: Double): Double = v <=| (x, x)
    @inline def <|(x: Double, d: Double): Double = if (v <# x) v.toDouble else d
    @inline def <=|(x: Double, d: Double): Double = if (v <=# x) v.toDouble else d
  }

  import japgolly.scalajs.react.vdom.prefix_<^._
  def table(th: ReactTagOf[html.TableHeaderCell]*)(xs: TagMod*) = <.table(
    ^.className := Styles.borderCls,
    <.thead(<.tr(th: _*)),
    <.tbody(xs: _*)
  )
  def table(xs: TagMod*) = <.table(^.className := Styles.borderCls, <.tbody(xs: _*))
  @inline def td(n: String, sub: String) = <.td(n, <.sub(sub))
  def td(n: String, sub: String, sup: String) = <.td(n, <.sup(sup), <.sub(^.marginLeft := "-10px", sub))
  def tdInput[D](lens: Lens[D, Double],
                 validator: String => Boolean = _.decimal)
                (implicit d: D, dispatch: D => Callback) =
    <.td(<.input(
      ^.value := lens.get(d),
      ^.onChange ==> { e: ReactEventI =>
        val valid = validator(e.target.value)
        Callback.ifTrue(valid, dispatch(lens.set(e.target.value.toDouble)(d)))
      })
    )
  def Ngay1 = <.td("day", <.sup("-1"))
  def ThongSo = <.td(<.b("Thông số"))
  def KyHieu = <.td(<.b("Ký hiệu"))
  def DonVi = <.td(<.b("Đơn vị"))
  def Khoang = <.td(<.b("Khoảng dao động"))
  def GiaTri = <.td(<.b("Giá trị"))
  def Muy = <.span("μ", <.sub("m"))
}
