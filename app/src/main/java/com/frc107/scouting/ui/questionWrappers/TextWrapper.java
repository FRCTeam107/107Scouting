package com.frc107.scouting.ui.questionWrappers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.frc107.scouting.form.ISetter;

public class TextWrapper {
    private EditText editText;
    private TextWatcher textWatcher;
    private ISetter<String> setter;

    public TextWrapper(EditText editText, ISetter<String> setter) {
        this.editText = editText;
        this.setter = setter;

        textWatcher = createTextWatcher();
        this.editText.addTextChangedListener(textWatcher);
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setter.set(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        };
    }

    public String getValue() {
        return editText.getText().toString();
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
