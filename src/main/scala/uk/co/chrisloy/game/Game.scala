package uk.co.chrisloy.game

import java.awt.image.BufferStrategy
import java.awt.Color
import scala.util.Random
import scala.actors._


case object Start
case object Okay
case object PrintFps
case object Render
case object Update
  

class GameRunner(game:Game, debug:Boolean, ups:Int) extends Actor {
  game.start
  if(debug) new FPS(game).start
  new UpdateTicker(game, ups).start
  new RenderTicker(game).start
  def act() {
    react {
      case Start => game ! Start ; act
    }
  }
}

class FPS(game:Game) extends Actor {
  def act() {
    reactWithin(1000) {
      case TIMEOUT => game ! PrintFps; act()
    }
  }
}

class UpdateTicker(game:Game, ups:Int) extends Actor {
  val interval = 1000 / ups
  def act() {
    reactWithin(interval) {
      case Start   => game ! Update; act()
      case TIMEOUT => game ! Update; act()
    }
  }
}

class RenderTicker(game:Game) extends Actor {
  def act() {
    reactWithin(1000) {
      case Okay    => game ! Render; act()
      case Start   => game ! Render; act()
      case TIMEOUT => game ! Render; act()
    }
  }
}

trait Game extends Actor {
  
  val name:String
  val size:(Int, Int)
  val startFullscreen:Boolean
  
  def controls():Controls
  
  var screen:Screen = null
  var window:Window = null
  
  var verbose:Boolean = false
  var frames:Int = 0;
  var updates:Int = 0;
  
  def act() {
    react {
      case Start    => go()         ; act
      case Update   => tickUpdate() ; act
      case Render   => tickRender() ; sender ! Okay ; act
      case PrintFps => fps()        ; act
    }
  }
  
  def fps() {
    verbose = true
    println(updates + " UPS    " + frames + " FPS")
    frames = 0
    updates = 0
  }
  
  def go() = {
    screen = new Screen(size._1, size._2)
    window = new Window(size._1, size._2, this, startFullscreen)
  }
  
  private def tickUpdate() = {
    if(verbose){
      updates = updates + 1
    }
    update
  }
  
  private def tickRender() = {
    if(verbose){
      frames = frames + 1
    }
    render; draw
  }
  
  def update()
  
  def render()
  
  def draw() = screen.draw
}
