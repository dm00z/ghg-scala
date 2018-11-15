package ghg.pages

import chandu0101.scalajs.react.components.materialui.{MuiDropDownMenuItem, MuiDropDownMenu}
import diode.react.ModelProxy
import ghg.components.{Electric1, Electric2, Electric3}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.ElectricData.{PowerSupply, CalcMethod}
import model.GhgData
import scala.scalajs.js, js.JSConverters._
import ghg.Utils._
import scala.scalajs.js.JSNumberOps._

object ElectricPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric)
  }

  final case class Backend($: BackendScope[Props, _]) {
    @inline def calcMethods = CalcMethod.values.map(m => MuiDropDownMenuItem(m.id.toString, m.toString)).toJSArray

    def render(P: Props) = {
      val my = P.my()

      val rows = my.powerStruct.supplies.zipWithIndex.map {
        case (supply, i) =>
          implicit val r: PowerSupply = supply
          implicit val dispatch: PowerSupply => Callback = d => {
            my.powerStruct.supplies.splitAt(i) match {
              case (r1, _ :: r2) => P.my.dispatch(my.powerStruct.copy(supplies = (r1 :+ d) ++ r2))
              case (r1, Nil) => P.my.dispatch(my.powerStruct.copy(supplies = r1 :+ d))
            }
          }

          <.tr(
            <.td(r.tpe),
            tdIntInput(PowerSupply.EFi),
            tdInput(PowerSupply.PFi),
            <.td(r.mul.toFixed(3)),
            <.td(r.ref)
          )
      }

      <.div(
        <.h2("2.1. Công suất điện năng sử dụng trong HTXLNT"),
        <.label("Cách tính công suất tiêu thụ điện năng: "),
        MuiDropDownMenu(
          menuItems = calcMethods,
          selectedIndex = my.method.id,
          onChange = (_: ReactEventI, i: Int, _: js.Any) => P.dispatch(CalcMethod(i))
        )(),
        my.method match {
          case CalcMethod.Method1 => Electric1(P)
          case CalcMethod.Method2 => Electric2(P)
          case CalcMethod.Method3 => Electric3(P)
        },
        <.h2("2.2. Hệ số phát thải khí nhà kính từ sản xuất điện năng"),
        table(
          <.th("Nguồn điện"),
          <.th("Hệ số phát thải EFi (g", <.sub("CO2-td"), "/Kwh)"),
          <.th("Cơ cấu nguồn điện PFi (%)"),
          <.th("PFi * EFi"),
          <.th("Tài liệu tham khảo")
        )(
          rows :+ <.tr(
            <.td(^.colSpan := 3, "Tổng"),
            <.td(my.powerStruct.totalRatio.toFixed(3)),
            <.td()
          ) :_*
        ),
        <.h2("2.3. Phát thải KNK từ tiêu thụ điện năng sử dụng trong HTXLNT"), // = ${P().ghgElectric.toFixed(3)} (kg", <.sub("CO2-td"), "/day)"
        <.div("Công thức tính: `P_(CO2,điện) = QE * tổng(PF_i * EF_i)`",
          dataTbl(
            tr("P", "CO2, điện", P().ghgElectric, "gCO2/ngày"), //Lượng SS bị khử trong bể lắng sơ cấp (g/day)
            tr("QE", "", P().electricPower, "kwh/ngày"), //Công suất dòng vào ban đầu
            tr("Tổng (PFi * EFi)", "", my.powerStruct.totalRatio, "gC02/kwh")
          )
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Electric")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
