package com.frc107.scouting.pit;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Toast;

import com.frc107.scouting.BuildConfig;
import com.frc107.scouting.R;
import com.frc107.scouting.utils.PermissionUtils;
import com.frc107.scouting.utils.StringUtils;
import com.frc107.scouting.utils.ViewUtils;
import com.frc107.scouting.BaseActivity;
import com.frc107.scouting.ui.questionWrappers.RadioWrapper;
import com.frc107.scouting.ui.questionWrappers.TextWrapper;

import java.io.File;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by Matt on 9/30/2017.
 */

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

        SparseIntArray radioButtonMappings = new SparseIntArray();
        radioButtonMappings.put(R.id.visionSystemSandstorm_Radiobtn, 0);
        radioButtonMappings.put(R.id.cameraDrivingSandstorm_Radiobtn, 1);
        radioButtonMappings.put(R.id.blindDrivingSandstorm_Radiobtn, 2);
        radioButtonMappings.put(R.id.noDrivingSandstorm_Radiobtn, 3);

        radioButtonMappings.put(R.id.rocketshipPreferenceSandstorm_Radiobtn, 0);
        radioButtonMappings.put(R.id.cargoshipPreferenceSandstorm_Radiobtn, 1);
        radioButtonMappings.put(R.id.noPreferenceSandstorm_Radiobtn, 2);

        radioButtonMappings.put(R.id.topRocketLevelSandstorm_Radiobtn, 0);
        radioButtonMappings.put(R.id.middleRocketLevelSandstorm_Radiobtn, 1);
        radioButtonMappings.put(R.id.bottomRocketLevelSandstorm_Radiobtn, 2);
        radioButtonMappings.put(R.id.noRocketLevelSandstorm_Radiobtn, 3);

        radioButtonMappings.put(R.id.topHabitatLevel_Radiobtn, 0);
        radioButtonMappings.put(R.id.middleRocketLevelSandstorm_Radiobtn, 1);
        radioButtonMappings.put(R.id.bottomRocketLevelSandstorm_Radiobtn, 2);
        radioButtonMappings.put(R.id.noHabitatLevel_Radiobtn, 3);

        sandstormOpWrapper = new RadioWrapper(
                findViewById(R.id.pit_sandstorm_op),
                radioButtonMappings,
                viewModel::setSandstormOperation);
        sandstormPrefWrapper = new RadioWrapper(findViewById(R.id.pit_sandstorm_preference),
                radioButtonMappings,
                viewModel::setSandstormPreference);
        highestRocketLevelWrapper = new RadioWrapper(findViewById(R.id.pit_sandstorm_highest_rocket_level),
                radioButtonMappings,
                viewModel::setSandstormHighestRocketLevel);
        highestHabLevelWrapper = new RadioWrapper(findViewById(R.id.pit_highest_habitat),
                radioButtonMappings,
                viewModel::setHighestHabitat);

        programmingLangWrapper = new TextWrapper(findViewById(R.id.pit_programming_language),               viewModel::setProgrammingLanguage);
        teamNumWrapper = new TextWrapper(findViewById(R.id.pit_team_number),                                viewModel::setTeamNumber);
        habTimeWrapper = new TextWrapper(findViewById(R.id.pit_habitat_time),                               viewModel::setHabitatTime);
        arcadeGameWrapper = new TextWrapper(findViewById(R.id.pit_bonus),                                   viewModel::setBonus);
        commentsWrapper = new TextWrapper(findViewById(R.id.pit_comments),                                  viewModel::setComments);

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

        if (!viewModel.isFinished()) {
            // todo: replace this focusOnView(viewModel.getUnfinishedQuestionId());
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
            ViewUtils.requestFocus(findViewById(R.id.pit_team_number), this);
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

        File photoFile = viewModel.createPhotoFile();
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
        /*if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (!viewModel.rotateAndCompressPhoto())
                Toast.makeText(this, "Failure while compressing photo.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Successfully took photo!", Toast.LENGTH_SHORT).show();
        }*/
    }
}
