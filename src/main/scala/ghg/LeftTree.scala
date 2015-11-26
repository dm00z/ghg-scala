package ghg

import scalafx.geometry.Insets
import scalafx.scene.control.{TreeItem, TreeView, ScrollPane}

class LeftTree extends ScrollPane {
  content = new TreeView(
    new TreeItem(""){
      children = Seq(
        new TreeItem("0. Thông tin nhà máy"),
        new TreeItem("1. Gián tiếp"),
        new TreeItem("2. Trực tiếp")
      )
      expanded = true
    }
  ){
    showRoot = false
  }
  prefWidth = 500
  maxWidth = 500
  fitToWidth = true
  fitToHeight = true
  margin = Insets(0, 0, 0, -15)
}
