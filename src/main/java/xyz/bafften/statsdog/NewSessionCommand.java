package xyz.bafften.statsdog;

import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;
import xyz.duncanruns.julti.util.ResetCounter;
import org.apache.logging.log4j.Level;

public class NewSessionCommand extends Command{
    @Override
    public String helpDescription() {
        return "newsession - Start a new session. (=Sets the current number of resets, enters, etc. in the current session)";
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public int getMaxArgs() {
        return 0;
    }

    @Override
    public String getName() {
        return "newsession";
    }

    @Override
    public void run(String[] args, CancelRequester cancelRequester) {
        ResetCounter.sessionCounter = 0;
        ResetCounter.updateFiles();
        Julti.log(Level.INFO, "RunTracker: New session have started.");
    }
}