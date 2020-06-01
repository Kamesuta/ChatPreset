package net.teamfruit.chatpreset;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyBindingList {
    public static final List<KeyBinding> ModKeyBindings = new ArrayList<>();

    private static final IKeyConflictContext ANYTHING = new IKeyConflictContext() {
        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    };

    public static final KeyBinding keyPreset = register(new KeyBinding(
            "key." + ChatPreset.MODID + ".next",
            ANYTHING,
            KeyModifier.NONE,
            Keyboard.KEY_RETURN,
            "key.categories.multiplayer"
    ));
    public static final KeyBinding keyEdit = register(new KeyBinding(
            "key." + ChatPreset.MODID + ".edit",
            ANYTHING,
            KeyModifier.SHIFT,
            Keyboard.KEY_RETURN,
            "key.categories.multiplayer"
    ));

    public static KeyBinding register(KeyBinding key) {
        ModKeyBindings.add(key);
        return key;
    }

    public static void registerKeys() {
        ModKeyBindings.forEach(ClientRegistry::registerKeyBinding);
    }
}