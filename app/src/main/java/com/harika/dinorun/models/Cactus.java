package com.harika.dinorun.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.harika.dinorun.R;
import com.harika.dinorun.utils.Constants;
import com.harika.dinorun.utils.ResourceManager;

public class Cactus {

    private static final int CACTUS_DEFAULT_Y = Constants.SCREEN_HEIGHT - Constants.CACTUS_HEIGHT;

    private Drawable cactusDrawable;
    private Rect boundingBox;
    private int x, y;
    private int width, height;
    private int velocityX;

    public Cactus(Context context, int x, int height) {
        this.cactusDrawable = ResourceManager.getInstance(context).getDrawableFromResource(R.drawable.cactus);
        this.x = x;
        this.y = CACTUS_DEFAULT_Y;
        this.width = cactusDrawable.getIntrinsicWidth();
        this.height = cactusDrawable.getIntrinsicHeight();
        this.boundingBox = new Rect(x, y, x + width, y + height);
        this.velocityX = Constants.CACTUS_SPEED;
    }

    public void update() {
        x -= Constants.CACTUS_SPEED; // Adjust the cactus speed as needed
        boundingBox.left = x;
        boundingBox.right = x + width;
    }

    public void draw(Canvas canvas) {
        cactusDrawable.setBounds(x, y, x + width, y + height);
        cactusDrawable.draw(canvas);
    }

    public Rect getBoundingBox() {
        return new Rect(x+20, y, x + width-20, y + height);
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }
}
