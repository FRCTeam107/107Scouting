package com.frc107.scouting.ui.questionWrappers;

import android.widget.RadioGroup;

import com.frc107.scouting.form.ISetter;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private ISetter<Integer> setter;

    public RadioWrapper(RadioGroup radioGroup, ISetter<Integer> setter) {
        this.setter = setter;
        this.radioGroup = radioGroup;
        this.radioGroup.setOnCheckedChangeListener(createChangeListener());
    }

    private RadioGroup.OnCheckedChangeListener createChangeListener() {
        return (group, checkedId) -> setter.set(checkedId);
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
