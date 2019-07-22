package com.frc107.scouting.ui.questionWrappers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.frc107.scouting.form.IQuestionAnswerer;

public class TextWrapper {
    private EditText editText;
    private TextWatcher textWatcher;
    private IQuestionAnswerer viewModel;

    public TextWrapper(EditText editText, IQuestionAnswerer viewModel) {
        this.editText = editText;
        this.viewModel = viewModel;

        textWatcher = createTextWatcher();
        this.editText.addTextChangedListener(textWatcher);
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setAnswer(editText.getId(), s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        };
    }

    public EditText getEditText() {
        return editText;
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
