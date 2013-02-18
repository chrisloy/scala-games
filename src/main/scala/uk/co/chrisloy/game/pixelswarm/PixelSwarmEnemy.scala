package uk.co.chrisloy.game.pixelswarm

import java.util.Random
import java.awt.Color

object PixelSwarmEnemy {
  val rand = new Random
  def nextColour() = rand.nextInt(6) match {
    case 0 => new Color(255, 0, 0)
    case 1 => new Color(0, 255, 0)
    case 2 => new Color(0, 100, 255)
    case 3 => new Color(255, 255, 0)
    case 4 => new Color(255, 120, 0)
    case 5 => new Color(180, 0, 255)
  }
  def nextStart(xSize:Int, ySize:Int, r:Int) = (rand.nextInt(xSize/2 - r)+xSize/2, rand.nextInt(ySize - r))
}

class PixelSwarmEnemy(val xSize:Int, val ySize:Int, val r:Int, val p:PixelSwarmPlayer, val speed:Int) {
  
  val col = PixelSwarmEnemy.nextColour()
  val (ix, iy) = PixelSwarmEnemy.nextStart(xSize, ySize, r)
   
  val maxDist = math.sqrt(math.pow(xSize, 2) + math.pow(ySize, 2))
 
  val maxX = xSize - r
  val maxY = ySize - r
  
  var x = ix
  var y = iy
  var dx = 0
  var dy = 0

  def tick() {

    val track = math.sqrt(math.pow(x - p.x, 2) + math.pow(y - p.y, 2)) / maxDist

    val (mx, my) = if (PixelSwarmEnemy.rand.nextDouble() > track) {
      val mmx = { if (p.x == x) x else if (p.x > x) x + speed else x - speed }
      val mmy = { if (p.y == y) y else if (p.y > y) y + speed else y - speed }
      (mmx, mmy)
    } else {
      if (PixelSwarmEnemy.rand.nextInt(15) == 4) {
        dx = if (PixelSwarmEnemy.rand.nextBoolean()) speed else -speed
        dy = if (PixelSwarmEnemy.rand.nextBoolean()) speed else -speed
      }
      (x + dx, y + dy)
    }
    
    x = if (mx < 0) 0 else if (mx > maxX) maxX else mx
    y = if (my < 0) 0 else if (my > maxY) maxY else my
  }
  
  def collides() = math.abs(x - p.x) < r && math.abs(y - p.y) < r
}