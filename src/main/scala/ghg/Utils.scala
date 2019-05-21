package ghg

import japgolly.scalajs.react._
import model.{C, Col, GraphData, R}
import monocle._
import org.scalajs.dom.html
import org.widok.moment.Moment

import scala.scalajs.js
import scala.scalajs.js.JSNumberOps._
import spray.json._
import DefaultJsonProtocol._

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
  def table(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",<.col(^.width := "34%"),<.col(^.width := "33%"),<.col(^.width := "33%"), <.tbody(xs: _*))
  def table7(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",
    <.colgroup(
    <.col(^.width := "4%"),
    <.col(^.width := "4%"),
    <.col(^.width := "34%"),
    <.col(^.width := "14%"),
    <.col(^.width := "14%"),
    <.col(^.width := "14%"),
    <.col(^.width := "14%")),
    <.tbody(xs: _*))
  def table4(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",
    <.colgroup(
    <.col(^.width := "32%"),
    <.col(^.width := "25%"),
    <.col(^.width := "25%"),
    <.col(^.width := "18%")),
    <.tbody(xs: _*))
  def table3(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",
    <.colgroup(
    <.col(^.width := "31%"),
    <.col(^.width := "25%"),
    <.col(^.width := "25%")),
    <.tbody(xs: _*))
  def table5(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",
    <.colgroup(
    <.col(^.width := "20%"),
    <.col(^.width := "20%"),
    <.col(^.width := "20%"),
    <.col(^.width := "20%"),
    <.col(^.width := "20%")),
    <.tbody(xs: _*))
  def table6(xs: TagMod*) = <.table(^.className := Styles.borderCls,^.className := "dataTable",
    <.colgroup(
    <.col(^.width := "16.6%"),
    <.col(^.width := "16.6%"),
    <.col(^.width := "16.6%"),
    <.col(^.width := "16.6%"),
    <.col(^.width := "16.6%")),
    <.tbody(xs: _*))
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
      <.tr(<.td(^.colSpan := 3, ^.color := "white",^.backgroundColor := "#145dbf", <.b("Dữ liệu:"))),
      dataRows,
      <.tr(<.td(^.colSpan := 3, ^.color := "white",^.backgroundColor := "#145dbf", <.b("Kết quả tính toán:"))),
      result
    )

  def dataTbl(numResult: Int, rows: TagMod*) =
    rows.splitAt(numResult) match {
      case (results, dataRows) =>
        table(
          <.tr(<.td(^.colSpan := 3, ^.color := "white",^.backgroundColor := "#145dbf", <.b("Dữ liệu:"))),
          dataRows,
          <.tr(<.td(^.colSpan := 3, ^.color := "white",^.backgroundColor := "#145dbf", <.b("Kết quả tính toán:"))),
          results
        )
    }

  //<iframe src='//charts.hohli.com/embed.html?#w=640&h=480&d=
