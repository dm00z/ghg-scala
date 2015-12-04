package ghg

import chandu0101.scalajs.react.components._
import ghg.components.{AppHeader, LeftNavPage, LeftNav}
import scalacss.mutable.GlobalRegistry
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object AppCSS {
  def load() = {
    GlobalRegistry.register(LeftNav.Style,
      LeftNavPage.Style,
//      MuiPaperDemo.Style,
//      MuiSwitchesDemo.Style,
//      MuiMenuDemo.Style,
//      MobileTearSheet.Style,
      ReactTable.DefaultStyle,
      ReactListView.DefaultStyle,
//      ReactSearchBox.DefaultStyle,
      Pager.DefaultStyle
//      ScalaCSSTutorial.Style,
//      InfoTemplate.Style,
//      ReactInfiniteDemo.styles,
//      ReactDraggable.Style,
//      MuiTabsDemo.Style
    )
    GlobalRegistry.addToDocumentOnRegistration()
  }
}
