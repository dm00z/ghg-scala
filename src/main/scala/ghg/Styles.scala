package ghg

import scalacss.mutable.StyleSheet
import scalacss.Defaults._

object Styles extends StyleSheet.Standalone {
  import dsl._, svg.Dsl._
  @inline def borderCls = "border"

  s".$borderCls, .$borderCls tr, .$borderCls td, .$borderCls th" - (
    border(1.px, solid, black),
    borderCollapse.collapse
  )

  "h2" - fontSize(1.2.em)

  ".visitors .sparkline" - (
    fill.none,
    stroke(steelblue),
    strokeWidth(1.5.px)
  )

  "body" - (
    margin.`0`,
    position.relative,
    height :=! "100%"
  )

  "table" - (
    marginTop(10.px),
    borderCollapse.collapse,
    borderSpacing(2.px),
    width :=! "100%",
    backgroundColor.white,
    borderRadius(5.px),
    overflow.hidden,
    boxShadow := "0 0px 40px 0px rgba(0, 0, 0, 0.15)"
  )

  "tr" - (
    display.tableRow,
    verticalAlign.inherit,
    borderColor.inherit
  )

  "th, td" - (
    //fontWeight.unset,
    paddingRight(10.px),
    borderBottom(2.px, solid),
    paddingTop(5.px),
    paddingBottom(5.px),
    paddingLeft(20.px)
  )

  "input" - (
    fontSize(14.px),
    //padding(5.px),
    borderTopColor.transparent,
    borderLeftColor.transparent,
    borderRightColor.transparent,
    borderBottomColor :=! ("#21A8DE"),
    borderBottomWidth(1.px),
    backgroundColor.transparent
    //width :=! "15%"
  )

  "hr" - (
    margin.`0`,
    borderStyle.solid,
    borderTopWidth(0.5.px),
    borderTopColor :=! "#0275d8"
  )

  "#container h1" - (
    textAlign.center,
    backgroundColor.white,
    fontSize.large,
    marginTop(-40.px)
  )

  ".note-area" - (
    float.left,
    padding(5.px),
    width :=! "30%",
    maxWidth :=! "30%",
    border(2.px, solid, rgb(223, 220, 220)),
    marginTop(20.px),
    height(207.px)
  )

  ".note-area label" - (
    //fontStyle.italic,
    fontWeight.bold
  )

  ".graph-area" - (
    float.right,
    padding(5.px),
    width :=! "65%",
    maxWidth :=! "65%",
    border(2.px, solid, rgb(223, 220, 220)),
    marginTop(20.px),
    height(207.px)
  )

  ".graph-area label" - (
    //fontStyle.italic,
    fontWeight.bold
  )
}
