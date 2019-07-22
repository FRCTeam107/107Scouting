package com.frc107.scouting.ui.questionWrappers;

import android.widget.RadioGroup;

import com.frc107.scouting.form.IQuestionAnswerer;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private IQuestionAnswerer viewModel;

    public RadioWrapper(RadioGroup radioGroup, IQuestionAnswerer viewModel) {
        this.viewModel = viewModel;
        this.radioGroup = radioGroup;
        this.radioGroup.setOnCheckedChangeListener(createChangeListener());
    }

    private RadioGroup.OnCheckedChangeListener createChangeListener() {
        return (group, checkedId) -> viewModel.setAnswer(group.getId(), checkedId);
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
