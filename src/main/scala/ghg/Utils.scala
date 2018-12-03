package ghg

import japgolly.scalajs.react._
import model.R
import monocle._
import org.scalajs.dom.html
import org.widok.moment.Moment
import scala.scalajs.js
import scala.scalajs.js.JSNumberOps._

object Utils {
  val dateFormater = (d: js.Date) => Moment(d).format("DD/MM/YYYY")

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
//  implicit final class LightTeX(val sc: StringContext) extends AnyVal {
//    def t(args: Any*): TagMod = {
//      val s = sc.s()
//      val b = new StringBuilder
//      <.span()
//      s.foreach {
//        case '_' =>
//
//      }
//    }
//  }

  def table(th: ReactTagOf[html.TableHeaderCell]*)(xs: TagMod*) = <.table(
    ^.className := Styles.borderCls,
    <.thead(<.tr(th: _*)),
    <.tbody(xs: _*)
  )
  def table(xs: TagMod*) = <.table(^.className := Styles.borderCls, <.tbody(xs: _*))
  @inline def td(n: String, sub: String) = <.td(n, <.sub(sub))
  def td(n: String, sub: String, sup: String) = <.td(n, <.sup(sup), <.sub(^.marginLeft := "-10px", sub))

  @inline def tr(symbol: TagMod, data: Double) = <.tr(<.td(symbol), <.td(data.toFixed(3)), <.td())
  @inline def tr(symbol: TagMod, data: Double, unit: TagMod) = <.tr(<.td(symbol), <.td(data.toFixed(3)), <.td(unit))
  @inline def tr(symbol: String, sub: String, data: Double) = <.tr(td(symbol, sub), <.td(data.toFixed(3)), <.td())
  @inline def tr(symbol: String, sub: String, data: Double, unit: TagMod) = <.tr(td(symbol, sub), <.td(data.toFixed(3)), <.td(unit))
  @inline def tr(desc: String, data1: Double, data2: Double, percent: Double) = <.tr(<.td(desc), <.td(data1.toFixed(2)), <.td(data2.toFixed(2)), <.td((percent * 100).toFixed(2) + "%"))

  def input[D](lens: Lens[D, Double],
               validator: String => Boolean = _.decimal)
              (implicit d: D, dispatch: D => Callback) =
    <.input(
      ^.value := lens.get(d).toFixed(2),
      ^.onChange ==> { e: ReactEventI =>
        val valid = validator(e.target.value)
        Callback.ifTrue(valid, dispatch(lens.set(e.target.value.toDouble)(d)))
      })

  @inline def tdInput[D](lens: Lens[D, Double],
                         validator: String => Boolean = _.decimal)
                        (implicit d: D, dispatch: D => Callback) =
    <.td(input(lens, validator)(d, dispatch))

  //def tdInput[D]()

  //@inline def tdInput[D]()


  def tdStrInput[D](lens: Lens[D,String])(implicit d: D, dispatch: D => Callback) =
    <.td(<.input(
      ^.value := lens.get(d),
      ^.onChange ==> { e: ReactEventI =>
        dispatch(lens.set(e.target.value)(d))
      }
    ))

  def tdIntInput[D](lens: Lens[D,Int])(implicit d: D, dispatch: D => Callback) =
    <.td(<.input(
      ^.value := lens.get(d),
      ^.onChange ==> { e: ReactEventI =>
        dispatch(lens.set(e.target.value.toInt)(d))
      }
    ))

  def Day1 = <.span("day", <.sup("-1"))
  def Ngay1 = <.td("day", <.sup("-1"))
  def ThongSo = <.td(<.b("Thông số"))
  def KyHieu = <.td(<.b("Ký hiệu"))
  def DonVi = <.td(<.b("Đơn vị"))
  def GhiChu = <.td(<.b("Ghi chú"))
  def Khoang = <.td(<.b("Khoảng dao động"))
  def GiaTri = <.td(<.b("Giá trị"))
  def Muy = <.span("μ", <.sub("m"))

  implicit final class OptionEx[T](val o: Option[T]) extends AnyVal {
    def ?(f: T => TagMod): TagMod = o.fold(EmptyTag)(f)
  }

  def dataTbl(result: TagMod, dataRows: TagMod*) =
    table(
      <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
      dataRows,
      <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
      result
    )

  def dataTbl(numResult: Int, rows: TagMod*) =
    rows.splitAt(numResult) match {
      case (results, dataRows) =>
        table(
          <.tr(<.td(^.colSpan := 3, <.b("Dữ liệu:"))),
          dataRows,
          <.tr(<.td(^.colSpan := 3, <.b("Kết quả tính toán:"))),
          results
        )
    }
}
