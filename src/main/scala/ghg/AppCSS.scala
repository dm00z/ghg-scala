package ghg

import scalacss.mutable.GlobalRegistry

object AppCSS {
  def load() = {
    GlobalRegistry.register(/*???*/)
//    GlobalRegistry.addToDocumentOnRegistration()
  }
}
