package com.frc107.scouting.match.cycle;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.match.sandstorm.SandstormData;
import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.ui.questionWrappers.RadioWrapper;
import com.frc107.scouting.utils.PermissionUtils;

public class CycleActivity extends BaseActivity {
    private RadioWrapper pickupLocationWrapper;
    private RadioWrapper itemPickedUpWrapper;
    private RadioWrapper locationPlacedWrapper;
    private CheckBox defenseCheckBox;
    private CheckBox onlyDefenseCheckBox;

    private SandstormData sandstormData;
    private CycleModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        sandstormData = getIntent().getParcelableExtra(ScoutingStrings.SANDSTORM_DATA_EXTRA_KEY);

        model = ViewModelProviders.of(this).get(CycleModel.class);

        pickupLocationWrapper = new RadioWrapper(this, CycleIDs.PICKUP_LOCATION, model::setPickupLocation);
        itemPickedUpWrapper = new RadioWrapper(this, CycleIDs.ITEM_PICKED_UP, model::setItemPickedUp);
        locationPlacedWrapper = new RadioWrapper(this, CycleIDs.LOCATION_PLACED, model::setLocationPlaced);

        defenseCheckBox = findViewById(CycleIDs.DEFENSE);
        defenseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setDefense(isChecked));

        onlyDefenseCheckBox = findViewById(CycleIDs.ONLY_DEFENSE);
        onlyDefenseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> model.setOnlyDefense(isChecked));

        checkForPermissions();
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

        onlyDefenseCheckBox.setOnCheckedChangeListener(null);
        onlyDefenseCheckBox = null;

        model = null;
    }

    public void enterNewCycle(View view) {
        if (!PermissionUtils.verifyWritePermissions(this))
            return;

        int id = model.getUnfinishedQuestionId();
        if (id != -1) {
            focusOnView(id);
            return;
        }

        CycleData cycleData = model.getData();
        //Intent intent = new Intent(this, CycleActivity.class);
        //intent.putExtra(ScoutingStrings.SANDSTORM_DATA_EXTRA_KEY, data);
        //intent.putExtra(ScoutingStrings.CYCLE_DATA_EXTRA_KEY, data);
        //startActivity(intent);

        clearAnswers();
        // todo: go to cycle with intent extra
    }

    private void clearAnswers() {
        pickupLocationWrapper.clear();
        itemPickedUpWrapper.clear();
        locationPlacedWrapper.clear();
        defenseCheckBox.setOnCheckedChangeListener(null);
        onlyDefenseCheckBox.setOnCheckedChangeListener(null);
    }
}
