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
}
