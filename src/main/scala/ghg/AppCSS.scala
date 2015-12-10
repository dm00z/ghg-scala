package ghg

import chandu0101.scalajs.react.components._
import ghg.components.{KRData, LeftNav}
import ghg.pages.GhgPage
import org.scalajs.dom.raw.HTMLStyleElement
import scalacss.ScalaCssReactFns
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

    ScalaCssReactFns.installStyle(KRData.Styles.render[HTMLStyleElement])
  }
}
