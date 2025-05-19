package com.itheima.topline.utils;

import android.view.View;

public class AnimHelper {
    private AnimHelper() {
        throw new RuntimeException("AnimHelper cannot be initialized!");
    }
    public static void setViewX(View view, float originalX, float finalX, float percent)
    {
        float calcX = (finalX - originalX) * percent + originalX;
        view.setX(calcX);
    }
    public static void setViewY(View view, float originalY, float finalY, float percent)
    {
        float calcY = (finalY - originalY) * percent + originalY;
        view.setY(calcY);
    }
    public static void scaleView(View view, float originalSize, float finalSize, float
            percent) {
        float calcSize = (finalSize - originalSize) * percent + originalSize;
        float caleScale = calcSize / originalSize;
        view.setScaleX(caleScale);
        view.setScaleY(caleScale);
    }
}
