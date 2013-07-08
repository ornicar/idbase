package idbase

object Helper {

  def selectable(seq: Seq[_]): Seq[(String, String)] = {
    val x = seq.map(_.toString.capitalize) 
    x zip x
  }
}
