package reactd3

import scala.scalajs.js
import scala.scalajs.js.UndefOr

@js.native
trait ChartSerie extends js.Object {
  //  val `type`: js.Array[js.Object]
  val field: String
  val color: UndefOr[String]
  val name: UndefOr[String]
}

object ChartSerie {
  def apply(field: String,
            color: UndefOr[String] = js.undefined,
            name: UndefOr[String] = js.undefined): ChartSerie =
    js.Dynamic
      .literal("field" -> field, "color" -> color, "name" -> name)
      .asInstanceOf[ChartSerie]
}