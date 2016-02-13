package uk.co.chrisloy.game

import java.awt.image.BufferStrategy
import javax.swing.JFrame
import java.awt._

sealed trait Display {

  val graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment
  val screenDevice = graphicsEnv.getDefaultScreenDevice
  val toolkit = Toolkit.getDefaultToolkit

  def getStrategy: BufferStrategy
  def xSize: Int
  def ySize: Int

  def getPosition: Option[(Int, Int)]

  def addControls(controls: Controls)

  def focus()
}

case class WindowDisplay(name: String, xSize: Int, ySize: Int) extends JFrame(name) with Display {

  val canvas = new Canvas()

  setIgnoreRepaint(true)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setResizable(false)
  setVisible(false)
  dispose()
  screenDevice.setFullScreenWindow(null)
  add(canvas, BorderLayout.CENTER)
  getContentPane.setPreferredSize(new Dimension(xSize, ySize))
  pack()
  setVisible(true)

  def focus() {
    canvas.requestFocus()
  }

  def getStrategy = {
    canvas.createBufferStrategy(2)
    canvas.getBufferStrategy
  }

  def addControls(controls: Controls) = {
    canvas.addKeyListener(controls)
    canvas.addMouseListener(controls)
  }

  def getPosition = {
    // TODO this does not cope with the window being moved
    val positionOnWindow = getMousePosition(true)
    Option(positionOnWindow) map { p =>
      val a = getLocation
      val b = canvas.getLocation
      val (x, y) = (p.getX - a.getX - b.getX, p.getY - a.getY - b.getY)
      (x.toInt, y.toInt)
    }
  }
}

case class FullScreen(xSize: Int, ySize: Int) extends JFrame with Display {

  val initial = screenDevice.getDisplayMode
  val displayMode = new DisplayMode(xSize, ySize, initial.getBitDepth, initial.getRefreshRate)

  setVisible(false)
  dispose()
  setUndecorated(true)
  screenDevice.setFullScreenWindow(this)
  screenDevice.setDisplayMode(displayMode)
  setFocusable(true)
  setVisible(true)

  def getStrategy = {
    createBufferStrategy(2)
    getBufferStrategy
  }

  def addControls(controls: Controls) = {
    addMouseListener(controls)
    addKeyListener(controls)
  }

  def getPosition = {
    val p = MouseInfo.getPointerInfo.getLocation
    Some(p.getX.toInt, p.getY.toInt)
  }

  def focus() {
    requestFocus()
  }
}