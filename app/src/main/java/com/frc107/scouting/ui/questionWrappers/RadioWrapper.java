package com.frc107.scouting.ui.questionWrappers;

import android.util.SparseIntArray;
import android.widget.RadioGroup;

import com.frc107.scouting.form.ISetter;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private ISetter<Integer> setter;
    private SparseIntArray buttonAnswerMappings;

    public RadioWrapper(RadioGroup radioGroup, SparseIntArray buttonAnswerMappings, ISetter<Integer> setter) {
        this.buttonAnswerMappings = buttonAnswerMappings;
        this.setter = setter;
        this.radioGroup = radioGroup;
        this.radioGroup.setOnCheckedChangeListener(createChangeListener());
    }

    private RadioGroup.OnCheckedChangeListener createChangeListener() {
        return (group, checkedId) -> {
            int value = buttonAnswerMappings.get(checkedId);
            setter.set(value);
        };
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
