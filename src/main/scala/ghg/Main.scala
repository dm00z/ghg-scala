package ghg

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._

object Main extends JFXApp {
  private val leftTree = new LeftTree
  private val righPane = new RightPane
  private val view = new SplitPane {
    items.addAll(leftTree.delegate, righPane.delegate)
    dividerPositions = 0.4
  }
  stage = new PrimaryStage {
    width = 1024
    title = "Green House Gas"
    scene = new Scene { root = view }
    delegate setMaximized true
  }
}
