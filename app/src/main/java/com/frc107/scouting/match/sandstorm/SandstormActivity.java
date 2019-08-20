package com.frc107.scouting.match.sandstorm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.match.cycle.CycleActivity;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.ui.questionwrappers.RadioWrapper;
import com.frc107.scouting.core.ui.questionwrappers.TextWrapper;
import com.frc107.scouting.core.utils.PermissionUtils;

public class SandstormActivity extends BaseActivity {
    private TextWrapper matchNumberWrapper;
    private TextWrapper teamNumberWrapper;
    private RadioWrapper startingLocationWrapper;
    private RadioWrapper startingGamePieceWrapper;
    private RadioWrapper placedLocationWrapper;
    private CheckBox crossedBaselineCheckBox;

    private SandstormModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandstorm);

        model = ViewModelProviders.of(this).get(SandstormModel.class);

        matchNumberWrapper = new TextWrapper(this, SandstormIDs.MATCH_NUM, model::setMatchNumber);

        // We want to be able to auto-increment the match number, so here we grab the current match number if there is one.
        Integer matchNumber = model.getMatchNumber();
        if (matchNumber != null)
            matchNumberWrapper.getEditText().setText(matchNumber.toString());

        teamNumberWrapper = new TextWrapper(this, SandstormIDs.TEAM_NUM, model::setTeamNumber);

        startingLocationWrapper = new RadioWrapper(this, SandstormIDs.STARTING_LOCATION, model::setStartingLocation);
        startingGamePieceWrapper = new RadioWrapper(this, SandstormIDs.STARTING_GAME_PIECE, model::setStartingGamePiece);
        placedLocationWrapper = new RadioWrapper(this, SandstormIDs.PLACED_LOCATION, model::setPlacedLocation);

        crossedBaselineCheckBox = findViewById(SandstormIDs.CROSSED_BASELINE);
        crossedBaselineCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setCrossedBaseline(isChecked));

        checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        matchNumberWrapper.cleanUp();
        matchNumberWrapper = null;

        teamNumberWrapper.cleanUp();
        teamNumberWrapper = null;

        startingLocationWrapper.cleanUp();
        startingLocationWrapper = null;

        startingGamePieceWrapper.cleanUp();
        startingGamePieceWrapper = null;

        placedLocationWrapper.cleanUp();
        placedLocationWrapper = null;

        crossedBaselineCheckBox.setOnCheckedChangeListener(null);
        crossedBaselineCheckBox = null;

        model = null;
    }

    public void enterCycles(View view) {
        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        // Make sure we don't miss any unfinished questions.
        int id = model.getUnfinishedQuestionId();
        if (id != -1) {
            focusOnView(id);
            showMessage("Unanswered questions.", Toast.LENGTH_LONG);
            return;
        }

        // Tell the model to finalize.
        model.finish();

        // Go to the cycle screen with attached sandstorm data.
        SandstormData data = model.getData();
        Intent intent = new Intent(this, CycleActivity.class);
        intent.putExtra(ScoutingStrings.SANDSTORM_DATA_EXTRA_KEY, data);
        startActivity(intent);

        // Clear the answers for reuse.
        clearAnswers();
    }

    private void clearAnswers() {
        matchNumberWrapper.clear();
        teamNumberWrapper.clear();
        startingLocationWrapper.clear();
        startingGamePieceWrapper.clear();
        placedLocationWrapper.clear();
        crossedBaselineCheckBox.setChecked(false);
    }
}
