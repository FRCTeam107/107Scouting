package com.frc107.scouting.cycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.frc107.scouting.R;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.admin.AdminActivity;
import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.utils.ViewUtils;
import com.frc107.scouting.endgame.EndGameActivity;
import com.frc107.scouting.MainActivity;
import com.frc107.scouting.ui.questionWrappers.RadioWrapper;

public class CycleActivity extends BaseActivity {
    private RadioWrapper pickupLocationWrapper;
    private RadioWrapper itemPickedUpWrapper;
    private RadioWrapper itemPlacedWrapper;

    private RadioGroup pickupLocationRadioGroup;
    private RadioGroup itemPickedUpRadioGroup;
    private RadioGroup itemPlacedRadioGroup;

    private CheckBox defenseCheckbox;
    private CheckBox allDefenseCheckbox;

    private CycleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        int teamNumber = getIntent().getIntExtra(ScoutingStrings.EXTRA_TEAM_NUM, -1);
        getSupportActionBar().setTitle("Team: " + teamNumber);

        boolean hasUsedStartingItem = getIntent().getBooleanExtra(ScoutingStrings.EXTRA_HAS_USED_STARTING_PIECE_SANDSTORM, true);
        if (hasUsedStartingItem) {
            enableStartedWithItem();
        } else {
            disableStartedWithItem();
        }

        viewModel = new CycleViewModel(teamNumber, hasUsedStartingItem);

        pickupLocationWrapper = new RadioWrapper(findViewById(R.id.pickupLocationRadioQuestion), viewModel);
        itemPickedUpWrapper = new RadioWrapper(findViewById(R.id.itemPickedUpRadioQuestion), viewModel);
        itemPlacedWrapper = new RadioWrapper(findViewById(R.id.itemPlacedRadioQuestion), viewModel);

        pickupLocationRadioGroup = findViewById(R.id.pickupLocationRadioQuestion);
        itemPickedUpRadioGroup = findViewById(R.id.itemPickedUpRadioQuestion);
        itemPlacedRadioGroup = findViewById(R.id.itemPlacedRadioQuestion);

        defenseCheckbox = findViewById(R.id.defense_chkbx);
        allDefenseCheckbox = findViewById(R.id.allDefense_chkbx);

        pickupLocationRadioGroup.setOnCheckedChangeListener((group, checkedId) -> viewModel.setAnswer(R.id.pickupLocationRadioQuestion, checkedId));
        itemPickedUpRadioGroup.setOnCheckedChangeListener((group, checkedId) -> viewModel.setAnswer(R.id.itemPickedUpRadioQuestion, checkedId));
        itemPlacedRadioGroup.setOnCheckedChangeListener((group, checkedId) -> viewModel.setAnswer(R.id.itemPlacedRadioQuestion, checkedId));
        defenseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAnswer(R.id.defense_chkbx, isChecked));
        allDefenseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setAnswer(R.id.allDefense_chkbx, isChecked);
            setAllDefense(isChecked);
        });
    }

    private void setAllDefense(boolean allDefense) {
        boolean questionsEnabled = !allDefense;
        ViewUtils.setRadioGroupEnabled(pickupLocationRadioGroup, questionsEnabled);
        ViewUtils.setRadioGroupEnabled(itemPickedUpRadioGroup, questionsEnabled);
        ViewUtils.setRadioGroupEnabled(itemPlacedRadioGroup, questionsEnabled);

        defenseCheckbox.setEnabled(questionsEnabled);
        defenseCheckbox.setChecked(allDefense);

        if (allDefense) {
            pickupLocationRadioGroup.clearCheck();
            itemPickedUpRadioGroup.clearCheck();
            itemPlacedRadioGroup.clearCheck();
        }

        viewModel.setAllDefense(allDefense);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        pickupLocationWrapper.cleanUp();
        itemPickedUpWrapper.cleanUp();
        itemPlacedWrapper.cleanUp();
        defenseCheckbox.setOnCheckedChangeListener(null);

        viewModel = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cycle_teleop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.send_data:
                startActivity(new Intent(this, AdminActivity.class));
                return true;
            case R.id.go_to_endgame:
                goToEndGame();
                return true;
            case R.id.clear_cycle:
                clearAnswers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToEndGame() {
        if (!viewModel.isFinished()) {
            focusOnView(viewModel.getUnfinishedQuestionId());
            return;
        }

        viewModel.finish();

        int teamNumber = viewModel.getTeamNumber();

        final Intent intent = new Intent(this, EndGameActivity.class);
        intent.putExtra(ScoutingStrings.EXTRA_TEAM_NUM, teamNumber);
        startActivity(intent);

        finish();
    }

    public void goToNextCycle(View view) {
        if (!viewModel.isFinished()) {
            focusOnView(viewModel.getUnfinishedQuestionId());
            return;
        }

        viewModel.finish();

        if (viewModel.hasUsedStartingItem())
            disableStartedWithItem();

        clearAnswers();
    }

    private void enableStartedWithItem() {
        RadioButton portRadioButton = findViewById(R.id.portPickupLocation_Radiobtn);
        portRadioButton.setEnabled(false);
        portRadioButton.setVisibility(View.GONE);

        RadioButton floorRadioButton = findViewById(R.id.floorPickupLocation_Radiobtn);
        floorRadioButton.setEnabled(false);
        floorRadioButton.setVisibility(View.GONE);
    }

    private void disableStartedWithItem() {
        RadioButton portRadioButton = findViewById(R.id.portPickupLocation_Radiobtn);
        portRadioButton.setEnabled(true);
        portRadioButton.setVisibility(View.VISIBLE);

        RadioButton floorRadioButton = findViewById(R.id.floorPickupLocation_Radiobtn);
        floorRadioButton.setEnabled(true);
        floorRadioButton.setVisibility(View.VISIBLE);

        RadioButton startedWithItemButton = findViewById(R.id.startedWithItem_Radiobtn);
        startedWithItemButton.setVisibility(View.GONE);
        startedWithItemButton.setEnabled(false);
    }

    private void clearAnswers() {
        pickupLocationWrapper.clear();
        itemPickedUpWrapper.clear();
        itemPlacedWrapper.clear();
        defenseCheckbox.setChecked(false);
        allDefenseCheckbox.setChecked(false);
    }
}
