package xyz.bafften.statsdog;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.cylorun.pace.rpc.PaceMan;
import xyz.bafften.statsdog.utils.FileUtils;

public class RecordsManager {

    /** セッションのスタート時刻 */
    private static long start;

    /** セッションのラン */
    private static Map<String, Record> records = new HashMap<>();

    /** 新規セッションの準備 */
    public static void newSession() {
        RecordsManager.start = System.currentTimeMillis();
        RecordsManager.records.clear();
        Record.nethers = 0;
        FileUtils.updateFiles(0, 0, "0.0");
    }

    /**
     * PaceManに問い合わせる
     */
    public static void query() {
        StatsDogOptions options = StatsDogOptions.getInstance();
        JsonObject run = PaceMan.getRun(options.username.toLowerCase());
        if (run != null) {
            String worldId = run.get("worldId").getAsString();
            JsonArray eventList = run.getAsJsonArray("eventList");

            // StatsDog
            if (!records.containsKey(worldId)) {
                JsonObject event = eventList.get(0).getAsJsonObject();
                Long currentTime = Long.parseLong(event.get("igt").getAsString());

                RecordsManager.createNewRecord(worldId, currentTime);
                RecordsManager.updateENStats();
            }
        }
    }

    /**
     * recordsに新しいランを登録
     * 
     * @param worldId PaceMan上で付けられる(?)ランのID
     * @param enterNether EnterNetherのタイム
     */
    private static void createNewRecord(String worldId, long enterNether) {
        records.put(worldId, new Record(RecordsManager.roundTime(enterNether)));
        records.get(worldId).setProgress(0);
    }

    /**
     * RecordにEnterBastion以降のタイムを登録
     * 
     * @param worldId PaceMan上で付けられる(?)ランのID
     * @param eventId イベントID(Recordのprogressに対応)
     * @param currentTime そのイベント時のタイム
     */
    /*
    private static void updateRecord(String worldId, int eventId, long currentTime) {
    }
    */

    /** Enters, Avg, NPHの計算 -> FileUtilsに渡す */
    private static void updateENStats() {
        int nethers = Record.nethers;
        long sum = 0;
        for (Record r : records.values()) {
            sum += r.getEnterNether();
        }

        long current = System.currentTimeMillis();
        double hours = (double) (current - start) / (1000 * 60 * 60);
        double nph = nethers / hours ;

        FileUtils.updateFiles(nethers, sum/nethers, RecordsManager.formatValue(nph));
    }

    /** ms -> s
     * 
     * @param ms 時間(ms単位)
     * @return 時間(s単位)
     */
    public static long roundTime(long ms) {
        return ms / 1000;
    }

    /** double型の数値を整形
     * 
     * @param d 整形したい数値
     * @return xx.x形式の数値(String型)
     */
    public static String formatValue(double d) {
        return String.format("%.1f", d);
    }

}
