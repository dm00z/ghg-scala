package ghg.components

import chandu0101.scalajs.react.components.materialui._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.language.postfixOps
import scala.scalajs.js
import scala.scalajs.js.{UndefOr, `|`}
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import scalacss.mutable.StyleSheet.Inline

object LeftMenu {
  object Style extends Inline {
    import dsl._
    //border-top-style: none; overflow: hidden; position: absolute; top: 64px; width: 192px;
    val container = style(maxWidth(700 px))
    val content = style(display.flex,
      padding(30.px),
      flexDirection.column,
      alignItems.center)
  }


  case class State(isOpen: Boolean, value: Option[String]){
    def touched(us: UndefOr[String]) = us.fold(this){
      case s if value contains s => copy(value = None)
      case s => copy(value = Some(s))
    }
  }

  class Backend($: BackendScope[Unit, State]) {
    val toggleOpen: ReactEvent => Callback =
      e => $.modState(s => s.copy(isOpen = !s.isOpen))

    val onTouchTap: (ReactUIEventH, JsComponentM[MuiMenuItemProps, _, TopNode]) => Callback =
      (e, elem) => $.modState(_.touched(elem.props.value))

    def renderOpen(S: State) =
      <.div(Style.container,
        MuiMenu(
//          width          = 320: String | Int,
          value          = if(S.value.isEmpty) js.undefined else S.value.get: String | js.Array[String],
//          multiple       = false,
//          openDirection  = MuiMenuOpenDirection.TOP_LEFT,
          onItemTouchTap = onTouchTap,
          onEscKeyDown   = toggleOpen
        )(
          MuiMenuItem(primaryText = "1. Thông tin nhà máy", checked = true)(),
          MuiMenuDivider()(),
          MuiMenuItem(primaryText = "2. Gián tiếp")(),
          MuiMenuItem(primaryText = "2.1")(),
          MuiMenuItem(primaryText = "2.2")(),
          MuiMenuDivider()(),
          MuiMenuItem(primaryText = "3. Trực tiếp")(),
          MuiMenuItem(primaryText = "3.1")(),
          MuiMenuItem(primaryText = "3.2")(),
          MuiMenuItem(primaryText = "3.3")()
        ),
        MuiFlatButton(label = "<<", onClick = toggleOpen)()
      )
    def renderClosed(S: State) =
      MuiFlatButton(label = ">>", onClick = toggleOpen)()

    def render(S: State) =
      <.div(
        Style.content,
        if (S.isOpen) renderOpen(S) else renderClosed(S)
      )
  }

  val component = ReactComponentB[Unit] ("LeftMenu")
    .initialState(State(isOpen = true, None))
    .renderBackend[Backend]
    .buildU

  def apply() = component()
}
