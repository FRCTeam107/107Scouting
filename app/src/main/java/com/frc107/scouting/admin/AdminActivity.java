package com.frc107.scouting.admin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frc107.scouting.R;
import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.duck.DuckActivity;
import com.frc107.scouting.ui.IUIListener;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.analysis.attribute.AttributeAnalysisActivity;
import com.frc107.scouting.analysis.team.TeamAnalysisActivity;
import com.frc107.scouting.utils.PermissionUtils;

import java.util.ArrayList;

/**
 * Created by Matt on 10/9/2017.
 */

public class AdminActivity extends BaseActivity implements IUIListener {
    private AdminViewModel viewModel;
    private EditText eventKeyEditText;
    private Button sendPitDataButton;
    private Button sendConcatPitDataButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private TextWatcher eventKeyTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        viewModel = new AdminViewModel(this);

        pref = getApplicationContext().getSharedPreferences(Scouting.PREFERENCES_NAME, MODE_PRIVATE);
        prefEditor = pref.edit();

        eventKeyEditText = findViewById(R.id.eventKeyEditText);
        eventKeyEditText.setText(viewModel.getEventKey());

        eventKeyTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEventKey(s.toString());
                prefEditor.putString(Scouting.EVENT_KEY_PREFERENCE, s.toString());
                prefEditor.apply();
            }
            public void afterTextChanged(Editable s) { }
        };
        eventKeyEditText.addTextChangedListener(eventKeyTextWatcher);

        sendPitDataButton = findViewById(R.id.send_pit_data);
        sendPitDataButton.setOnLongClickListener(v -> {
            viewModel.toggleDuckButton();
            return true;
        });

        sendConcatPitDataButton = findViewById(R.id.send_concat_pit_data);
        sendConcatPitDataButton.setOnLongClickListener(v -> {
            if (viewModel.duckButtonIsPressed()) {
                Intent duckIntent = new Intent(this, DuckActivity.class);
                startActivity(duckIntent);
                viewModel.toggleDuckButton();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventKeyEditText.removeTextChangedListener(eventKeyTextWatcher);
        eventKeyTextWatcher = null;

        sendPitDataButton.setOnLongClickListener(null);
        sendConcatPitDataButton.setOnLongClickListener(null);
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
        String message = "OPRs downloaded successfully.";
        if (error)
            message = "Error while downloading OPRs. Double-check your event key.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}