package com.dewdrop.loliga.model

sealed trait Link {
  def address: String
}

object Link {
  private val host = "https://pefl.ru/"

  private class LinkImpl(script: String, params: Seq[(String, String)]) extends Link {
    override def address: String = {
      val paramsStr = params.map(e => s"${e._1}=${e._2}").mkString("?", "&", "")
      s"$host$script$paramsStr"
    }
  }

  private case class PlugLink(params: Seq[(String, String)]) extends LinkImpl(
    script = "plug.php",
    params = ("p" -> "refl") +: params
  )

  def apply(t: String, j: Int, z: String): Link =
    PlugLink(Seq("t" -> t, "j" -> j.toString, "z" -> z))

  def apply(t: String, j: Int, n: Int, z: String): Link =
    PlugLink(Seq("t" -> t, "j" -> j.toString, "n" -> n.toString, "z" -> z))


  def apply(t: String, k: Int, j: Int, f: Int, z: String): Link =
    PlugLink(Seq("t" -> t, "k" -> k.toString, "j" -> j.toString, "f" -> f.toString, "z" -> z))


  def team(j: Int, z: String): Link =
    apply(t = "k", j = j, z = z)

  def tournament(j: Int, f: Int, z: String): Link =
    apply(t = "ss", k = 0, j = j, f = f, z = z)

  def round(j: Int, n: Int, z: String): Link =
    apply(t = "x", j = j, n = n, z = z)
}
