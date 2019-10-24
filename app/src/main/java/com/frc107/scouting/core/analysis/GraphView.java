package com.frc107.scouting.core.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

public class GraphView extends View {
    private Map<Integer, Integer> dataSource;
    private String nameOfXAxis = "X axis";
    private String nameOfYAxis = "Y axis";

    private int min;
    private int max;

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSource(Map<Integer, Integer> dataSource, String nameOfXAxis, String nameOfYAxis) {
        this.dataSource = dataSource;

        int min = 0;
        int max = 0;
        for (int value : dataSource.values()) {
            if (value < min)
                min = value;

            if (value > max)
                max = value;
        }

        this.nameOfXAxis = nameOfXAxis;
        this.nameOfYAxis = nameOfYAxis;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int textSize = 40;
        int xPadding = 80;
        int yPadding = 80;
        int startX = xPadding;
        int startY = yPadding;
        int width = getWidth() - xPadding;
        int height = getHeight() - yPadding;

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);

        // draw the horizontal and vertical axes
        canvas.drawLine(startX, startY, startX, height, paint);
        canvas.drawLine(startX, height, width, height, paint);

        // draw labels
        canvas.drawText(nameOfXAxis, xPadding, height + textSize, paint);

        int distFromYAxis = 15;
        Rect bounds = new Rect();
        paint.getTextBounds(nameOfYAxis, 0, nameOfYAxis.length(), bounds);

        canvas.save();
        canvas.rotate(-90, xPadding - distFromYAxis, height);
        canvas.drawText(nameOfYAxis, xPadding - distFromYAxis, height, paint);
        canvas.restore();

        // test line
        canvas.drawLine(startX, startY, width, height, paint);
    }
}
