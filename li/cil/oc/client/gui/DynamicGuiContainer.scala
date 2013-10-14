package li.cil.oc.client.gui

import li.cil.oc.Config
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.inventory.{Container, Slot}
import net.minecraft.util.{StatCollector, ResourceLocation}
import org.lwjgl.opengl.GL11

abstract class DynamicGuiContainer(container: Container) extends GuiContainer(container) {
  protected val slotBackground = new ResourceLocation(Config.resourceDomain, "textures/gui/slot.png")
  protected val background = new ResourceLocation(Config.resourceDomain, "textures/gui/background.png")

  protected var (x, y) = (0, 0)

  override def initGui() = {
    super.initGui()
    x = (width - xSize) / 2
    y = (height - ySize) / 2
  }

  override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) = {
    fontRenderer.drawString(
      StatCollector.translateToLocal("container.inventory"),
      8, ySize - 96 + 2, 0x404040)
  }

  override def drawGuiContainerBackgroundLayer(dt: Float, mouseX: Int, mouseY: Int) = {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(background)
    drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
  }

  override def drawSlotInventory(slot: Slot) = {
    if (slot.slotNumber < container.inventorySlots.size() - 36)
      drawSlotBackground(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    super.drawSlotInventory(slot)
    GL11.glDisable(GL11.GL_BLEND)
  }

  private def drawSlotBackground(x: Int, y: Int) {
    GL11.glColor4f(1, 1, 1, 1)
    mc.renderEngine.bindTexture(slotBackground)
    val t = Tessellator.instance
    t.startDrawingQuads()
    t.addVertexWithUV(x, y + 18, zLevel, 0, 1)
    t.addVertexWithUV(x + 18, y + 18, zLevel, 1, 1)
    t.addVertexWithUV(x + 18, y, zLevel, 1, 0)
    t.addVertexWithUV(x, y, zLevel, 0, 0)
    t.draw()
  }
}
