package svg

import scalacss.Attr
import scalacss.Literal.{Typed => LT}
import scalacss.ValueT.{TypedAttr_Length, TypedAttr_Color}

object Dsl {
  @inline def fill = Attrs.fill

  @inline def stroke = Attrs.stroke

  @inline def strokeWidth = Attrs.strokeWidth
}

object Attrs {
  /**
    * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/fill">MDN</a>
    */
  object fill extends TypedAttr_Color {
    override val attr = Attr.real("fill")
    @inline def none      = avl(LT.none)
  }

  /**
    * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke">MDN</a>
    */
  object stroke extends TypedAttr_Color {
    override val attr = Attr.real("fill")
    @inline def none      = avl(LT.none)
  }

  /**
    * The width CSS property specifies the width of the content area of an element. The content area is inside the padding, border, and margin of the element.
    *
    * @see <a href="https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/stroke-width">MDN</a>
    */
  object strokeWidth extends TypedAttr_Length {
    override protected def attr2 = Attr.real("stroke-width", _)
  }
}