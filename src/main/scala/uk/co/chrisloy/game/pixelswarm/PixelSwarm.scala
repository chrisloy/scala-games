package uk.co.chrisloy.game.pixelswarm

import java.awt.{Color, Font}
import uk.co.chrisloy.game.{GameRunner, Game, Controls}
import java.util.Random
import java.awt.Color._
import java.awt.event.KeyEvent

class PixelSwarm(
    runner: GameRunner,
    xSize: Int,
    ySize: Int,
    timeLimit: Int,
    mode: Mode) extends Game {

  val warning = new Color(127, 0, 0)
  val font = new Font(Font.MONOSPACED, Font.BOLD, 80)
  val smallFont = new Font(Font.MONOSPACED, Font.BOLD, 25)
  val bigFont = new Font(Font.MONOSPACED, Font.BOLD, 200)

  val startCount = mode.startCount

  var started = false
  var dead = false
  var gameOver = false
  var endTime = 0L
  var startTime = 0L

  def remaining = math.max(endTime - System.currentTimeMillis, 0)

  val r = xSize / 95
  val pspeed = xSize / 75
  def espeed = xSize / 450// * (util.Random.nextInt(3) + 1)
  
  val p = new PixelSwarmPlayer(r, xSize/4, ySize/2, xSize-r, ySize-r, pspeed, succeed)
  
  val rand = new Random()
  
  var es: List[PixelSwarmEnemy] = Nil
  
  for (i <- 1 to startCount) {
    es = enemy() :: es
  }
  
  def update() = {
    val now = System.currentTimeMillis
    if (!started) {
      endTime = now + (timeLimit * 1000) + 200
    }
    gameOver = mode.timed && endTime < now
    if (started && !gameOver) {
      p.tick()
      es.foreach(e => e.tick())
      es.foreach(e => if (e.collides) fail())
    }
  }
  
  def reset() = {
    p.x = xSize/4
    p.y = ySize/2
    es.foreach { e =>
      e.x = e.ix
      e.y = e.iy
    }
  }
  
  def fail() = {
    reset()
    dead = true
  }
  
  def enemy() = new PixelSwarmEnemy(xSize, ySize, r, p, espeed)
  
  def succeed():Unit = {
    es = enemy() :: es
    reset()
    dead = true
  }
  
  def render() {

    screen.tick()

    // Background
    if (dead) {
      screen.background(DARK_GRAY)
      dead = false
    } else {
      screen.background(
        if (gameOver || mode.timed && remaining < 10000 && remaining % 1000 > 900) warning else BLACK
      )
    }

    // Score
    screen.write((es.size-startCount).toString, GRAY, font, 20, 70)

    // Timer
    if (mode.timed) {
      val rem = remaining / 1000
      val minutes = rem / 60
      val seconds = rem % 60
      val time = "%d:%02d".format(minutes, seconds)
      screen.write(time, RED, font, 20, 680)
    }

    // High scores
    if (mode.timed) {
      screen.write("==TOP==", WHITE, smallFont, 1150, 550)
      0 to 4 foreach { x =>
        screen.write(mode.highScores(x), WHITE, smallFont, 1150, 550 + ((1 + x) * 30))
      }
    }

    // Pixels
    es.foreach(e => screen.fill(e.col, Array(e.x, e.x, e.x + r, e.x + r), Array(e.y, e.y + r, e.y + r, e.y)))
    screen.fill(WHITE, Array(p.x, p.x, p.x + r, p.x + r), Array(p.y, p.y + r, p.y + r, p.y))

    // Overlay
    if (!started) {
      screen.write("PRESS ANY KEY TO START", BLUE, font, 100, 400)
    } else if (gameOver) {
      screen.write("GAME OVER", BLACK, bigFont, 100, 450)
    }

    // Instructions
    if (started && mode.instructions.nonEmpty) {
      val n = mode.instructions.size
      val duration = 4000
      val index = (((System.currentTimeMillis - startTime) % (n * duration)) / duration).toInt
      screen.write(mode.instructions(index), WHITE, smallFont, 50, 680)
    }
  }
  
  val controls = new Controls() {

    import KeyEvent._

    override def keyPressed(event:KeyEvent): Unit = {
      if(!started) {
        startTime = System.currentTimeMillis
        started = true
      }
      event.getKeyCode match {
        case VK_W | VK_UP    => p.up    = true
        case VK_A | VK_LEFT  => p.left  = true
        case VK_S | VK_DOWN  => p.down  = true
        case VK_D | VK_RIGHT => p.right = true
        case _ =>
      }
    }
    override def keyReleased(event:KeyEvent): Unit = {
      event.getKeyCode match {
        case VK_W | VK_UP    => p.up    = false
        case VK_A | VK_LEFT  => p.left  = false
        case VK_S | VK_DOWN  => p.down  = false
        case VK_D | VK_RIGHT => p.right = false
        case VK_ESCAPE       => running.set(false); runner.start(new Menu(xSize, ySize, runner))
        case _ =>
      }
    }
  }
}
