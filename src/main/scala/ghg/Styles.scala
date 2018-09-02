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
    width :=! "85%",
    backgroundColor.white,
    borderRadius(5.px),
    overflow.hidden,
    boxShadow := ("0 0px 40px 0px rgba(0, 0, 0, 0.15)")
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
    paddingTop(15.px),
    paddingBottom(15.px),
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
  )

  "hr" - (
    margin.`0`,
    borderStyle.solid,
    borderTopWidth(0.5.px),
    borderTopColor :=! "#0275d8"
  )
}
