package ghg.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.Plant

object AppHeader {
  val component = ReactComponentB[Props]("AppHeader")
    .stateless
    .renderBackend[Backend]
    .build

  @inline def apply(plant: ModelProxy[Plant], group: String, subGroup: Option[String]) =
    component(Props(plant, group, subGroup))

  case class Props(plant: ModelProxy[Plant], group: String, subGroup: Option[String]) {
    def divs = {
      val f = plant()
      val l = Seq(
        "Loại nước thải" -> f.tpe,
        "Tên nhà máy" -> f.name,
        "Địa điểm" -> f.addr,
        "Hạng mục" -> group
      )
      subGroup.map("Tiểu mục" -> _).fold(l)(l :+ _)
    }
  }

  class Backend(t: BackendScope[Props, _]) {
    def render(P: Props) = {
      val divs = P.divs.map {
        case (n, v) => <.div(<.label(n + ": "), v)
      }
      <.div(<.h1("Tính toán phát thải khí nhà kính từ hệ thống xử lý nước thải") +: divs :_*)
    }
  }
}
