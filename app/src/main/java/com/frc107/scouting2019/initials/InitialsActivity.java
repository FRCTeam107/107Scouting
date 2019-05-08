package com.frc107.scouting2019.initials;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.scouting2019.R;
import com.frc107.scouting2019.sandstorm.SandstormActivity;
import com.frc107.scouting2019.ui.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class InitialsActivity extends BaseActivity {
    private InitialsViewModel viewModel;

    private TextInputEditText scouterInitialsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouter_initials);
        viewModel = new InitialsViewModel();

        scouterInitialsEditText = findViewById(R.id.scouterInitials_input);
        scouterInitialsEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setInitials(s.toString());
                // TODO: move this special behavior to model. Just call setAnswer and do a switch statement in the model where it handles questions being answered.
            }
            public void afterTextChanged(Editable s) { }
        });
    }

    public void submitInitials(View view) {
        String initials = viewModel.getInitials();
        if (initials.length() == 0)
            return;

        startActivity(new Intent(this, SandstormActivity.class));
    }
}