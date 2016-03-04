package uk.co.chrisloy.game

import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GameRunner(ups: Int, name: String, fullScreen: Boolean, x: Int, y: Int) {

  var blocker = Future.sequence(Seq(Future.successful(())))

  val display =
    if (fullScreen) FullScreen(x, y)
    else WindowDisplay(name, x, y)
  display.focus()

  def start(game: Game) {
    Await.ready(blocker, 5.seconds)
    game.go(display)
    blocker = Future.sequence {
      Seq(
        Future { new FPS(game).act() },
        Future { new UpdateTicker(game, ups).act() },
        Future { new RenderTicker(game).act() },
        Future { new Killer(game, display).act() }
      )
    }
  }
}

class Killer(game: Game, display: Display) {
  def act(): Unit = {
    while(game.running.get()) {
      Thread.sleep(50)
    }
    display.removeControls(game.controls)
  }
}

class FPS(game: Game) extends StopGo {
  def act() {
    while(game.running.get) {
      within(1000) {
        game.fps()
      }
    }
    println("DEATH-1")
  }
}

class UpdateTicker(game: Game, ups: Int) extends StopGo {
  val interval = 1000 / ups
  def act() {
    while(game.running.get) {
      within(interval) {
        game.tickUpdate()
      }
    }
    println("DEATH-2")
  }
}

class RenderTicker(game: Game) extends StopGo {
  def act() {
    while(game.running.get) {
      within(20) {
        game.tickRender()
      }
    }
    println("DEATH-3")
  }
}

trait StopGo {
  def within[B](x: Long)(b: => B): B = {
    Thread.sleep(x)
    b
  }
}

trait Game {
  
  def controls: Controls

  val running = new AtomicBoolean(true)
  
  var screen: Screen = null

  var verbose = false
  var frames = 0
  var updates = 0
  
  def fps() {
    verbose = true
    println(updates + " UPS    " + frames + " FPS")
    frames = 0
    updates = 0
  }
  
  def go(display: Display) = {
    display.addControls(controls)
    screen = new Screen(display)
  }
  
  def tickUpdate() = {
    if(verbose){
      updates = updates + 1
    }
    update()
  }
  
  def tickRender() = {
    if(verbose){
      frames = frames + 1
    }
    render()
    draw()
  }
  
  def update()
  
  def render()
  
  def draw() = screen.draw()
}
