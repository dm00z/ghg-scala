package tex

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js

object TeX {
  type Props = String

  object Backend {
    private[this] var id = 0
    private def nextId = { id += 1; id }
  }
  case class Backend($: BackendScope[Props, _]) {
    private lazy val id = "tex" + Backend.nextId
    def didMount() = Callback {
      MathJax.Hub.Queue(js.Array("Typeset", MathJax.Hub, id))
    }
    def render(P: Props) = <.span(^.id := id, ^.dangerouslySetInnerHtml(P))
  }

  val component = ReactComponentB[Props]("TeX")
    .renderBackend[Backend]
    .componentDidMount(_.backend.didMount())
    .build

  def apply(text: String) = component(text)

//  def config() = {
//    MathJax.Hub.Config(js.Dynamic.literal(
//      config = js.Array("MMLorHTML.js"),
//      jax = js.Array("input/AsciiMath","output/HTML-CSS","output/NativeMML", "output/CommonHTML"),
//      extensions = js.Array("asciimath2jax.js","MathMenu.js","MathZoom.js", "CHTML-preview.js"),
//      elements = js.Array()
//    ))
//  }

  @inline implicit class String2Tex(val s: String) extends AnyVal {
    @inline def teX = TeX(s)
    @inline def teXb = <.b(TeX(s))
//    @inline def tdX = <.td(TeX(s))
//    @inline def tdXb = <.td(<.b(TeX(s)))
  }
}
