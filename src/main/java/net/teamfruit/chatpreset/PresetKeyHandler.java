package net.teamfruit.chatpreset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PresetKeyHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.currentScreen == null) {
            if (KeyBindingList.keyEdit.isPressed()) {
                ChatPreset.presetFile.openEditor();
                ChatScreen chatScreen = new ChatScreen("");
                mc.displayGuiScreen(chatScreen);
            } else if (KeyBindingList.keyPreset.isPressed()) {
                ChatScreen chatScreen = new ChatScreen(ChatPreset.presetFile.poll());
                mc.displayGuiScreen(chatScreen);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post event) {
        if (!(mc.currentScreen instanceof ChatScreen)) {
            mc.fontRenderer.drawStringWithShadow(
                    ChatPreset.presetFile.getPeek(),
                    4,
                    event.getWindow().getScaledHeight() - mc.fontRenderer.FONT_HEIGHT - 3,
                    0xffffff);
        }
    }
}