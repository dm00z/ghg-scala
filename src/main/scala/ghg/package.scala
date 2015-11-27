import org.slf4j.LoggerFactory

import scalafx.beans.property.StringProperty
import scalafx.scene.text.Text

package object ghg {
  val logger = LoggerFactory.getLogger("app")

  implicit class StringPropEx(val p: StringProperty) extends AnyVal {
    def createText() = {
      val t = new Text(p.value)
      t.text <== p
      t.delegate
    }
  }
}
