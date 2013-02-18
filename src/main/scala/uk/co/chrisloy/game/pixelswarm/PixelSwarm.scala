package uk.co.chrisloy.game.pixelswarm

import java.awt.Font
import uk.co.chrisloy.game.Game
import java.util.Random
import java.awt.Color
import java.awt.Color._
import java.awt.event.KeyEvent
import uk.co.chrisloy.game.Controls

class PixelSwarm(val xSize:Int, val ySize:Int, val startFullscreen:Boolean) extends Game {
      
  val font = new Font(Font.MONOSPACED, Font.BOLD, 80);
  
  val name = "Pixel Swarm"
  val size = (xSize, ySize)
  
  val startCount = 500
  
  var dead = false
  
  val r = xSize / 95
  val pspeed = xSize / 75
  val espeed = xSize / 450
  
  val p = new PixelSwarmPlayer(r, xSize/4, ySize/2, xSize-r, ySize-r, pspeed, succeed)
  
  val rand = new Random()
  
  var es:List[PixelSwarmEnemy] = Nil
  
  for (i <- 1 to startCount) {
    es = enemy() :: es
  }
  
  def update = {
    p.tick
    es.map(e => e.tick)
    es.map(e => if (e.collides) fail)
  }
  
  def reset = {
    p.x = xSize/4
    p.y = ySize/2
    es.map(e => {
      e.x = e.ix
      e.y = e.iy
    })
  }
  
  def fail() = {
    reset
    dead = true
  }
  
  def enemy() = new PixelSwarmEnemy(xSize, ySize, r, p, espeed)
  
  def succeed():Unit = {
    es = enemy() :: es
    reset
    dead = true
  }
  
  def render() {
    screen.tick
    if (dead) {
      screen.background(DARK_GRAY)
      dead = false
    } else {
      screen.background(BLACK)
    }
    screen.write(es.size.toString(), GRAY, font, 20, 70)
    es.map(e => screen.fill(e.col, Array(e.x, e.x, e.x + r, e.x + r), Array(e.y, e.y + r, e.y + r, e.y)))
    screen.fill(WHITE, Array(p.x, p.x, p.x + r, p.x + r), Array(p.y, p.y + r, p.y + r, p.y))
  }
  
  val controls = new Controls() {
    def keyPressed(event:KeyEvent):Unit = {
      event.getKeyChar() match {
        case 'w' => p.up    = true
        case 'a' => p.left  = true
        case 's' => p.down  = true
        case 'd' => p.right = true
        case  _  => None
      }
    }
    def keyReleased(event:KeyEvent):Unit = {
      event.getKeyChar() match {
        case 'w' => p.up    = false
        case 'a' => p.left  = false
        case 's' => p.down  = false
        case 'd' => p.right = false
        case  _  => None
      }
    }
  }
}
