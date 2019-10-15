package com.frc107.scouting.core.ui.questionwrappers;

import android.app.Activity;
import android.widget.RadioGroup;

import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

public class RadioWrapper {
    private RadioGroup radioGroup;
    private ICallbackWithParam<Integer> setter;

    /**
     * Creates a wrapper for a RadioGroup that helps make some of the listener stuff easier.
     * @param context The current context.
     * @param id The RadioGroups view id.
     * @param setter A callback to run when the value changes.
     */
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

    /**
     * Call this in onDestroy.
     */
    public void cleanUp() {
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup = null;
    }

    public void clear() {
        radioGroup.clearCheck();
    }
}
