package com.frc107.scouting2019.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.frc107.scouting2019.R;
import com.frc107.scouting2019.Scouting;
import com.frc107.scouting2019.utils.PermissionUtils;
import com.frc107.scouting2019.utils.ViewUtils;
import com.frc107.scouting2019.view.wrappers.RadioWrapper;
import com.frc107.scouting2019.viewmodel.CycleViewModel;

import androidx.appcompat.app.AppCompatActivity;



public class CycleActivity extends AppCompatActivity {
    private RadioWrapper pickupLocationWrapper;
    private RadioWrapper itemPickedUpWrapper;
    private RadioWrapper itemPlacedWrapper;
    private CheckBox defenseCheckbox;

    private CycleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        viewModel = new CycleViewModel();

        getSupportActionBar().setTitle("Team: " + Scouting.getInstance().getTeamNumber());

        pickupLocationWrapper = new RadioWrapper(findViewById(R.id.pickupLocationRadioQuestion), viewModel);
        itemPickedUpWrapper = new RadioWrapper(findViewById(R.id.itemPickedUpRadioQuestion), viewModel);
        itemPlacedWrapper = new RadioWrapper(findViewById(R.id.itemPlacedRadioQuestion), viewModel);

        defenseCheckbox = findViewById(R.id.defense_chkbx);
        defenseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAnswer(R.id.defense_chkbx, isChecked));
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
        if (viewModel.isTeleop())
            getMenuInflater().inflate(R.menu.cycle_teleop_menu, menu);
        else
            getMenuInflater().inflate(R.menu.cycle_sandstorm_menu, menu);
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
            case R.id.enter_teleop_cycle:
                goToTeleop();
                return true;
            case R.id.go_to_endgame:
                goToEndGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToTeleop() {
        boolean allQuestionsAreUnanswered = viewModel.areNoQuestionsAnswered();

        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (!allQuestionsAreUnanswered && unfinishedQuestionId != -1) {
            ViewUtils.requestFocusToUnfinishedQuestion(findViewById(unfinishedQuestionId), this);
            return;
        }

        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        if (!allQuestionsAreUnanswered)
            viewModel.finishCycle();

        viewModel.turnTeleopOn();

        clearAnswers();

        invalidateOptionsMenu(); // Calling this tells Android to call onCreateOptionsMenu.
    }

    private void goToEndGame() {
        boolean allQuestionsAreUnanswered = viewModel.areNoQuestionsAnswered();

        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (!allQuestionsAreUnanswered && unfinishedQuestionId != -1) {
            ViewUtils.requestFocusToUnfinishedQuestion(findViewById(unfinishedQuestionId), this);
            return;
        }

        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        if (!allQuestionsAreUnanswered)
            viewModel.finishCycle();

        final Intent intent = new Intent(this, EndGameActivity.class);
        startActivity(intent);

        finish();
    }

    public void goToNextCycle(View view) {
        int unfinishedQuestionId = viewModel.getFirstUnfinishedQuestionId();
        if (unfinishedQuestionId != -1) {
            ViewUtils.requestFocusToUnfinishedQuestion(findViewById(unfinishedQuestionId), this);
            return;
        }

        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        viewModel.finishCycle();
        clearAnswers();
    }

    private void clearAnswers() {
        pickupLocationWrapper.clear();
        itemPickedUpWrapper.clear();
        itemPlacedWrapper.clear();
        defenseCheckbox.setChecked(false);
    }
}
