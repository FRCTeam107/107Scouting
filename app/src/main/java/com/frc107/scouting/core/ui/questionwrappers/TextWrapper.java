package com.frc107.scouting.core.ui.questionwrappers;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

public class TextWrapper {
    private EditText editText;
    private TextWatcher textWatcher;
    private ICallbackWithParam<String> setter;

    /**
     * Create a wrapper for an EditText that helps make some of the listener stuff easier.
     * @param context The current context.
     * @param id The EditTexts view id.
     * @param setter A callback to run when the value changes.
     */
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // We don't care.
            }
            public void afterTextChanged(Editable s) {
                // We don't care.
            }
        };
    }

    public String getValue() {
        return editText.getText().toString();
    }

    public EditText getEditText() {
        return editText;
    }

    /**
     * Call this in onDestroy.
     */
    public void cleanUp() {
        editText.removeTextChangedListener(textWatcher);
        editText = null;
        textWatcher = null;
    }

    public void clear() {
        editText.setText("");
    }
}
