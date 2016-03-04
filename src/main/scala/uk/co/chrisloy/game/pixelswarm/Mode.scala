package uk.co.chrisloy.game.pixelswarm

sealed trait Mode {
  def startCount: Int
  def text: String
  def highScores: Seq[String]
  def timed: Boolean
  def instructions: Seq[String] = Nil
}

object Practice extends Mode {
  val startCount = 0
  val text = "PRACTICE MODE"
  val highScores = Nil
  val timed = false
  override def instructions = Seq(
    "Move with the arrow keys or WASD.",
    "Get your pixel to the right side to score points.",
    "Avoid the coloured pixels. If they touch you then you will restart.",
    "Coloured pixels will track you more accurately the closer you are.",
    "Each time you score a point, another pixel appears to stop you.",
    "You are against the clock so go as fast as you can!",
    "Good luck!"
  )
}

object Easy extends Mode {
  val startCount = 0
  val text = "EASY"
  val highScores = Seq("108 CJL", "106 JSG", "99  RBB", "92  RBB", "88  YYP")
  val timed = true
}

object Medium extends Mode {
  val startCount = 20
  val text = "Medium"
  val highScores = Seq("62  RBB", "61  JSG", "57  RBB", "42  YYP", "49  MGH")
  val timed = true
}

object Hard extends Mode {
  val startCount = 300
  val text = "Hard"
  val highScores = Seq("41  RBB", "34  JSG", "28  CJL", "27  CJL", "25  YYP")
  val timed = true
}