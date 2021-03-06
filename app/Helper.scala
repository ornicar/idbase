package idbase

import controllers.routes
import java.text.Normalizer
import java.util.Calendar

import play.twirl.api.Html

object Helper {

  val version = 18

  def slugify(input: String) = {
    val nowhitespace = input.trim.replace(" ", "-")
    val normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD)
    val slug = """[^\w-]""".r.replaceAllIn(normalized, "")
    """\s{2,}""".r.replaceAllIn(slug.toLowerCase.replace("-", " ").trim, " ").replace(" ", "-")
  }

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString)
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
      """<table class="table table-doc"><tbody>%s</tbody></table>""".format(content)
    }
  }

  object Link {

    def apply(ns: List[String], f: String ⇒ Any, sep: String = ", ") =
      Html { ns map f mkString sep }

    private val search = routes.Doc.search()

    def notion(n: String) =
      """<a class="label-link" href="%s?notion[]=%s"><span class="label label-info">%s</span></a>""".format(search, n, n)

    def methodePedagogique(n: String) =
      """<a class="label-link" href="%s?methodePedagogique[]=%s"><span class="label label-success">%s</span></a>""".format(search, n, n)

    def niveau(n: String) =
      """<a href="%s?niveau[]=%s">%s</a>""".format(search, n, n)

    def interdisciplinarite(n: String) =
      """<a href="%s?interdisciplinarite[]=%s">%s</a>""".format(search, n, n)

    def evaluation(n: String) =
      """<a href="%s?evaluation[]=%s">%s</a>""".format(search, n, n)
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
