package model

import scala.collection.mutable.ListBuffer

class RK4(f: (Double, Double) => Double) {
  var result: ListBuffer[Double] = new ListBuffer[Double]

  def solve(counter: Double, h: Double, x: Double, y: Double): Unit = {
    if (counter > 0) {
      val dy1 = h * f(x, y)
      val dy2 = h * f(x + h / 2, y + dy1 / 2)
      val dy3 = h * f(x + h / 2, y + dy2 / 2)
      val dy4 = h * f(x + h, y + dy3)

      val yn = y + (dy1 + 2 * dy2 + 2 * dy3 + dy4) / 6
      val t = x + h

      result += yn

      solve(counter - 1, h, x, y)

    }
  }
}
