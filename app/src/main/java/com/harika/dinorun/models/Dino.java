package com.harika.dinorun.models;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import com.harika.dinorun.R;
import com.harika.dinorun.utils.Constants;
import com.harika.dinorun.utils.ResourceManager;

public class Dino {

    private static final int DEFAULT_DINO_Y = Constants.SCREEN_HEIGHT - Constants.DINO_HEIGHT; // Update the default Y position
    private static final int GRAVITY = 2;
    private static final int JUMP_VELOCITY = -35;
    private Drawable dinoDrawable;
    private Rect boundingBox;
    private int x, y;
    private int width, height;
    private int velocityY;
    private boolean isJumping;

    public Dino(Context context, int x) {
        this.dinoDrawable = ResourceManager.getInstance(context).getDrawableFromResource(R.drawable.dino);
        this.x = x;
        this.y = DEFAULT_DINO_Y;
        this.width = dinoDrawable.getIntrinsicWidth();
        this.height = dinoDrawable.getIntrinsicHeight();
        this.boundingBox = new Rect(x, y, x + width, y + height);
        this.velocityY = 0;
        this.isJumping = false;
    }

    public void update() {
        velocityY += GRAVITY;
        y += velocityY;

        if (y > DEFAULT_DINO_Y) {
            y = DEFAULT_DINO_Y;
            velocityY = 0;
            isJumping = false;
        }

        boundingBox.left = x;
        boundingBox.right = x + width;
        boundingBox.top = y;
        boundingBox.bottom = y + height;
    }

    public void draw(Canvas canvas) {
        dinoDrawable.setBounds(x, y, x + width, y + height);
        dinoDrawable.draw(canvas);
    }

    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_VELOCITY;
            isJumping = true;
        }
    }

    public Rect getBoundingBox() {
        return new Rect(x+20, y+10, x + width - 20, y + height-10);
    }
}
