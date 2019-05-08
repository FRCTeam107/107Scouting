package com.frc107.scouting.sandstorm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.cycle.CycleActivity;
import com.frc107.scouting.utils.ViewUtils;
import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.ui.questionWrappers.RadioWrapper;
import com.frc107.scouting.ui.questionWrappers.TextWrapper;

public class SandstormActivity extends BaseActivity {
    private RadioWrapper startingPosWrapper;
    private RadioWrapper startingPieceWrapper;
    private RadioWrapper itemPlacedWrapper;
    private TextWrapper teamNumWrapper;
    private TextWrapper matchNumWrapper;

    private EditText matchNumberEditText;

    private CheckBox crossedBaselineCheckbox;

    private SandstormViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandstorm);

        viewModel = new SandstormViewModel();

        matchNumberEditText = findViewById(R.id.matchNumberEditText);

        startingPosWrapper = new RadioWrapper(findViewById(R.id.sandstormStartingPositionRadioQuestion), viewModel);
        startingPieceWrapper = new RadioWrapper(findViewById(R.id.sandstormStartingGamePieceRadioQuestion), viewModel);
        itemPlacedWrapper = new RadioWrapper(findViewById(R.id.sandstormItemPlacedRadioQuestion), viewModel);

        teamNumWrapper = new TextWrapper(findViewById(R.id.teamNumberEditText), viewModel);
        matchNumWrapper = new TextWrapper(matchNumberEditText, viewModel);

        crossedBaselineCheckbox = findViewById(R.id.sandstormBaseline_chkbx);
        crossedBaselineCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAnswer(R.id.sandstormBaseline_chkbx, isChecked));
    }

    @Override
    protected void onResume() {
        super.onResume();
        int matchNum = Scouting.getInstance().getMatchNumber();
        String matchNumToShow = matchNum == -1 ? "" : matchNum + "";
        matchNumberEditText.setText(matchNumToShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        startingPosWrapper.cleanUp();
        startingPieceWrapper.cleanUp();
        itemPlacedWrapper.cleanUp();
        teamNumWrapper.cleanUp();
        matchNumWrapper.cleanUp();

        viewModel = null;
    }

    public void goToCycle(View view) {
        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (unfinishedQuestionId != -1) {
            ViewUtils.requestFocusToUnfinishedQuestion(findViewById(unfinishedQuestionId), this);
            return;
        }
        viewModel.finish();

        int teamNumber = viewModel.getTeamNumber();
        boolean shouldAllowStartingPiece = viewModel.shouldAllowStartingPiece();

        clearAnswers();

        ViewUtils.requestFocus(teamNumWrapper.getEditText(), this);

        Intent intent = new Intent(this, CycleActivity.class);
        intent.putExtra(ScoutingStrings.EXTRA_TEAM_NUM, teamNumber);
        intent.putExtra(ScoutingStrings.EXTRA_SHOULD_ALLOW_STARTING_PIECE_SANDSTORM, shouldAllowStartingPiece);
        startActivity(intent);

    }

    private void clearAnswers() {
        teamNumWrapper.clear();
        startingPosWrapper.clear();
        startingPieceWrapper.clear();
        itemPlacedWrapper.clear();
        crossedBaselineCheckbox.setChecked(false);
    }
}
