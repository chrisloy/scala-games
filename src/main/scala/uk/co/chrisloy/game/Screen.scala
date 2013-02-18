package uk.co.chrisloy.game

import java.awt.image.BufferStrategy
import java.awt.Graphics
import java.awt.Color
import java.awt.Font

class Screen(val xSize:Int, val ySize:Int) {
  
  var buffer:BufferStrategy = null
  var graphics:Graphics = null
  
  def tick() = { graphics = buffer.getDrawGraphics() }
  
  def draw() {
    if(buffer.contentsLost()) {
      println("Buffer lost, oops");
    }
    else {
      buffer.show()
      if(graphics != null) {
        graphics.dispose()
        graphics = null
      }
    }
  }

  def background (colour: Color) {
    graphics.setColor(colour)
    graphics.fillRect(0, 0, xSize, ySize)
  }

  def write (text: String, c: Color, font: Font, x: Int, y: Int) {
    graphics.setColor(c)
    graphics.setFont(font)
    graphics.drawString(text, x, y)
  }

  def draw (colour: Color, xs: Array[Int], ys: Array[Int]) {
    graphics.setColor(colour);
    graphics.drawPolygon(xs, ys, xs.length);
  }

  def fill (colour: Color, xs: Array[Int], ys: Array[Int]) {
    graphics.setColor(colour);
    graphics.fillPolygon(xs, ys, xs.length);
  }
}