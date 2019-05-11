package com.frc107.scouting.initials;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.scouting.R;
import com.frc107.scouting.sandstorm.SandstormActivity;
import com.frc107.scouting.ui.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class InitialsActivity extends BaseActivity {
    private InitialsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouter_initials);
        viewModel = new InitialsViewModel();

        TextInputEditText scouterInitialsEditText = findViewById(R.id.scouterInitials_input);
        scouterInitialsEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setInitials(s.toString());
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