package com.harika.dinorun.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.harika.dinorun.R;
import com.harika.dinorun.utils.Constants;
import com.harika.dinorun.utils.ResourceManager;

public class Cloud {
    private Drawable cloudDrawable;
    private float x;
    private float y;
    private int width;
    private int height;
    private float velocityX = Constants.CLOUD_VELOCITY;

    public Cloud(Context context) {
        cloudDrawable = ResourceManager.getInstance(context).getDrawableFromResource(R.drawable.cloud);
        width = cloudDrawable.getIntrinsicWidth();
        height = cloudDrawable.getIntrinsicHeight();
    }

    public void update() {
        x += velocityX;
        if (x < -cloudDrawable.getIntrinsicWidth()) {
            x = Constants.SCREEN_WIDTH;
        }
    }

    public void draw(Canvas canvas) {
        cloudDrawable.setBounds((int) x, (int) y, (int) (x + width), (int) (y + height));
        cloudDrawable.draw(canvas);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