//   {
//     "containerId":"chart",
//     "dataTable":{
//        "cols":[
//           {
//              "label":"A",
//              "type":"string"
//           },
//           {
//              "label":"B",
//              "type":"number"
//           }
//        ],
//        "rows":[
//           {
//              "c":[
//                 {
//                    "v":"Science Fiction"
//                 },
//                 {
//                    "v":217
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"General Science"
//                 },
//                 {
//                    "v":203
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"Computer Science"
//                 },
//                 {
//                    "v":175
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"History"
//                 },
//                 {
//                    "v":155
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"General Fiction"
//                 },
//                 {
//                    "v":72
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"Fantasy"
//                 },
//                 {
//                    "v":51
//                 }
//              ]
//           },
//           {
//              "c":[
//                 {
//                    "v":"Law"
//                 },
//                 {
//                    "v":29
//                 }
//              ]
//           }
//        ]
//     },
//     "options":{
//        "width":640,
//        "height":480
//     },
//     "state":{
//
//     },
//     "isDefaultVisualization":true,
//     "chartType":"PieChart"
//  }' frameborder='0' width='650' height='490'></iframe>

  case class ResultPercentage(name: String, value: Double)

  def PieChartBuilder(results: List[(String, Double)], width: Int, height: Int) = {
    var result: (String, Double) = ("", 0)

    var data: String = ""

    var i = 0
    for (result <- results) {
      i += 1
      if (i == results.length) {
        data += "{\"c\":[{\"v\":\""+ result._1 +"\"},{\"v\":"+ result._2 +"}]}"
      } else {
        data += "{\"c\":[{\"v\":\""+ result._1 +"\"},{\"v\":"+ result._2 +"}]},"
      }
    }

    <.div(<.iframe(^.src := "http://charts.hohli.com/embed.html?created=1548273688850#w="+width+"&h="+height+"&d=" +
      "{\"containerId\":\"chart\",\"dataTable\":{\"cols\":[{\"label\":\"A\",\"type\":\"string\"},{\"label\":\"B\",\"type\":\"number\"}]," +
      "\"rows\":[" +
        data +
        "]" +
      "}" +
      ",\"options\":{\"width\":"+width+",\"height\":"+height+"},\"state\":{},\"isDefaultVisualization\":true,\"chartType\":\"PieChart\"}", ^.frameBorder := 0, ^.width := width+10, ^.height := height+10
    ))
  }

  def BarChartBuilder(results: List[(String, Double)], title: String, leftTitle: String, botTitle: String, width: Int, height: Int) = {
    var result: (String, Double) = ("", 0)

    var data: String = ""

    var i = 0
    for (result <- results) {
      i += 1
      if (i == results.length) {
        data += "{\"c\":[{\"v\":\""+ result._1 +"\"},{\"v\":"+ result._2 +"}]}"
      } else {
        data += "{\"c\":[{\"v\":\""+ result._1 +"\"},{\"v\":"+ result._2 +"}]},"
      }
    }

    /*
    <iframe src='//charts.hohli.com/embed.html?created=1548652577834#w=640&h=480&d={"containerId":"chart","dataTable":{"cols":[{"label":"A","type":"string","p":{}},{"label":"B","type":"number"}],"rows":[{"c":[{"v":"Science Fiction"},{"v":217}]},{"c":[{"v":"General Science"},{"v":203}]},{"c":[{"v":"Computer Science"},{"v":175}]},{"c":[{"v":"History"},{"v":155}]},{"c":[{"v":"General Fiction"},{"v":72}]},{"c":[{"v":"Fantasy"},{"v":51}]},{"c":[{"v":"Law"},{"v":29}]}]},"options":{"hAxis":{"useFormatFromData":true,"viewWindow":null,"minValue":null,"maxValue":null,"viewWindowMode":null,"title":"Ngu\u1ed3n ph\u00e1t th\u1ea3i","slantedText":true,"slantedTextAngle":30},"legacyScatterChartLabels":true,"vAxes":[{"useFormatFromData":true,"viewWindow":{"max":null,"min":null},"minValue":null,"maxValue":null,"logScale":false,"title":"t\u1ea5n CO2/n\u0103m","minorGridlines":{"count":"0"},"formatOptions":{"source":"data","scaleFactor":null}},{"useFormatFromData":true,"viewWindow":{"max":null,"min":null},"minValue":null,"maxValue":null,"logScale":false}],"isStacked":false,"booleanRole":"certainty","legend":"none","width":640,"height":480,"title":"Thu h\u1ed3i v\u00e0 \u0111\u1ed1t","domainAxis":{"direction":1},"series":{"0":{"errorBars":{"errorType":"none"},"targetAxisIndex":0}}},"state":{},"view":{"columns":null,"rows":null},"isDefaultVisualization":false,"chartType":"ColumnChart"}' frameborder='0' width='650' height='490'></iframe>
     */

    <.div(<.iframe(^.src := "http://charts.hohli.com/embed.html?created=1548273688850#w="+width+"&h="+height+"&d=" +
      "{\"containerId\":\"chart\",\"dataTable\":{\"cols\":[{\"label\":\"A\",\"type\":\"string\"},{\"label\":\"Khí nhà kính\",\"type\":\"number\"}]," +
      "\"rows\":[" +
      data +
      "]" +
      "}" +
      ",\"options\":{\"hAxis\":{\"useFormatFromData\":true,\"viewWindow\":null,\"minValue\":null,\"maxValue\":null,\"viewWindowMode\":null,\"title\":\""+botTitle+"\",\"slantedText\":false,\"slantedTextAngle\":0},\"legacyScatterChartLabels\":true,\"vAxes\":[{\"useFormatFromData\":true,\"viewWindow\":{\"max\":null,\"min\":null},\"minValue\":null,\"maxValue\":null,\"logScale\":false,\"title\":\""+leftTitle+"\",\"minorGridlines\":{\"count\":\"0\"},\"formatOptions\":{\"source\":\"data\",\"scaleFactor\":null}},{\"useFormatFromData\":true,\"viewWindow\":{\"max\":null,\"min\":null},\"minValue\":null,\"maxValue\":null,\"logScale\":false}],\"isStacked\":false,\"booleanRole\":\"certainty\",\"legend\":\"none\",\"width\":"+width+",\"height\":"+height+",\"title\":\""+title+"\",\"domainAxis\":{\"direction\":1},\"series\":{\"0\":{\"errorBars\":{\"errorType\":\"none\"},\"targetAxisIndex\":0}}},\"state\":{},\"view\":{\"columns\":null,\"rows\":null},\"isDefaultVisualization\":false,\"chartType\":\"ColumnChart\"}", ^.frameBorder := 0, ^.width := width+10, ^.height := height+10
    ))
  }
}
