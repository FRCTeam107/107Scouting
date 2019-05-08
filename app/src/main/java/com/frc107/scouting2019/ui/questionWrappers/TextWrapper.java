package com.frc107.scouting2019.ui.questionWrappers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.frc107.scouting2019.viewmodel.BaseViewModel;

public class TextWrapper {
    private EditText editText;
    private TextWatcher textWatcher;
    private BaseViewModel viewModel;
    private int id;

    public TextWrapper(EditText editText, BaseViewModel viewModel) {
        this.viewModel = viewModel;
        this.editText = editText;
        this.id = editText.getId();

        textWatcher = createTextWatcher();
        this.editText.addTextChangedListener(textWatcher);
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleText(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        };
    }

    public EditText getEditText() {
        return editText;
    }

    public void handleText(String text) {
        if (text == null)
            text = "";

        viewModel.setAnswer(id, text);
    }

    public void cleanUp() {
        editText.removeTextChangedListener(textWatcher);
        editText = null;
        textWatcher = null;
    }

    public void clear() {
        editText.setText("");
    }
}
