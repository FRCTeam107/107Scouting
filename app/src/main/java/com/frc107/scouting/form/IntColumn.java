package com.frc107.scouting.form;

import java.util.List;

public class IntColumn extends Column<Integer> {
    public IntColumn(int id, String name) {
        super(id, name, Integer.class);
    }

    /*public double getAverageValue() {
        List<Integer> values = getValues();
        double count = values.size();
        double sum = 0;
        for (Integer i : values)
            sum += i;

        return sum / count;
    }*/
}
