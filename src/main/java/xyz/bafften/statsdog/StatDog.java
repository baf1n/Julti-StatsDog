package xyz.bafften.statsdog;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;
import xyz.duncanruns.julti.command.CommandManager;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

public class StatDog implements PluginInitializer {
    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(StatDog.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new StatDog());
    }

    @Override
    public void initialize() {
        // This gets run once when Julti launches
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "StatDog Reloaded!");
        });

        AtomicLong timeTracker = new AtomicLong(System.currentTimeMillis());

        PluginEvents.RunnableEventType.END_TICK.register(() -> {
            // This gets run every tick (1 ms)
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeTracker.get() > 3000) {
                // This gets ran every 3 seconds
                // Julti.log(Level.INFO, "StatDog ran for another 3 seconds.");
                timeTracker.set(currentTime);
            }
        });

        PluginEvents.RunnableEventType.STOP.register(() -> {
            // This gets run when Julti is shutting down
            Julti.log(Level.INFO, "StatDog shutting down...");
        });

        // Command
        CommandManager.getMainManager().registerCommand(new NewSessionCommand());

        Julti.log(Level.INFO, "StatDog Initialized");
    }

    @Override
    public String getMenuButtonName() {
        return "Comming soon...";
    }

    @Override
    public void onMenuButtonPress() {
        JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Comming soon...", "StatDog Window.", JOptionPane.INFORMATION_MESSAGE);
    }
}
