package com.frc107.scouting.core.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphView extends View {
    private List<Point> points = new ArrayList<>();

    private String nameOfXAxis = "X axis";
    private String nameOfYAxis = "Y axis";

    private int actualHeight;
    private int actualWidth;

    private static final int TEXT_SIZE = 40;
    private static final int X_PADDING = 80;
    private static final int Y_PADDING = 80;
    private static final Paint PAINT = new Paint();

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSource(int[] xValues, int[] yValues, String nameOfXAxis, String nameOfYAxis) {
        if (xValues.length != yValues.length)
            throw new IllegalArgumentException("xValues and yValues must have same length!");

        this.nameOfXAxis = nameOfXAxis;
        this.nameOfYAxis = nameOfYAxis;

        actualWidth = getWidth() - X_PADDING;
        actualHeight = getHeight() - Y_PADDING;
        
        PAINT.setTextSize(TEXT_SIZE);
        PAINT.setColor(Color.BLACK);
        
        int minX = -1;
        int minY = -1;
        int maxX = -1;
        int maxY = -1;
        
        for (int i = 0; i < xValues.length; i++) {
            int x = xValues[i];
            int y = yValues[i];

            if (x < minX || minX == -1)
                minX = x;

            if (x > maxX)
                maxX = x;
            
            if (y < minY || minY == -1)
                minY = y;

            if (y > maxY)
                maxY = y;
        }
        
        points.clear();

        float actualMaxX = actualWidth;
        float actualMaxY = actualHeight - Y_PADDING;
        for (int i = 0; i < xValues.length; i++) {
            int x = xValues[i];
            int y = yValues[i];

            float newX = transformValueToRange(x, minX, maxX, X_PADDING, actualMaxX);
            float newY = Y_PADDING + actualMaxY - transformValueToRange(y, minY, maxY, Y_PADDING, actualMaxY);
            String label = "(" + x + "," + y + ")";
            points.add(new Point(newX, newY, label));
        }

        invalidate();
    }

    private float transformValueToRange(float value, float oldMin, float oldMax, float newMin, float newMax) {
        float newRange = newMax - newMin;
        float oldRange = oldMax - oldMin;
        float oldValueMinDiff = value - oldMin;
        float valueRatio = oldValueMinDiff / oldRange;
        return newMin + (valueRatio * newRange);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = X_PADDING;
        int startY = Y_PADDING;

        // draw the horizontal and vertical axes
        canvas.drawLine(startX, startY, startX, actualHeight, PAINT);
        canvas.drawLine(startX, actualHeight, actualWidth, actualHeight, PAINT);

        // draw labels
        canvas.drawText(nameOfXAxis, X_PADDING, actualHeight + TEXT_SIZE, PAINT);

        int distFromYAxis = 15;
        Rect textBounds = new Rect();
        PAINT.getTextBounds(nameOfYAxis, 0, nameOfYAxis.length(), textBounds);

        canvas.save();
        canvas.rotate(-90, X_PADDING - distFromYAxis, actualHeight);
        canvas.drawText(nameOfYAxis, X_PADDING - distFromYAxis, actualHeight, PAINT);
        canvas.restore();

        // draw lines
        Rect previousTextBounds = null;
        Point previousPoint = null;
        for (Point point : points) {
            if (previousPoint != null)
                canvas.drawLine(previousPoint.x, previousPoint.y, point.x, point.y, PAINT);

            PAINT.getTextBounds(point.label, 0, point.label.length(), textBounds);
            float textOffset = 0;
            if (previousTextBounds != null) {
                float distBetweenCenters = point.x - previousPoint.x;
                float minimumDist = (textBounds.width() / 2f) + (previousTextBounds.width() / 2f);
                if (distBetweenCenters < minimumDist) {
                    textOffset = minimumDist - distBetweenCenters;
                }
            }

            canvas.drawText(point.label, point.x - textBounds.exactCenterX() + textOffset, point.y - textBounds.height(), PAINT);

            previousTextBounds = textBounds;
            previousPoint = point;
        }
    }

    class Point {
        float x;
        float y;
        String label;
        Point(float x, float y, String label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }
    }
}
