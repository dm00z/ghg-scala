package ghg.components

import chandu0101.scalajs.react.components.materialui._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.Plant
import scala.scalajs.js.Dynamic.{literal => jsObj}

object AppHeader {
  val component = ReactComponentB[Props]("AppHeader")
    .stateless
    .renderBackend[Backend]
    .build

  case class Props(plant: ModelProxy[Plant], group: String, subGroup: Option[String], readonly: Boolean = true)

  class Backend($: BackendScope[Props, _]) {
    private lazy val txtStyle = jsObj(width = "600px")
    def render(P: Props) = {
      val f = P.plant()
      <.div(
        <.h1("Tính toán phát thải khí nhà kính từ hệ thống xử lý nước thải"),
        <.div(<.label("Loại nước thải: "),
          if(P.readonly) f.tpe
          else MuiTextField(value = f.tpe, style = txtStyle, onChange = {e: ReactEventI => P.plant.dispatch(P.plant().copy(tpe = e.target.value))})()),
        <.div(<.label("Tên nhà máy: "),
          if(P.readonly) f.name
          else MuiTextField(value = f.name, style = txtStyle, onChange = { e: ReactEventI => P.plant.dispatch(P.plant().copy(name = e.target.value))})()),
        <.div(<.label("Địa điểm: "),
          if(P.readonly) f.addr
          else MuiTextField(value = f.addr, style = txtStyle, onChange = {e: ReactEventI => P.plant.dispatch(P.plant().copy(addr = e.target.value))})()),
        <.div(<.label("Hạng mục: "), P.group),
        P.subGroup.fold(EmptyTag)(v => <.div(<.label("Tiểu mục: "), v))
      )
    }
  }
}
