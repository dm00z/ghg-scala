package es6

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native @JSName("Object")
object Object extends js.Object {
  /**
    * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/assign
    */
  def assign(target: js.Object, sources: js.Object*): js.Object = js.native
}
