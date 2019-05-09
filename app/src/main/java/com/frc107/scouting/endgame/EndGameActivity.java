package com.frc107.scouting.endgame;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.frc107.scouting.R;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.model.FormStatus;
import com.frc107.scouting.utils.PermissionUtils;
import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.ui.questionWrappers.RadioWrapper;

public class EndGameActivity extends BaseActivity {
    private RadioWrapper habLevelWrapper;
    private RadioWrapper defenseWrapper;

    private CheckBox defenseAllMatchCheckbox;
    private CheckBox foulsCheckbox;

    private EndGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        viewModel = new EndGameViewModel();

        int teamNumber = getIntent().getIntExtra(ScoutingStrings.EXTRA_TEAM_NUM, -1);
        getSupportActionBar().setTitle("Team: " + teamNumber);

        habLevelWrapper = new RadioWrapper(findViewById(R.id.endGameHabitatLevelRadioQuestion), viewModel);
        defenseWrapper = new RadioWrapper(findViewById(R.id.endGameDefenseRadioQuestion), viewModel);

        defenseAllMatchCheckbox = findViewById(R.id.endGameDefenseAllMatch_chkbx);
        defenseAllMatchCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAnswer(R.id.endGameDefenseAllMatch_chkbx, isChecked));

        foulsCheckbox = findViewById(R.id.endGameFouls_chkbx);
        foulsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAnswer(R.id.endGameFouls_chkbx, isChecked));

        checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        habLevelWrapper.cleanUp();
        defenseWrapper.cleanUp();
        defenseAllMatchCheckbox.setOnCheckedChangeListener(null);
        foulsCheckbox.setOnCheckedChangeListener(null);

        viewModel = null;
    }

    public void saveData(View view) {
        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        if (!viewModel.isFinished()) {
            focusOnView(viewModel.getUnfinishedQuestionId());
            return;
        }

        boolean successfullySaved = viewModel.finish();
        String message;
        if (successfullySaved)
            message = "Saved data successfully.";
        else
            message = "Error while saving data.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        finish();
    }
}
