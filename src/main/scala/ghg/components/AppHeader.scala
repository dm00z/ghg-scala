package ghg.components

import chandu0101.scalajs.react.components.materialui._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{GhgData, WaterType, SampleData, KineticRelationData}
import scala.scalajs.js.Dynamic.{literal => jsObj}

object AppHeader {
  lazy val txtStyle = jsObj(width = "600px")

  val component = ReactComponentB[Props]("AppHeader")
    .stateless
    .renderBackend[Backend]
    .build

  case class Props(d: ModelProxy[GhgData], group: String, subGroup: Option[String], readonly: Boolean = true)

  class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val f = P.d().info.f
      <.div(
        <.div(
          <.h1("MÔ HÌNH TÍNH TOÁN PHÁT THẢI KHÍ NHÀ KÍNH TỪ HỆ THỐNG XỬ LÝ NƯỚC THẢI")
        ),
        if(P.readonly) ^.backgroundColor := "#e4e5e6" else EmptyTag,
        ^.padding := "20px", ^.borderRadius := "5px", ^.width := "82%", ^.color := "#1254d6",

        if (P.readonly) EmptyTag else <.div(
          <.label("Tải dữ liệu: "),
          <.select(
            SampleData.all.map {
              case (k, d) if k == SampleData.selected =>
                <.option(d.info.f.name, ^.value := k, ^.selected := "selected")
              case (k, d) =>
                <.option(d.info.f.name, ^.value := k)
            },
            ^.onChange ==> { e: ReactEventI =>
              SampleData.selected = e.target.value
              P.d.dispatch(SampleData.data)
            }
          )
        ),
//        <.div(<.label("Loại nước thải: "),
//          if(P.readonly) f.tpe.toString
//          else <.select(
//            WaterType.values.map { tpe =>
//              if (tpe == f.tpe)
//                <.option(tpe.toString, ^.value := tpe.id, ^.selected := "selected")
//              else
//                <.option(tpe.toString, ^.value := tpe.id)
//            },
//            ^.onChange ==> { e: ReactEventI =>
//              val fNew = f.copy(tpe = WaterType(e.target.value.toInt))
//              val relationNew = fNew.tpe match {
//                case WaterType.Domestic => KineticRelationData.dataDomestic
//                case WaterType.Industrial => KineticRelationData.dataIndustrial
//              }
//              val d = P.d()
//              val dNew = d.copy(
//                info = d.info.Nghiên cứu sinhcopy(f = fNew),
//                direct = d.direct.copy(relation = relationNew)
//              )
//              P.d.dispatch(dNew)
//            }
//          )
//        ),
        <.div(<.label("Tên nhà máy: "),
          if(P.readonly) f.name
          else MuiTextField(value = f.name, style = txtStyle, onChange = { e: ReactEventI => P.d.dispatch(f.copy(name = e.target.value))})()
        ),
        <.div(<.label("Địa điểm: "),
          if(P.readonly) f.addr
          else MuiTextField(value = f.addr, style = txtStyle, onChange = {e: ReactEventI => P.d.dispatch(f.copy(addr = e.target.value))})()
        ),
        <.div(<.label("Hạng mục: "), P.group),
        P.subGroup.fold(EmptyTag)(v => <.div(<.label("Tiểu mục: "), v))
      )
    }
  }
}
