package com.frc107.scouting.analysis;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.frc107.scouting.Scouting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadDataTask extends AsyncTask<Void, Void, SparseArray<TeamDetails>> {
    private static final int COL_MATCH_NUM = 0,
                             COL_TEAM_NUM = 1,
                             COL_STARTING_ITEM = 3,
                             COL_STARTING_PLACED_LOCATION = 4,
                             COL_ITEM_PICKED_UP = 8,
                             COL_ITEM_PLACED_LOCATION = 9,
                             COL_DEFENSE = 13,
                             COL_HAB = 11,
                             COL_OPR = 17,
                             COL_DPR = 18;

    private IAnalysisListener listener;
    private SparseArray<TeamDetails> teamDetailsSparseArray;

    public LoadDataTask(IAnalysisListener listener) {
        this.listener = listener;
        teamDetailsSparseArray = new SparseArray<>();
    }

    @Override
    protected SparseArray<TeamDetails> doInBackground(Void... voids) {
        loadData();
        return teamDetailsSparseArray;
    }

    @Override
    protected void onPostExecute(SparseArray<TeamDetails> map) {
        super.onPostExecute(map);
        listener.onDataLoaded(map, map == null);
    }

    public void loadData() {
        File file = Scouting.FILE_UTILS.getConcatMatchFile();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()))) {
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine(); // Skip first line, which is the header
            while (line != null) {
                String[] columns = line.split(",");
                handleColumns(columns);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.d("Scouting", e.getMessage());
            return;
        }

        for(int i = 0; i < teamDetailsSparseArray.size(); i ++) {
            TeamDetails details = teamDetailsSparseArray.valueAt(i);
            details.calculateAverages();
        }
    }

    private void handleColumns(String[] columns) {
        int matchNum = Integer.parseInt(columns[COL_MATCH_NUM]);
        int teamNum = Integer.parseInt(columns[COL_TEAM_NUM]);
        int startingItem = Integer.parseInt(columns[COL_STARTING_ITEM]);
        int startingPlacedLocation = Integer.parseInt(columns[COL_STARTING_PLACED_LOCATION]);
        int itemPickedUp = Integer.parseInt(columns[COL_ITEM_PICKED_UP]);
        int cyclePlacedLocation = Integer.parseInt(columns[COL_ITEM_PLACED_LOCATION]);
        int defense = Integer.parseInt(columns[COL_DEFENSE]);
        int habLevel = Integer.parseInt(columns[COL_HAB]);
        double opr = Double.parseDouble(columns[COL_OPR]);
        double dpr = Double.parseDouble(columns[COL_DPR]);

        TeamDetails teamDetails = teamDetailsSparseArray.get(teamNum);
        if (teamDetails == null) {
            teamDetailsSparseArray.put(teamNum, new TeamDetails());
            teamDetails = teamDetailsSparseArray.get(teamNum);
        }

        teamDetails.setOPR(opr);
        teamDetails.setDPR(dpr);
        teamDetails.incrementCycleNum();

        if (!teamDetails.hasMatch(matchNum)) {
            // Sandstorm game pieces
            if (startingPlacedLocation != Scouting.SANDSTORM_FLOOR && startingPlacedLocation != Scouting.SANDSTORM_NOTHING_PLACED) {
                switch (startingItem) {
                    case Scouting.SANDSTORM_CARGO:
                        teamDetails.incrementCargoNum();
                        break;
                    case Scouting.SANDSTORM_PANEL:
                        teamDetails.incrementHatchNum();
                        break;
                }
            }

            // Match defense
            switch (defense) {
                case Scouting.ENDGAME_DEFENSE_EFFECTIVE:
                    teamDetails.incrementDefenseNum();
                    teamDetails.incrementEffectiveDefenseNum();
                    break;
                case Scouting.ENDGAME_DEFENSE_INEFFECTIVE:
                    teamDetails.incrementDefenseNum();
                    break;
            }
            teamDetails.addMatch(matchNum);
        }

        // Cycle game pieces
        if (cyclePlacedLocation != Scouting.CYCLE_FLOOR && cyclePlacedLocation != Scouting.CYCLE_NOTHING_PLACED) {
            switch (itemPickedUp) {
                case Scouting.CYCLE_CARGO:
                    teamDetails.incrementCargoNum();
                    break;
                case Scouting.CYCLE_PANEL:
                    teamDetails.incrementHatchNum();
                    break;
            }
        }

        // Habitat levels
        switch (habLevel) {
            case Scouting.ENDGAME_HAB_ONE:
                teamDetails.incrementHabOneAmount();
                break;
            case Scouting.ENDGAME_HAB_TWO:
                teamDetails.incrementHabTwoAmount();
                break;
            case Scouting.ENDGAME_HAB_THREE:
                teamDetails.incrementHabThreeAmount();
                break;
        }

        // Rocket levels
        switch (startingPlacedLocation) {
            case Scouting.SANDSTORM_BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case Scouting.SANDSTORM_MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case Scouting.SANDSTORM_TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case Scouting.SANDSTORM_CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
        }
        switch (cyclePlacedLocation) {
            case Scouting.CYCLE_BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case Scouting.CYCLE_MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case Scouting.CYCLE_TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case Scouting.CYCLE_CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
        }
    }
}
