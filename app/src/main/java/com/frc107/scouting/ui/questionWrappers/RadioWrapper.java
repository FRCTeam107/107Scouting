package com.frc107.scouting.ui.questionWrappers;

import android.widget.RadioGroup;

import com.frc107.scouting.form.IFieldSetter;
import com.frc107.scouting.form.IFormViewModel;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private IFormViewModel viewModel;

    public RadioWrapper(RadioGroup radioGroup, IFormViewModel viewModel) {
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
