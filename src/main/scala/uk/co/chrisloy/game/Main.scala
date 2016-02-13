package uk.co.chrisloy.game

import uk.co.chrisloy.game.pixelswarm.PixelSwarm

object Main extends App {
  
  val p1800 = (2880, 1800)
  val p900 = (1440, 900)

  val r1080 = (1920, 1080)
  val r720 = (1280, 720)
  val r600 = (800, 600)
  val r480 = (640, 480)
  val r360 = (480, 360)

  val (xSize, ySize) = r720

  val fullscreen = false

  val game = new PixelSwarm(xSize, ySize, fullscreen)

  new GameRunner(game, true, 60).start()
}