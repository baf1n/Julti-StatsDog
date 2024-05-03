package xyz.bafften.statsdog.utils;

import xyz.bafften.statsdog.RecordsManager;
import xyz.bafften.statsdog.StatsDogInit;

import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;
import xyz.duncanruns.julti.util.ResetCounter;

/** 新しいセッションにする(Entersなどをリセットする)コマンド */
public class NewSessionCommand extends Command{
    @Override
    public String helpDescription() {
        return "newsession - Start a new session.";
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
        RecordsManager.newSession();
        StatsDogInit.infoLog("Start a new session.");
    }
}