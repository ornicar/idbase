package idbase

import java.util.Calendar
import play.api.templates.Html

object Helper {

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString.capitalize)
    x zip x
  }

  def firstYear = 1995
  def thisYear = Calendar.getInstance().get(Calendar.YEAR)
  def yearsToNow: List[Int] = (thisYear to firstYear by -1).toList

  def row(th: String, td: Any) = rowHtml(th)(Html(td.toString))

  def rowHtml(th: String)(td: Html) = Html {
    """<tr><th class="muted">%s</th><td>%s</td></tr>""".format(th, td.body)
  }

  def group(name: String)(content: Html) = Html {
    """
<div class="group">
  <span class="group-title">%s</span>
  <table class="table">
    <tbody>%s</tbody>
  </table>
</div>""".format(name, content)
  }

  object Random {

    private val chars: IndexedSeq[Char] = (('0' to '9') ++ ('a' to 'z'))
    private val nbChars = chars.size

    def nextString(len: Int) = List.fill(len)(nextChar) mkString
    def nextChar = chars(scala.util.Random nextInt nbChars)
  }
}
