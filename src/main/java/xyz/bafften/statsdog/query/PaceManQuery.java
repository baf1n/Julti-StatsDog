package xyz.bafften.statsdog.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaceManQuery {

    private static String requestData() {
        String apiUrl = "https://paceman.gg/api/ars/liveruns";
        StringBuilder response = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null){
            return null;
        } else {
            return response.toString();
        }
    }

    public static JsonObject getRun(String searchRunner) {
        String paceData = PaceManQuery.requestData();
        if (paceData != null) {
            JsonArray ja = JsonParser.parseString(paceData).getAsJsonArray();
            for (JsonElement runElement : ja) {
                JsonObject run = runElement.getAsJsonObject();
                String runnerNick = run.get("nickname").getAsString();
                if (runnerNick.toLowerCase().equals(searchRunner)) {
                    Julti.log(Level.DEBUG, "Run detected from " + searchRunner);
                    return run;
                }

            }
        }
        return null;
    }

}