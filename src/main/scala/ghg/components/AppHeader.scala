package ghg.components

import chandu0101.scalajs.react.components.materialui._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{SampleData, Plant}
import scala.scalajs.js.Dynamic.{literal => jsObj}

object AppHeader {
  lazy val txtStyle = jsObj(width = "600px")

  val component = ReactComponentB[Props]("AppHeader")
    .stateless
    .renderBackend[Backend]
    .build

  case class Props(plant: ModelProxy[Plant], group: String, subGroup: Option[String], readonly: Boolean = true)

  class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val f = P.plant()
      <.div(
        <.h1("Tính toán phát thải khí nhà kính từ hệ thống xử lý nước thải"),
        if (P.readonly) EmptyTag else <.div(
          <.label("Tải dữ liệu: "),
          <.select(
            <.option("Nhà máy xử lý nước thải sinh hoạt Yên Sở",
              ^.value := "yen_so", ^.selected := "selected"),
            <.option("Nhà máy xử lý nước thải công ty Giấy Bãi Bằng",
              ^.value := "bai_bang"),
            ^.onChange ==> { e: ReactEventI =>
              e.target.value match {
                case "yen_so" => P.plant.dispatch(SampleData.dataYenSo)
                case "bai_bang" => P.plant.dispatch(SampleData.dataBaiBang)
              }
            }
          )
        ),
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
