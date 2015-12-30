package reactfauxdom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("ReactFauxDOM")
@js.native
object ReactFauxDOM extends js.Object {
  def createElement(nodeName: String): Element = js.native
  def createElementNS(namespace: String, nodeName: String): Element = js.native
  def compareDocumentPosition(): Int = js.native
}
