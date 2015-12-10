package ghg

import scalacss.mutable.StyleSheet
import scalacss.Defaults._

object Styles extends StyleSheet.Standalone {
  import dsl._
  @inline def kr = "krdata"

  s".$kr, .$kr tr, .$kr td" - (
    border(1.px, solid, black),
    borderCollapse.collapse
  )
}
