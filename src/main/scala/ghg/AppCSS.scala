package ghg

import chandu0101.scalajs.react.components._
import ghg.components.LeftNav
import ghg.pages.GhgPage
import scalacss.mutable.GlobalRegistry
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object AppCSS {
  def load() = {
    GlobalRegistry.register(
      LeftNav.Style,
      GhgPage.Style,
      ReactTable.DefaultStyle,
      ReactListView.DefaultStyle,
      Pager.DefaultStyle
    )
    GlobalRegistry.addToDocumentOnRegistration()
  }
}
