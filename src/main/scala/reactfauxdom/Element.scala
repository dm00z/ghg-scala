package reactfauxdom

import japgolly.scalajs.react.ReactDOMElement
import org.scalajs.dom
import scala.scalajs.js

@js.native
abstract class Element extends dom.Element {
  def toReact(index: Int = 0): ReactDOMElement = js.native
}
