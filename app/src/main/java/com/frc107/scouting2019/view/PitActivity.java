package com.frc107.scouting2019.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.frc107.scouting2019.BuildConfig;
import com.frc107.scouting2019.R;
import com.frc107.scouting2019.utils.PermissionUtils;
import com.frc107.scouting2019.utils.ViewUtils;
import com.frc107.scouting2019.view.wrappers.RadioWrapper;
import com.frc107.scouting2019.view.wrappers.TextWrapper;
import com.frc107.scouting2019.viewmodel.PitViewModel;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * Created by Matt on 9/30/2017.
 */

public class PitActivity extends BaseActivity {
    private RadioWrapper sandstormOpWrapper;
    private RadioWrapper sandstormPrefWrapper;
    private RadioWrapper highestRocketLevelWrapper;
    private RadioWrapper highestHabLevelWrapper;
    private RadioWrapper programmingLangWrapper;
    private TextWrapper teamNumWrapper;
    private TextWrapper habTimeWrapper;
    private TextWrapper arcadeGameWrapper;
    private TextWrapper commentsWrapper;

    private EditText teamNumberEditText;
    private RadioGroup teleopPreferenceRadioGroup;
    private EditText cubeNumberInSwitchEditText;
    private EditText cubeNumberInScaleEditText;
    private EditText cubeNumberInExchangeEditText;
    private RadioGroup climbRadioGroup;
    private RadioGroup climbHelpRadioGroup;
    private RadioGroup programmingLanguageRadioGroup;
    private EditText habitatTimeEditText;
    private EditText bonusQuestionEditText;
    private EditText commentsEditText;

    private PitViewModel viewModel;

    private static final int REQUEST_CODE_CAMERA = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit);

        viewModel = new PitViewModel();

        sandstormOpWrapper = new RadioWrapper(findViewById(R.id.sandstormOperationsRadioQuestion), viewModel);
        sandstormPrefWrapper = new RadioWrapper(findViewById(R.id.sandstormPreferenceRadioQuestion), viewModel);
        highestRocketLevelWrapper = new RadioWrapper(findViewById(R.id.highestRocketLevelSandstormRadioQuestion), viewModel);
        highestHabLevelWrapper = new RadioWrapper(findViewById(R.id.highestHabitatLevelRadioQuestion), viewModel);
        programmingLangWrapper = new RadioWrapper(findViewById(R.id.programmingLanguageRadioQuestion), viewModel);

        teamNumWrapper = new TextWrapper(findViewById(R.id.pit_teamNumber_editText), viewModel);
        habTimeWrapper = new TextWrapper(findViewById(R.id.pit_habitatTime_editText), viewModel);
        arcadeGameWrapper = new TextWrapper(findViewById(R.id.pit_bonusQuestion_editText), viewModel);
        commentsWrapper = new TextWrapper(findViewById(R.id.pit_comments_editText), viewModel);

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
        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (unfinishedQuestionId != -1) {
            ViewUtils.requestFocus(findViewById(unfinishedQuestionId), this);
            return;
        }

        boolean hasWritePermissions = PermissionUtils.getPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!hasWritePermissions) {
            Toast.makeText(getApplicationContext(), "No write permissions.", Toast.LENGTH_LONG).show();
            return;
        }

        String saveResponse = viewModel.save();

        Toast.makeText(getApplicationContext(), saveResponse, Toast.LENGTH_LONG).show();

        setResult(RESULT_OK);

        finish();
    }

    public void openCamera(View view) {
        String teamNumber = viewModel.getAnswerForQuestion(R.id.pit_teamNumber_editText);
        if (teamNumber == null) {
            ViewUtils.requestFocus(findViewById(R.id.pit_teamNumber_editText), this);
            return;
        }

        boolean hasCameraPermissions = PermissionUtils.getPermissions(this, Manifest.permission.CAMERA);
        if (!hasCameraPermissions) {
            Toast.makeText(getApplicationContext(), "No camera permissions.", Toast.LENGTH_LONG).show();
            checkForPermissions();
            return;
        }

        boolean hasWritePermissions = PermissionUtils.getPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!hasWritePermissions) {
            Toast.makeText(getApplicationContext(), "No write permissions.", Toast.LENGTH_LONG).show();
            checkForPermissions();
            return;
        }

        boolean hasReadPermissions = PermissionUtils.getPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (!viewModel.rotateAndCompressPhoto())
                Toast.makeText(this, "Failure while compressing photo.", Toast.LENGTH_SHORT);
        }
    }
}
