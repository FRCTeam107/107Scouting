package com.frc107.scouting.deepspace.pit;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.frc107.scouting.BuildConfig;
import com.frc107.scouting.R;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.utils.PermissionUtils;
import com.frc107.scouting.core.utils.StringUtils;
import com.frc107.scouting.core.utils.ViewUtils;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.ui.questionwrappers.RadioWrapper;
import com.frc107.scouting.core.ui.questionwrappers.TextWrapper;

import java.io.File;
import java.io.IOException;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

public class PitActivity extends BaseActivity {
    private RadioWrapper sandstormOpWrapper;
    private RadioWrapper sandstormPrefWrapper;
    private RadioWrapper highestRocketLevelWrapper;
    private RadioWrapper highestHabLevelWrapper;
    private TextWrapper programmingLangWrapper;
    private TextWrapper teamNumWrapper;
    private TextWrapper habTimeWrapper;
    private TextWrapper arcadeGameWrapper;
    private TextWrapper commentsWrapper;

    private PitViewModel viewModel;

    private static final int REQUEST_CODE_CAMERA = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit);

        viewModel = ViewModelProviders.of(this).get(PitViewModel.class);

        sandstormOpWrapper = new RadioWrapper(this, PitIDs.SANDSTORM_OP, viewModel::setSandstormOperation);
        sandstormPrefWrapper = new RadioWrapper(this, PitIDs.SANDSTORM_PREF, viewModel::setSandstormPreference);
        highestRocketLevelWrapper = new RadioWrapper(this, PitIDs.SANDSTORM_HIGHEST_ROCKET_LEVEL, viewModel::setSandstormHighestRocketLevel);
        highestHabLevelWrapper = new RadioWrapper(this, PitIDs.HIGHEST_HAB, viewModel::setHighestHabitat);

        programmingLangWrapper = new TextWrapper(this, PitIDs.PROGRAMMING_LANG, viewModel::setProgrammingLanguage);
        teamNumWrapper = new TextWrapper(this, PitIDs.TEAM_NUM, viewModel::setTeamNumber);
        habTimeWrapper = new TextWrapper(this, PitIDs.HAB_TIME, viewModel::setHabitatTime);
        arcadeGameWrapper = new TextWrapper(this, PitIDs.BONUS, viewModel::setBonus);
        commentsWrapper = new TextWrapper(this, PitIDs.COMMENTS, viewModel::setComments);

        checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sandstormOpWrapper.cleanUp();
        sandstormPrefWrapper.cleanUp();
        highestRocketLevelWrapper.cleanUp();
        highestHabLevelWrapper.cleanUp();
        programmingLangWrapper.cleanUp();
        teamNumWrapper.cleanUp();
        habTimeWrapper.cleanUp();
        arcadeGameWrapper.cleanUp();
        commentsWrapper.cleanUp();

        viewModel = null;
    }

    public void savePitData(View view) {
        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        int id = viewModel.getUnfinishedQuestionId();
        if (id != -1) {
            focusOnView(id);
            showMessage("Unanswered questions.", Toast.LENGTH_LONG);
            return;
        }

        boolean successfullySaved = viewModel.save();
        String message;
        if (successfullySaved)
            message = "Saved data successfully.";
        else
            message = "Error while saving data.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);

        finish();
    }

    public void openCamera(View view) {
        String teamNumber = teamNumWrapper.getValue();
        if (StringUtils.isEmptyOrNull(teamNumber)) {
            ViewUtils.requestFocus(teamNumWrapper.getEditText(), this);
            Toast.makeText(getApplicationContext(), "Enter a team number.", Toast.LENGTH_LONG).show();
            return;
        }

        boolean hasCameraPermissions = PermissionUtils.checkForPermission(this, Manifest.permission.CAMERA);
        if (!hasCameraPermissions) {
            Toast.makeText(getApplicationContext(), "No camera permissions.", Toast.LENGTH_LONG).show();
            checkForPermissions();
            return;
        }

        boolean hasWritePermissions = PermissionUtils.checkForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!hasWritePermissions) {
            Toast.makeText(getApplicationContext(), "No write permissions.", Toast.LENGTH_LONG).show();
            checkForPermissions();
            return;
        }

        boolean hasReadPermissions = PermissionUtils.checkForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!hasReadPermissions) {
            Toast.makeText(getApplicationContext(), "No read permissions.", Toast.LENGTH_LONG).show();
            checkForPermissions();
            return;
        }

        File photoFile;
        try {
            photoFile = viewModel.createPhotoFile();
        } catch (IOException e) {
            Logger.log(e.getLocalizedMessage());
            showMessage("Failure while trying to create image file.", Toast.LENGTH_LONG);
            return;
        }

        Uri outputUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(getApplicationContext(), "Failure trying to take picture.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            try {
                viewModel.compressPhoto(this);
                Toast.makeText(this, "Successfully took photo!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Failure while compressing photo.", Toast.LENGTH_SHORT).show();
                Logger.log(e.getLocalizedMessage());
            }
        }
    }
}
