package idbase

import java.util.Calendar

import play.api.templates.Html

object Helper {

  val version = 4

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString.capitalize)
    x zip x
  }

  def firstYear = 1995
  def thisYear = Calendar.getInstance().get(Calendar.YEAR)
  def yearsToNow: List[Int] = (thisYear to firstYear by -1).toList

  def row(th: String, td: Any) = rowHtml(th)(Html(td.toString))

  def rowHtml(th: String)(td: Html) = Html {
    if (td.body.trim.isEmpty) ""
    else """<tr><th class="muted">%s</th><td>%s</td></tr>""".format(th, td.body)
  }

  def group(name: String)(content: Html) = Html {
    """<section class="group">
  <h2 class="group-title">%s</h2>
  %s
</section>""".format(name, content)
  }

  def tableGroup(name: String)(content: Html) = group(name) {
    Html {
      """<table class="table"><tbody>%s</tbody></table>""".format(content)
    }
  }

  object Random {

    private val chars: IndexedSeq[Char] = (('0' to '9') ++ ('a' to 'z'))
    private val nbChars = chars.size

    def nextString(len: Int) = List.fill(len)(nextChar) mkString
    def nextChar = chars(scala.util.Random nextInt nbChars)
  }

  object Markdown {

    import org.pegdown._, Extensions._

    private val transformer = new PegDownProcessor(HARDWRAPS | AUTOLINKS | QUOTES)

    def apply(text: String): Html = Html {
      transformer.markdownToHtml(text)
    }

    def apply(text: Html): Html = apply(text.toString)
  }
}
