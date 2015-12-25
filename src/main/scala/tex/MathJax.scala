package tex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("MathJax")
@js.native
object MathJax extends js.Object {
  @js.native
  object Hub extends js.Object {
    def Queue(array: js.Array[js.Any]): Unit = js.native

    def Config(o: js.Dynamic): Unit = js.native
  }
}
