package com.harika.dinorun.utils;

import android.graphics.Rect;
import com.harika.dinorun.models.Cactus;
import com.harika.dinorun.models.Dino;

public class CollisionDetector {

    public static boolean isCollision(Dino dino, Cactus cactus) {
        Rect dinoRect = dino.getBoundingBox();
        Rect cactusRect = cactus.getBoundingBox();

        return Rect.intersects(dinoRect, cactusRect);
    }
}





