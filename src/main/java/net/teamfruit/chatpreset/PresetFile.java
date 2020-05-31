package net.teamfruit.chatpreset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PresetFile {
    public final File location;
    private String line;

    public PresetFile(File location) {
        this.location = location;
        ChatPreset.WATCHER.register(this.location, this::onChange);
        ChatPreset.LOGGER.info("Reload file " + this.location.getAbsolutePath());
    }

    private void onChange() {
        this.line = peek();
    }

    public void openEditor() {
        Util.getOSType().openFile(this.location);
    }

    public String getPeek() {
        return this.line;
    }

    public PresetFile init() {
        try {
            if (!this.location.exists())
                FileUtils.write(this.location, I18n.format("chatpreset.template"), "UTF-8");
        } catch (IOException e) {
            ChatPreset.LOGGER.error("Could not create " + this.location.getAbsolutePath(), e);
        }
        onChange();
        return this;
    }

    public String peek() {
        String line = "";
        if (this.location.exists()) {
            try {
                List<String> lines = FileUtils.readLines(this.location, "UTF-8");
                if (!lines.isEmpty())
                    line = lines.get(0);
            } catch (IOException e) {
                ChatPreset.LOGGER.error("Could not read " + this.location.getAbsolutePath(), e);
            }
        }
        return line;
    }

    public String poll() {
        String line = "";
        if (!this.location.exists()) {
            init();
            openEditor();
        } else {
            try {
                List<String> lines = FileUtils.readLines(this.location, "UTF-8");
                if (!lines.isEmpty()) {
                    line = lines.get(0);
                    lines.remove(0);
                    FileUtils.writeLines(this.location, "UTF-8", lines);
                }
            } catch (IOException e) {
                ChatPreset.LOGGER.error("Could not read " + this.location.getAbsolutePath(), e);
            }
            onChange();
        }
        return line;
    }

    public static PresetFile create() {
        File location = new File(Minecraft.getInstance().gameDir, "chatpreset.txt");
        return new PresetFile(location);
    }
}
