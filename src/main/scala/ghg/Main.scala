package ghg

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._

object Main extends JFXApp {
  private val leftTree = new LeftTree
  private val rightPane = new ScrollPane
  private val view = new SplitPane {
    items.addAll(leftTree.delegate, rightPane.delegate)
    dividerPositions = 0.4
  }
  stage = new PrimaryStage {
    width = 1024
    title = "Tính toán phát thải khí nhà kính từ hệ thống xử lý nước thải"
    scene = new Scene { root = view }
    delegate setMaximized true
  }

  rightPane.content = new GeneralInfo
}
