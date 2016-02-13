package uk.co.chrisloy.game

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class GameRunner(game: Game, debug: Boolean, ups: Int) {
  def start() {
    game.go()
    if(debug) {
      Future { new FPS(game).act() }
    }
    Future { new UpdateTicker(game, ups).act() }
    Future { new RenderTicker(game).act() }
  }
}

class FPS(game: Game) extends StopGo {
  def act() {
    while(true) {
      within(1000) {
        game.fps()
      }
    }
  }
}

class UpdateTicker(game: Game, ups: Int) extends StopGo {
  val interval = 1000 / ups
  def act() {
    while(true) {
      within(interval) {
        game.tickUpdate()
      }
    }
  }
}

class RenderTicker(game: Game) extends StopGo {
  def act() {
    while(true) {
      within(20) {
        game.tickRender()
      }
    }
  }
}

trait StopGo {
  def within[B](x: Long)(b: => B): B = {
    Thread.sleep(x)
    b
  }
}

trait Game {
  
  val name:String
  val size:(Int, Int)
  val startFullscreen: Boolean
  
  def controls: Controls
  
  var screen: Screen = null
  var display: Display = null
  
  var verbose = false
  var frames = 0
  var updates = 0
  
  def fps() {
    verbose = true
    println(updates + " UPS    " + frames + " FPS")
    frames = 0
    updates = 0
  }
  
  def go() = {
    display =
      if (startFullscreen) FullScreen(size._1, size._2)
      else WindowDisplay(name, size._1, size._2)
    display.addControls(controls)
    screen = new Screen(display)
    display.focus()
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
