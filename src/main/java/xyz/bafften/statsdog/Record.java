package xyz.bafften.statsdog;

public class Record {
    public static int nethers;

    /**
     * progress: ランの進捗
     * 
     * 値一覧
     * null: none
     * 0: rsg.enter_nether
     * 1: rsg.enter_bastion
     * 2: rsg.enter_fortress
     * 3: rsg.first_portal
     * 4: rsg.second_portal
     * 5: rsg.enter_stronghold
     * 6: rsg.enter_end
     * 7: rsg.credits
     */
    private Integer progress = null;
    
    private long enterNether;

    Record(long enterNether) {
        nethers++;
        setEnterNether(enterNether);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setEnterNether(long enterNether) {
        this.enterNether = enterNether;
    }

    public long getEnterNether() {
        return enterNether;
    }
}
