package idbase

import java.util.Calendar

object Helper {

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString.capitalize)
    x zip x
  }

  def firstYear = 1995
  def thisYear = Calendar.getInstance().get(Calendar.YEAR)
  def yearsToNow: List[Int] = (thisYear to firstYear by -1).toList

  def row(th: String, td: Any) =
    "<tr><th>%s</th><td>%s</td></tr>".format(th, td)

  object Random {

    private val chars: IndexedSeq[Char] = (('0' to '9') ++ ('a' to 'z'))
    private val nbChars = chars.size

    def nextString(len: Int) = List.fill(len)(nextChar) mkString
    def nextChar = chars(scala.util.Random nextInt nbChars)
  }
}
