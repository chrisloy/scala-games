package uk.co.chrisloy.game

import javax.swing.JFrame
import java.awt._
import java.awt.image._

class Window(val xSize:Int, val ySize:Int, val game:Game, val startFullScreen:Boolean) extends JFrame(game.name) {
  
  // Startup code
  setIgnoreRepaint(true)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setResizable(false)
  addKeyListener(game.controls)
  addMouseListener(game.controls)
  
  var canvas:Option[Canvas] = None
  
  val initial = screenDevice.getDisplayMode()
  val displayMode = new DisplayMode(xSize, ySize, initial.getBitDepth(), initial.getRefreshRate())
  
  if (startFullScreen) fullScreen() else windowed()
  if (startFullScreen) hideCursor()
  
  // Some Scala-friendly aliases
  def graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment()
  def screenDevice = graphicsEnv.getDefaultScreenDevice()
  def toolkit = Toolkit.getDefaultToolkit()
  
  override def addNotify {
    super.addNotify
    game.screen.buffer = canvas match {
      case Some(canv) => canv.createBufferStrategy(2) ; canv.getBufferStrategy()
      case None       => this.createBufferStrategy(2) ; this.getBufferStrategy()
    }
  }
  
  def hideCursor() {
    setCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0))
  }
  
  def setCursor(image:Image, point:Point) {
    val cursor = toolkit.createCustomCursor(image, point, "Whatever")
    setCursor(cursor)
  }
  
  def fullScreen() {
    setVisible(false)
    dispose()
    setUndecorated(true)
    screenDevice.setFullScreenWindow(this)
    screenDevice.setDisplayMode(displayMode)
    setVisible(true)
  }
  
  def windowed() {
    setVisible(false)
    dispose()
    screenDevice.setFullScreenWindow(null)
    val c = new Canvas()
    canvas = Some(c)
    c.setIgnoreRepaint(true)
    add(c)
    getContentPane().setPreferredSize(new Dimension(xSize, ySize))
    pack()
    setLocationRelativeTo(null)
    setVisible(true)
  }
}