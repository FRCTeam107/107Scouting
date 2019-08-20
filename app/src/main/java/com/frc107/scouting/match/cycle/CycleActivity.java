package com.frc107.scouting.match.cycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.ui.questionwrappers.RadioWrapper;
import com.frc107.scouting.core.utils.ViewUtils;
import com.frc107.scouting.match.endgame.EndgameActivity;
import com.frc107.scouting.match.sandstorm.SandstormAnswers;
import com.frc107.scouting.match.sandstorm.SandstormData;

public class CycleActivity extends BaseActivity {
    private RadioWrapper pickupLocationWrapper;
    private RadioWrapper itemPickedUpWrapper;
    private RadioWrapper locationPlacedWrapper;
    private CheckBox defenseCheckBox;
    private CheckBox allCycleDefenseCheckBox;

    private SandstormData sandstormData;
    private CycleModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        // Grab the sandstorm data that we were given.
        sandstormData = getIntent().getParcelableExtra(ScoutingStrings.SANDSTORM_DATA_EXTRA_KEY);

        // Display the team number at the top of the screen.
        getSupportActionBar().setTitle("Team: " + sandstormData.getTeamNumber());

        model = ViewModelProviders.of(this).get(CycleModel.class);

        pickupLocationWrapper = new RadioWrapper(this, CycleIDs.PICKUP_LOCATION, model::setPickupLocation);
        itemPickedUpWrapper = new RadioWrapper(this, CycleIDs.ITEM_PICKED_UP, model::setItemPickedUp);
        locationPlacedWrapper = new RadioWrapper(this, CycleIDs.LOCATION_PLACED, model::setLocationPlaced);

        defenseCheckBox = findViewById(CycleIDs.DEFENSE);
        defenseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setDefense(isChecked));

        allCycleDefenseCheckBox = findViewById(CycleIDs.ONLY_DEFENSE);
        allCycleDefenseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.setDefenseAllCycle(isChecked);

            // The status of this checkbox effects other buttons, so refresh the necessary UI elements.
            refreshForAllCycleDefense(isChecked);
        });

        // Make sure the buttons available align with what was picked in sandstorm.
        boolean startedWithoutAPiece = sandstormData.getStartingGamePiece() == SandstormAnswers.NO_PIECE;
        boolean placedPiece = sandstormData.getPlacedLocation() != SandstormAnswers.NOT_PLACED;
        if (startedWithoutAPiece || placedPiece)
            disableStartedWithItem();
        else
            enableStartedWithItem();

        checkForPermissions();
    }

    private void enableStartedWithItem() {
        RadioButton portRadioButton = findViewById(CycleIDs.PORT_OPTION);
        portRadioButton.setEnabled(false);
        portRadioButton.setVisibility(View.GONE);

        RadioButton floorRadioButton = findViewById(CycleIDs.FLOOR_OPTION);
        floorRadioButton.setEnabled(false);
        floorRadioButton.setVisibility(View.GONE);
    }

    private void disableStartedWithItem() {
        RadioButton portRadioButton = findViewById(CycleIDs.PORT_OPTION);
        portRadioButton.setEnabled(true);
        portRadioButton.setVisibility(View.VISIBLE);

        RadioButton floorRadioButton = findViewById(CycleIDs.FLOOR_OPTION);
        floorRadioButton.setEnabled(true);
        floorRadioButton.setVisibility(View.VISIBLE);

        RadioButton startedWithItemButton = findViewById(CycleIDs.STARTED_WITH_ITEM_OPTION);
        startedWithItemButton.setVisibility(View.GONE);
        startedWithItemButton.setEnabled(false);
    }

    private void refreshForAllCycleDefense(boolean allCycleDefense) {
        boolean questionsEnabled = !allCycleDefense;
        RadioGroup pickupLocationGroup = pickupLocationWrapper.getRadioGroup();
        RadioGroup itemPickedUpGroup = itemPickedUpWrapper.getRadioGroup();
        RadioGroup locationPlacedGroup = locationPlacedWrapper.getRadioGroup();

        ViewUtils.setRadioGroupEnabled(pickupLocationGroup, questionsEnabled);
        ViewUtils.setRadioGroupEnabled(itemPickedUpGroup, questionsEnabled);
        ViewUtils.setRadioGroupEnabled(locationPlacedGroup, questionsEnabled);

        defenseCheckBox.setEnabled(questionsEnabled);
        defenseCheckBox.setChecked(allCycleDefense);

        if (allCycleDefense) {
            pickupLocationGroup.clearCheck();
            itemPickedUpGroup.clearCheck();
            locationPlacedGroup.clearCheck();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        pickupLocationWrapper.cleanUp();
        pickupLocationWrapper = null;

        itemPickedUpWrapper.cleanUp();
        itemPickedUpWrapper = null;

        locationPlacedWrapper.cleanUp();
        locationPlacedWrapper = null;

        defenseCheckBox.setOnCheckedChangeListener(null);
        defenseCheckBox = null;

        allCycleDefenseCheckBox.setOnCheckedChangeListener(null);
        allCycleDefenseCheckBox = null;

        model = null;
    }

    public void tryToGoToEndgame(View view) {
        if (model.areAllQuestionsUnanswered()) {
            openEndgameActivity();
            return;
        }

        int id = model.getUnfinishedQuestionId();
        if (id != -1) {
            focusOnView(id);
            return;
        }

        model.finishCycle();
        openEndgameActivity();
        finish();
    }

    private void openEndgameActivity() {
        Intent intent = new Intent(this, EndgameActivity.class);
        intent.putExtra(ScoutingStrings.SANDSTORM_DATA_EXTRA_KEY, sandstormData);
        intent.putExtra(ScoutingStrings.CYCLE_DATA_EXTRA_KEY, model.getData());
        startActivity(intent);
    }

    public void tryToEnterNewCycle(View view) {
        int id = model.getUnfinishedQuestionId();
        if (id != -1) {
            focusOnView(id);
            showMessage("Unanswered questions.", Toast.LENGTH_LONG);
            return;
        }

        model.finishCycle();
        refreshForNewCycle();
    }

    private void refreshForNewCycle() {
        clearAnswers();
        refreshForAllCycleDefense(false);
        disableStartedWithItem();
    }

    private void clearAnswers() {
        pickupLocationWrapper.clear();
        itemPickedUpWrapper.clear();
        locationPlacedWrapper.clear();

        defenseCheckBox.setChecked(false);
        allCycleDefenseCheckBox.setChecked(false);
    }
}
