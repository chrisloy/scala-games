package uk.co.chrisloy.game

import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import java.awt.event.MouseListener
import java.awt.event.MouseEvent

trait Controls extends KeyListener with MouseListener {
  
  def keyTyped(event:KeyEvent) = ()
  def mouseClicked(event:MouseEvent) = ()
  def mouseMoved(event:MouseEvent) = ()
  def mouseEntered(event:MouseEvent) = ()
  def mouseExited(event:MouseEvent) = ()
  
  def keyPressed(event:KeyEvent) = ()
  def keyReleased(event:KeyEvent) = ()
  def mousePressed(event:MouseEvent) = ()
  def mouseReleased(event:MouseEvent) = ()
}