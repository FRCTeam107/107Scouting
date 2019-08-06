package com.frc107.scouting.ui.questionWrappers;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.frc107.scouting.callbacks.ICallbackWithParam;

public class TextWrapper {
    private EditText editText;
    private TextWatcher textWatcher;
    private ICallbackWithParam<String> setter;

    public TextWrapper(Activity context, int id, ICallbackWithParam<String> setter) {
        editText = context.findViewById(id);

        this.setter = setter;
        textWatcher = createTextWatcher();
        editText.addTextChangedListener(textWatcher);
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setter.call(s.toString());
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
        // todo: make sure this calls the listener
        editText.setText("");
    }
}
