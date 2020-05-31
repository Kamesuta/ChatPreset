package net.teamfruit.chatpreset.watcher;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.teamfruit.chatpreset.ChatPreset;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher implements Runnable {
    private final ListMultimap<Path, Runnable> callbacks = MultimapBuilder.hashKeys().arrayListValues().build();
    public final Thread thread;
    private WatchService watcher;

    public FileWatcher() {
        this.thread = new Thread(this, "ChatPresenceFileWatcher");
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            ChatPreset.LOGGER.error("Could not create watcher", e);
        }
    }

    public FileWatcher watch() {
        this.thread.start();
        return this;
    }

    @Override
    public void run() {
        if (this.watcher == null)
            return;
        try {
            while (true) {
                WatchKey watchKey = watcher.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path modified = (Path) event.context();
                    callbacks.get(modified.toAbsolutePath().normalize()).forEach(Runnable::run);
                }
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            ChatPreset.LOGGER.error("Watcher is interrupted", e);
        }
    }

    public void register(Path dir, Path file, Runnable callback) {
        if (this.watcher == null)
            return;
        try {
            if (!callbacks.containsKey(dir))
                dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            callbacks.put(file, callback);
        } catch (IOException e) {
            ChatPreset.LOGGER.error("Could not watch " + file.toAbsolutePath().toString(), e);
        }
    }

    public void register(File file, Runnable callback) {
        register(file.getParentFile().toPath().toAbsolutePath().normalize(), file.toPath().toAbsolutePath().normalize(), callback);
    }
}
