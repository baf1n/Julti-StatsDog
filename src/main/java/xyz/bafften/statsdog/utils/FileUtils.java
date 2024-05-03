package xyz.bafften.statsdog.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import xyz.bafften.statsdog.StatsDogInit;
import xyz.duncanruns.julti.JultiOptions;

public class FileUtils {
    private static final Path SAVE_PATH = JultiOptions.getJultiDir().resolve("statsdog");
    private static final Path nethersTxt = SAVE_PATH.resolve("nethers.txt");
    private static final Path avgTxt = SAVE_PATH.resolve("avg.txt");
    private static final Path nphTxt = SAVE_PATH.resolve("nph.txt");

    public static void updateFiles(int nethers, long avg, String nph) {
        StatsDogInit.infoLog("Enters:" + nethers + " Avg:" + formatTime(avg) + " NPH:" + nph);

        try {
            FileWriter nethersFw = new FileWriter(nethersTxt.toString());
            PrintWriter nethersPw =  new PrintWriter(new BufferedWriter(nethersFw));
            nethersPw.print(nethers);
            nethersPw.close();

            FileWriter avgFw = new FileWriter(nethersTxt.toString());
            PrintWriter avgPw =  new PrintWriter(new BufferedWriter(avgFw));
            avgPw.print(nethers);
            avgPw.close();

            FileWriter nphFw = new FileWriter(nethersTxt.toString());
            PrintWriter nphPw =  new PrintWriter(new BufferedWriter(nphFw));
            nphPw.print(nethers);
            nphPw.close();
        } catch (IOException e) {
            StatsDogInit.debugLog(e.toString());
        }
    }

    public static void prepareDirAndFiles() throws IOException {
        File saveDir = new File(SAVE_PATH.toString());
        if (!saveDir.exists()) {
            saveDir.mkdir();
            Files.createFile(nethersTxt);
            Files.createFile(avgTxt);
            Files.createFile(nphTxt);
        }
    }

    public static String formatTime(long s) {
        long minutes = s / 60;
        long remainingSeconds = s % 60;

        return String.format("%d:%02d", minutes, remainingSeconds);
    }

}
