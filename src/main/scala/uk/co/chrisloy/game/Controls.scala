package uk.co.chrisloy.game

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import java.awt.event.MouseListener
import java.awt.event.MouseEvent

trait Controls extends KeyListener with MouseListener {
  
  // Ignore these
  def keyTyped(event:KeyEvent):Unit = None
  def mouseClicked(event:MouseEvent):Unit = None
  def mouseMoved(event:MouseEvent):Unit = None
  def mouseEntered(event:MouseEvent):Unit = None
  def mouseExited(event:MouseEvent):Unit = None
  
  // TODO
  def keyPressed(event:KeyEvent):Unit
  def keyReleased(event:KeyEvent):Unit
  def mousePressed(event:MouseEvent):Unit = None
  def mouseReleased(event:MouseEvent):Unit = None
}