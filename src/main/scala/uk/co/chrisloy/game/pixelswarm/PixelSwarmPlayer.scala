package uk.co.chrisloy.game.pixelswarm

class PixelSwarmPlayer(val r:Int, val initX:Int, val initY:Int, val maxX:Int, val maxY:Int, val speed:Int, val succeed:() => Unit) {
    
  var up    = false
  var left  = false
  var down  = false
  var right = false
  
  var x = initX
  var y = initY
  
  def tick() {
    if(x == maxX) {
      succeed()
    } else {
      move()
    }
  }
  
  private def move() {
    if (up && !down) y = {
      math.max(y - speed, 0)
    }
    else if (!up && down) y = {
      math.min(y + speed, maxY)
    }
    if (left && !right) x = {
      math.max(x - speed, 0)
    }
    else if (!left && right) x = {
      math.min(x + speed, maxX)
    }    
  }
}