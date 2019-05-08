package com.frc107.scouting.analysis.tba;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class TBA {
    private static final String BASE_ADDRESS = "https://www.thebluealliance.com/api/v3/";
    private static final String TBA_KEY = "P2hFBSlkMbnWHZ8kL0z4sWs8Bx59J8dJOpAj5CdG059qK25kPVLjFgQRQ3VHyNah";

    private JsonParser jsonParser;

    public TBA() {
        jsonParser = new JsonParser();
    }

    public OPR getOPRs(String eventKey) throws IOException {
        StringBuilder result = new StringBuilder();
        String address = BASE_ADDRESS + "event/" + eventKey + "/oprs";

        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-TBA-Auth-Key", TBA_KEY);
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        JsonObject jsonObject = jsonParser.parse(result.toString()).getAsJsonObject();
        JsonObject oprs = (JsonObject) jsonObject.get("oprs");
        JsonObject dprs = (JsonObject) jsonObject.get("dprs");

        OPR values = new OPR();

        Object[] oprArray = oprs.entrySet().toArray();
        Object[] dprArray = dprs.entrySet().toArray();
        for (int i = 0; i < oprArray.length; i++) {
            String opr = ((Map.Entry<String, JsonElement>) oprArray[i]).getValue().getAsString();
            String dpr = ((Map.Entry<String, JsonElement>) dprArray[i]).getValue().getAsString();
            int teamNum = Integer.parseInt((((Map.Entry<String, JsonElement>) oprArray[i])).getKey().substring(3));
            values.setOPR(teamNum, opr);
            values.setDPR(teamNum, dpr);
        }

        return values;
    }
}
