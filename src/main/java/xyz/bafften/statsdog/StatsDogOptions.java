package xyz.bafften.statsdog;

import java.nio.file.Path;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.duncanruns.julti.JultiOptions;


public class StatsDogOptions {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path SAVE_PATH = JultiOptions.getJultiDir().resolve("statsdogoptions.json");

    private static StatsDogOptions instance;

    public String username = "bafften";
    public boolean enabled = true;

    public static StatsDogOptions getInstance() {
        return instance;
    }

    public static StatsDogOptions load() throws IOException {
        if (Files.exists(SAVE_PATH)) {
            instance = GSON.fromJson(new String(Files.readAllBytes(SAVE_PATH)), StatsDogOptions.class);
        } else {
            instance = new StatsDogOptions();
        }
        return instance;
    }

    public static void save() throws IOException {
        FileWriter writer = new FileWriter(SAVE_PATH.toFile());
        GSON.toJson(instance, writer);
        writer.close();
    }

}
