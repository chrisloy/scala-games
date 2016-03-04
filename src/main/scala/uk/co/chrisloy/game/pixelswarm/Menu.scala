package uk.co.chrisloy.game.pixelswarm

import java.awt.Color._
import java.awt.Font
import java.awt.event.KeyEvent

import uk.co.chrisloy.game.{GameRunner, Controls, Game}

class Menu(
    xSize: Int,
    ySize: Int,
    runner: GameRunner
  ) extends Game {

  val font = new Font(Font.MONOSPACED, Font.BOLD, 80)

  def update(): Unit = ()

  def render(): Unit = {
    screen.tick()
    screen.background(BLACK)
    screen.write("PIXEL SWARM",  WHITE, font, 50, 100)
    screen.write("1 - PRACTICE", WHITE, font, 50, 300)
    screen.write("2 - EASY",     WHITE, font, 50, 400)
    screen.write("3 - MEDIUM",   WHITE, font, 50, 500)
    screen.write("4 - HARD",     WHITE, font, 50, 600)
  }

  val controls: Controls = new Controls() {

    import KeyEvent._

    override def keyPressed(event:KeyEvent): Unit = {
      event.getKeyCode match {
        case _ =>
      }
    }
    override def keyReleased(event:KeyEvent): Unit = {
      event.getKeyCode match {
        case VK_1 => running.set(false); runner.start(new PixelSwarm(runner, xSize, ySize, 0, Practice))
        case VK_2 => running.set(false); runner.start(new PixelSwarm(runner, xSize, ySize, 120, Easy))
        case VK_3 => running.set(false); runner.start(new PixelSwarm(runner, xSize, ySize, 120, Medium))
        case VK_4 => running.set(false); runner.start(new PixelSwarm(runner, xSize, ySize, 120, Hard))
        case _ =>
      }
    }
  }
}
