package com.frc107.scouting2019.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.frc107.scouting2019.R;
import com.frc107.scouting2019.viewmodel.InitialsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ScouterInitialsActivity extends BaseActivity {
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