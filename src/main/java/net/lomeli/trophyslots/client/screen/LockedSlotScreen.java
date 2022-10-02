package net.lomeli.trophyslots.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.client.handler.SpriteHandler;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class LockedSlotScreen extends Button {
    private static final int GREY_COLOR = 2130706433;
    private final ContainerScreen<?> parentScreen;

    @SuppressWarnings("all")
    public LockedSlotScreen(ContainerScreen<?> parentScreen) {
        super(0, 0, 0, 0, new StringTextComponent(""), null);
        this.active = false;
        this.parentScreen = parentScreen;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float renderTick) {
        if (!this.visible) return;
        Minecraft mc = Minecraft.getInstance();

        for (Slot slot : parentScreen.getMenu().slots) {
            if (slot.container instanceof PlayerInventory) {
                PlayerEntity player = ((PlayerInventory) slot.container).player;
                if (!player.abilities.instabuild) {
                    IPlayerSlots playerSlots = PlayerSlotHelper.getPlayerSlots(player);
                    if (playerSlots != null && !playerSlots.slotUnlocked(slot.getSlotIndex()))
                        drawLockedSlot(mc, slot.x, slot.y);
                }
            }
        }
    }

    private void drawLockedSlot(Minecraft mc, int xPos, int yPos) {
        if (ClientConfig.renderType == SlotRenderType.NONE)
            return;
        int left = parentScreen.getGuiLeft();
        int top = parentScreen.getGuiTop();
        int x = left + xPos;
        int y = top + yPos;

        MatrixStack matrix = new MatrixStack();
        matrix.pushPose();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (ClientConfig.renderType.isGreyOut()) {
            RenderSystem.enableLighting();

            this.fillGradient(matrix, x, y, x + 16, y + 16, GREY_COLOR, GREY_COLOR);
            RenderSystem.disableLighting();
        }
        if (ClientConfig.renderType.drawCross()) {
            TextureAtlasSprite crossSprite = mc.getTextureAtlas(PlayerContainer.BLOCK_ATLAS)
                    .apply(SpriteHandler.CROSS_SPRITE);
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            mc.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
            AbstractGui.blit(matrix, x, y, this.getBlitOffset(), 16, 16, crossSprite);
        }

        GlStateManager._disableBlend();
        matrix.popPose();
    }

    @Override
    public void onPress() {
    }
}
