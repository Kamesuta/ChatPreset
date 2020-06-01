package net.teamfruit.chatpreset;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.teamfruit.chatpreset.watcher.FileWatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ChatPreset.MODID,
        name = ChatPreset.MODNAME,
        version = ChatPreset.VERSION,
        acceptableRemoteVersions = "*"
)
public class ChatPreset {
    public static final String MODNAME = "ChatPreset";
    public static final String MODID = "chatpreset";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final String VERSION = "${version}";
    public static final FileWatcher WATCHER = new FileWatcher();
    public static PresetFile presetFile;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        KeyBindingList.registerKeys();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        WATCHER.watch();
        presetFile = PresetFile.create();
    }
}
