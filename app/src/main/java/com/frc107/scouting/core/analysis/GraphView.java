package com.frc107.scouting.core.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

    public void setSource(Map<Integer, Integer> dataSource, String nameOfXAxis, String nameOfYAxis) {
        this.nameOfXAxis = nameOfXAxis;
        this.nameOfYAxis = nameOfYAxis;

        actualWidth = getWidth() - X_PADDING;
        actualHeight = getHeight() - Y_PADDING;
        
        PAINT.setTextSize(TEXT_SIZE);
        PAINT.setColor(Color.BLACK);
        
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        
        Set<Integer> xValues = dataSource.keySet();
        for (int x : xValues) {
            int y = dataSource.get(x);

            if (x < minX)
                minX = x;

            if (x > maxX)
                maxX = x;
            
            if (y < minY)
                minY = y;

            if (y > maxY)
                maxY = y;
        }
        
        points.clear();
        for (int x : xValues) {
            int y = dataSource.get(x);
            
            int newX = transformValueToRange(x, minX, maxX, X_PADDING, actualWidth - X_PADDING);
            int newY = transformValueToRange(y, minY, maxY, Y_PADDING, actualHeight - Y_PADDING);
            points.add(new Point(newX, newY));
        }

        invalidate();
    }

    private int transformValueToRange(int value, int oldMin, int oldMax, int newMin, int newMax) {
        int newRange = newMax - newMin;
        int oldRange = oldMax - oldMin;
        int valueRatio = (value - oldMin) / oldRange;
        int newValue = newMin + (valueRatio * newRange);
        return newValue;
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
        Rect bounds = new Rect();
        PAINT.getTextBounds(nameOfYAxis, 0, nameOfYAxis.length(), bounds);

        canvas.save();
        canvas.rotate(-90, X_PADDING - distFromYAxis, actualHeight);
        canvas.drawText(nameOfYAxis, X_PADDING - distFromYAxis, actualHeight, PAINT);
        canvas.restore();

        // draw lines
        Point previousPoint = null;
        for (Point point : points) {
            if (previousPoint != null)
                canvas.drawLine(previousPoint.x, previousPoint.y, point.x, point.y, PAINT);  
            
            previousPoint = point;
        }
    }
}
