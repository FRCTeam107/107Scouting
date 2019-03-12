package com.frc107.scouting2019.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.frc107.scouting2019.R;
import com.frc107.scouting2019.utils.ViewUtils;
import com.frc107.scouting2019.view.wrappers.RadioWrapper;
import com.frc107.scouting2019.view.wrappers.TextWrapper;
import com.frc107.scouting2019.viewmodel.SandstormViewModel;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SandstormActivity extends BaseActivity {
    private RadioWrapper startingPosWrapper;
    private RadioWrapper startingPieceWrapper;
    private RadioWrapper itemPlacedWrapper;
    private TextWrapper teamNumWrapper;
    private TextWrapper matchNumWrapper;

    private SandstormViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandstorm);

        viewModel = new SandstormViewModel();

        startingPosWrapper = new RadioWrapper(findViewById(R.id.sandstormStartingPositionRadioQuestion), viewModel);
        startingPieceWrapper = new RadioWrapper(findViewById(R.id.sandstormStartingGamePieceRadioQuestion), viewModel);
        itemPlacedWrapper = new RadioWrapper(findViewById(R.id.sandstormItemPlacedRadioQuestion), viewModel);

        teamNumWrapper = new TextWrapper(findViewById(R.id.teamNumberEditText), viewModel);
        matchNumWrapper = new TextWrapper(findViewById(R.id.matchNumberEditText), viewModel);

        checkForPermissions();
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

    private void checkForPermissions() {
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void goToCycle(View view) {
        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (unfinishedQuestionId != -1) {
            ViewUtils.requestFocusToUnfinishedQuestion(findViewById(unfinishedQuestionId), this);
            return;
        }
        viewModel.finish();
        clearAnswers();
        ViewUtils.requestFocus(teamNumWrapper.getEditText(), this);
        final Intent intent = new Intent(this, CycleActivity.class);
        startActivity(intent);

    }

    private void clearAnswers() {
        teamNumWrapper.clear();
        matchNumWrapper.clear();
        startingPosWrapper.clear();
        startingPieceWrapper.clear();
        itemPlacedWrapper.clear();
    }
}
