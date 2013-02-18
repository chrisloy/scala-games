package uk.co.chrisloy.game

import scala.actors.Actor
import java.awt.Color
import java.awt.Color._
import java.awt.event.KeyEvent
import scala.math._
import java.util.Random
import java.awt.Font
import uk.co.chrisloy.game.pixelswarm.PixelSwarm

object Main {
  
  def main(args:Array[String]):Unit = {
    
    val r1080 = (1920, 1080)
    val r720 = (1280, 720)
    val r600 = (800, 600)
    val r480 = (640, 480)
    val r360 = (480, 360)
    
    val (xSize, ySize) = r720
    val fullscreen = false
    
    val game = new PixelSwarm(xSize, ySize, true)
    
    new GameRunner(game, true, 60).start() ! Start
  }
}