package idbase

import java.util.Calendar

object Helper {

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString.capitalize) 
    x zip x
  }

  def yearsToNow: List[Int] = {
    val y = Calendar.getInstance().get(Calendar.YEAR)
    (y to 1995 by -1).toList
  }
}
