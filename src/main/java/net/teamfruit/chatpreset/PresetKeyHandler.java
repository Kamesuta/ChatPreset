package net.teamfruit.chatpreset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class PresetKeyHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.currentScreen == null) {
            if (ChatPreset.presetFile.getPeek() == null) {
                ChatPreset.presetFile.onChange();
            } else if (KeyBindingList.keyEdit.isPressed()) {
                ChatPreset.presetFile.openEditor();
                GuiChat chatScreen = new GuiChat("");
                mc.displayGuiScreen(chatScreen);
            } else if (KeyBindingList.keyPreset.isPressed()) {
                GuiChat chatScreen = new GuiChat(ChatPreset.presetFile.poll());
                mc.displayGuiScreen(chatScreen);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Text event) {
        if (!(mc.currentScreen instanceof GuiChat) && mc.world != null) {
            mc.fontRenderer.drawStringWithShadow(
                    ChatPreset.presetFile.getPeek(),
                    4,
                    event.getResolution().getScaledHeight() - mc.fontRenderer.FONT_HEIGHT - 3,
                    0xffffff);
        }
    }
}