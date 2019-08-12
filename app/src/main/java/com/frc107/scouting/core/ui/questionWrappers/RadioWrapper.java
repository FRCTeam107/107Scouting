package com.frc107.scouting.core.ui.questionWrappers;

import android.app.Activity;
import android.widget.RadioGroup;

import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private ICallbackWithParam<Integer> setter;

    public RadioWrapper(Activity context, int id, ICallbackWithParam<Integer> setter) {
        radioGroup = context.findViewById(id);

        this.setter = setter;
        radioGroup.setOnCheckedChangeListener(createChangeListener());
    }

    private RadioGroup.OnCheckedChangeListener createChangeListener() {
        return (group, checkedId) -> setter.call(checkedId);
    }

    public RadioGroup getRadioGroup() {
        return radioGroup;
    }

    public void cleanUp() {
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup = null;
    }

    public void clear() {
        // todo: make sure this calls the listener
        radioGroup.clearCheck();
    }
}
