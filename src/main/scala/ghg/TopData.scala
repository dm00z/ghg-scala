package ghg

import scalafx.beans.property.StringProperty

/** @param _subGroup == null if not exist */
class TopData(_tpe: String = "", _name: String = "", _addr: String = "", _group: String = "", _subGroup: String = null) {
  val tpe = new StringProperty(this, "Loại nước thải", _tpe)
  val name = new StringProperty(this, "Tên nhà máy", _name)
  val addr = new StringProperty(this, "Địa điểm", _addr)
  val group = new StringProperty(this, "Hạng mục", _group)
  val subGroup = new StringProperty(this, "Tiểu mục", _subGroup)
}
