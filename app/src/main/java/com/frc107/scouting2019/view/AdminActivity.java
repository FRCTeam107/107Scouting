package com.frc107.scouting2019.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frc107.scouting2019.IUIListener;
import com.frc107.scouting2019.R;
import com.frc107.scouting2019.Scouting;
import com.frc107.scouting2019.analysis.attribute.AttributeAnalysisActivity;
import com.frc107.scouting2019.analysis.team.TeamAnalysisActivity;
import com.frc107.scouting2019.utils.PermissionUtils;
import com.frc107.scouting2019.viewmodel.AdminViewModel;

import java.util.ArrayList;

/**
 * Created by Matt on 10/9/2017.
 */

public class AdminActivity extends BaseActivity implements IUIListener {
    private AdminViewModel viewModel;
    private EditText eventKeyEditText;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewModel = new AdminViewModel(this);

        pref = getApplicationContext().getSharedPreferences(Scouting.PREFERENCES_NAME, MODE_PRIVATE);
        prefEditor = pref.edit();

        eventKeyEditText = findViewById(R.id.eventKeyEditText);
        eventKeyEditText.setText(viewModel.getEventKey());

        eventKeyEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEventKey(s.toString());
                prefEditor.putString(Scouting.EVENT_KEY_PREFERENCE, s.toString());
                prefEditor.apply();
            }
            public void afterTextChanged(Editable s) { }
        });
    }

    public void concatenateMatchData(View view) {
        String result = "Failure concatenating data.";
        if (viewModel.concatenateMatchData()) {
            result = "Successfully concatenated data.";
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    public void concatenatePitData(View view) {
        String result = "Failure concatenating data.";
        if (viewModel.concatenatePitData()) {
            result = "Successfully concatenated data.";
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    public void sendRobotPhotos(View view) {
        if (!PermissionUtils.getPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/jpeg");
        intent.setPackage("com.android.bluetooth");

        ArrayList<Uri> uriList = viewModel.getPhotoUriList(this);
        if (uriList.isEmpty()) {
            Toast.makeText(this, "No photos.", Toast.LENGTH_SHORT).show();
        } else {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            startActivity(Intent.createChooser(intent, "Share app"));
        }
    }

    public void sendPitData(View view) {
        sendFile(viewModel.getPitFile(false));
    }

    public void sendConcatMatchData(View view) {
        sendFile(viewModel.getMatchFile(true));
    }

    public void sendConcatPitData(View view) {
        sendFile(viewModel.getPitFile(true));
    }

    public void goToTeamAnalysis(View view) {
        Intent intent = new Intent(getApplicationContext(), TeamAnalysisActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToAttributeAnalysis(View view) {
        Intent intent = new Intent(getApplicationContext(), AttributeAnalysisActivity.class);
        startActivity(intent);
        finish();
    }

    public void downloadOPRs(View view) {
        viewModel.downloadOPRs();
    }

    @Override
    public void callback(boolean error) {
        if (!error)
            Toast.makeText(getApplicationContext(), "OPRs downloaded successfully.", Toast.LENGTH_LONG).show();
    }
}