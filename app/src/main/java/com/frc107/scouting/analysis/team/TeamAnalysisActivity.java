package com.frc107.scouting.analysis.team;

import android.app.AlertDialog;
import android.os.Bundle;

import com.frc107.scouting.form.FormActivity;
import com.frc107.scouting.R;
import com.frc107.scouting.utils.ISimpleCallback;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TeamAnalysisActivity extends FormActivity implements ISimpleCallback {
    private TeamAnalysisViewModel viewModel;
    private TextView teamNumberTextView;
    private TextView averageCargoTextView;
    private TextView averageHatchTextView;
    private TextView defenseCountTextView;
    private TextView effectiveDefenseCountTextView;
    private TextView habOneTextView;
    private TextView habTwoTextView;
    private TextView habThreeTextView;
    private TextView rocketOneTextView;
    private TextView rocketTwoTextView;
    private TextView rocketThreeTextView;
    private TextView oprTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_analysis);
        teamNumberTextView = findViewById(R.id.analysisTeamNumberTextView);
        averageCargoTextView = findViewById(R.id.averageCargoTextView);
        averageHatchTextView = findViewById(R.id.averageHatchPanelTextView);
        defenseCountTextView = findViewById(R.id.defenseCountTextView);
        effectiveDefenseCountTextView = findViewById(R.id.effectiveDefenseCountTextView);
        habOneTextView = findViewById(R.id.habOneTextView);
        habTwoTextView = findViewById(R.id.habTwoTextView);
        habThreeTextView = findViewById(R.id.habThreeTextView);
        rocketOneTextView = findViewById(R.id.rocketOneTextView);
        rocketTwoTextView = findViewById(R.id.rocketTwoTextView);
        rocketThreeTextView = findViewById(R.id.rocketThreeTextView);
        oprTextView = findViewById(R.id.oprTextView);

        viewModel = new TeamAnalysisViewModel(this);
        viewModel.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel = null;
    }

    private void onTeamNumberChanged(String teamNumber) {
        if (teamNumber.length() == 0)
            teamNumber = "-1";

        viewModel.setTeamNumber(teamNumber);

        teamNumberTextView.setText(teamNumber);

        averageHatchTextView.setText(viewModel.getAverageHatchPanels() + "");
        averageCargoTextView.setText(viewModel.getAverageCargo() + "");
        
        defenseCountTextView.setText(viewModel.getDefenseCount() + "");
        effectiveDefenseCountTextView.setText(viewModel.getEffectiveDefenseCount() + "");
        
        habOneTextView.setText(viewModel.canClimbHabOne() + "");
        habTwoTextView.setText(viewModel.canClimbHabTwo() + "");
        habThreeTextView.setText(viewModel.canClimbHabThree() + "");
        
        rocketOneTextView.setText(viewModel.canAccessRocketOne() + "");
        rocketTwoTextView.setText(viewModel.canAccessRocketTwo() + "");
        rocketThreeTextView.setText(viewModel.canAccessRocketThree() + "");

        oprTextView.setText(viewModel.getOPR() + "");
    }

    @Override
    public void callback(boolean success) {
        if (!success) {
            Toast.makeText(getApplicationContext(), "Error while loading data.", Toast.LENGTH_LONG).show();
            return;
        }

        findViewById(R.id.analysisProgressBar).setVisibility(View.GONE);
        findViewById(R.id.analysisTeamNumberButton).setEnabled(true);
    }

    public void teamNumberButtonPressed(View view) {
        String[] teamNumbers = viewModel.getTeamNumbers();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pick a team number");
        dialogBuilder.setItems(teamNumbers, (dialog, which) -> onTeamNumberChanged(teamNumbers[which]));
        dialogBuilder.show();
    }
}
