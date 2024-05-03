package xyz.bafften.statsdog;

import com.google.common.io.Resources;

import net.arikia.dev.drpc.DiscordRPC;

import org.apache.logging.log4j.Level;

import xyz.bafften.statsdog.utils.FileUtils;
import xyz.bafften.statsdog.utils.NewSessionCommand;

import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;
import xyz.duncanruns.julti.util.ExceptionUtil;
import xyz.duncanruns.julti.command.CommandManager;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatsDogInit implements PluginInitializer {
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(StatsDogInit.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new StatsDogInit());
    }

    // log method
    public static void debugLog(String s) {
        Julti.log(Level.DEBUG, "(StatsDog) " + s);
    }

    public static void infoLog(String s) {
        Julti.log(Level.INFO, "(StatsDog) " + s);
    }

    public static void errorLog(String s) {
        Julti.log(Level.ERROR, "(StatsDog) " + s);
    }

    @Override
    public void initialize() {

        // newsessionコマンドを登録
        CommandManager.getMainManager().registerCommand(new NewSessionCommand());
        RecordsManager.newSession();

        StatsDogOptions options;
        try {
            options = StatsDogOptions.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileUtils.prepareDirAndFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        StatsDogInit.infoLog("Plugin Initialized");

        // This gets run once when Julti launches
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "StatsDog Reloaded!");
        });

        AtomicInteger errorCounter = new AtomicInteger();
        EXECUTOR.scheduleWithFixedDelay(() -> {
            try {
                if (options.enabled) {
                    RecordsManager.query();
                } else {
                    DiscordRPC.discordClearPresence();
                }
                errorCounter.set(0);
            } catch (Throwable t) {
                if (errorCounter.incrementAndGet() > 10) {
                    Julti.log(Level.ERROR, "Error: " + ExceptionUtil.toDetailedString(t));
                }
            }
        }, 1, 20, TimeUnit.SECONDS);

        PluginEvents.RunnableEventType.STOP.register(() -> {
            // This gets run when Julti is shutting down
            Julti.log(Level.INFO, "StatsDog shutting down...");
        });
    }

    @Override
    public String getMenuButtonName() {
        return "Comming soon...";
    }

    @Override
    public void onMenuButtonPress() {
        JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Comming soon...", "StatsDog Window", JOptionPane.INFORMATION_MESSAGE);
    }
}
