package com.frc107.scouting.deepspace.match.endgame;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.ui.questionwrappers.RadioWrapper;
import com.frc107.scouting.deepspace.DeepSpaceStrings;
import com.frc107.scouting.deepspace.match.cycle.CyclesData;
import com.frc107.scouting.deepspace.match.sandstorm.SandstormData;

public class EndgameActivity extends BaseActivity {
    private RadioWrapper habitatWrapper;
    private CheckBox defenseAllMatchCheckBox;
    private RadioWrapper defenseWrapper;
    private CheckBox foulsCheckBox;

    private EndgameModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        model = ViewModelProviders.of(this).get(EndgameModel.class);

        SandstormData sandstormData = getIntent().getParcelableExtra(DeepSpaceStrings.SANDSTORM_DATA_EXTRA_KEY);
        CyclesData cyclesData = getIntent().getParcelableExtra(DeepSpaceStrings.CYCLE_DATA_EXTRA_KEY);

        getSupportActionBar().setTitle("Team: " + sandstormData.getTeamNumber());

        model.setSandstormAndCyclesData(sandstormData, cyclesData);

        habitatWrapper = new RadioWrapper(this, EndgameIDs.HAB_LEVEL_QUESTION, model::setHabLevel);
        defenseWrapper = new RadioWrapper(this, EndgameIDs.DEFENSE_RATING_QUESTION, model::setDefenseRating);

        defenseAllMatchCheckBox = findViewById(EndgameIDs.DEFENSE_ALL_MATCH);
        defenseAllMatchCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setDefenseAllMatch(isChecked));

        foulsCheckBox = findViewById(EndgameIDs.FOULS);
        foulsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setFouls(isChecked));

        checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        habitatWrapper.cleanUp();
        habitatWrapper = null;

        defenseWrapper.cleanUp();
        defenseWrapper = null;

        defenseAllMatchCheckBox.setOnCheckedChangeListener(null);
        defenseAllMatchCheckBox = null;

        foulsCheckBox.setOnCheckedChangeListener(null);
        foulsCheckBox = null;

        model = null;
    }

    public void saveData(View view) {
        int unfinishedQuestionId = model.getUnfinishedQuestionId();
        if (unfinishedQuestionId != -1) {
            focusOnView(unfinishedQuestionId);
            showMessage("Unanswered questions.", Toast.LENGTH_LONG);
            return;
        }

        boolean saved = model.save();
        if (!saved) {
            showMessage("Failed to save! Talk to the programmers.", Toast.LENGTH_LONG);
            return;
        }

        finish();
    }
}
