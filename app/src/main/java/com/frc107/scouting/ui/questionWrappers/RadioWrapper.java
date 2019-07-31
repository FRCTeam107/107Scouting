package com.frc107.scouting.ui.questionWrappers;

import android.widget.RadioGroup;

import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.callbacks.ICallbackWithParam;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private ICallbackWithParam<Integer> setter;

    public RadioWrapper(RadioGroup radioGroup, ICallbackWithParam<Integer> setter) {
        this.setter = setter;
        this.radioGroup = radioGroup;
        this.radioGroup.setOnCheckedChangeListener(createChangeListener());
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
        radioGroup.clearCheck();
    }
}
