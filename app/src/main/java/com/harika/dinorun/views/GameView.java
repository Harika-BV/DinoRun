package com.harika.dinorun.views;

import static com.harika.dinorun.utils.Constants.EARTH_Y_POSITION;
import static com.harika.dinorun.utils.Constants.MAX_Y_POSITION;
import static com.harika.dinorun.utils.Constants.MIN_Y_POSITION;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.harika.dinorun.R;
import com.harika.dinorun.models.Cactus;
import com.harika.dinorun.models.Cloud;
import com.harika.dinorun.models.Dino;
import com.harika.dinorun.models.Earth;
import com.harika.dinorun.utils.CollisionDetector;
import com.harika.dinorun.utils.Constants;
import com.harika.dinorun.utils.ResourceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Earth earth;
    private Dino dino;
    private List<Cactus> cacti;
    private boolean isPaused;
    private boolean isJumping;

    private Paint scorePaint;
    private Paint highScorePaint;
    private int score;
    private int highScore;
    private boolean isGameOver;
    private Paint gameOverPaint;
    private Paint replayPaint;
    private List<Cloud> clouds;
    private int NUM_INITIAL_CLOUDS = 2;

    private float replayX, replayY;

    public GameView(Context context) {
        super(context);
        createClouds();
        createEarth();
        initScore();
        gameOverStatus();

        getHolder().addCallback(this);
        setFocusable(true);
    }

    private void createClouds() {
        clouds = new ArrayList<>();
        float y = getRandomYPosition();

        for (int i = 0; i < NUM_INITIAL_CLOUDS; i++) {
            Cloud cloud = new Cloud(getContext());
            float x = getNextCloudXPosition(i);
            cloud.setPosition(x, y);
            clouds.add(cloud);
        }
    }

    private void createEarth() {
        float earthX = 0f; // Set the X position of the Earth
        float earthY = EARTH_Y_POSITION; // Set the Y position of the Earth
        earth = new Earth(earthX, earthY, getResources().getColor(R.color.object));
    }

    private void initScore() {
        score = 0;
        highScore = 0;

        scorePaint = new Paint();
        scorePaint.setColor(getResources().getColor(R.color.object));
        scorePaint.setTextSize(48);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        highScorePaint = new Paint();
        highScorePaint.setColor(getResources().getColor(R.color.object));
        highScorePaint.setTextSize(36);
        highScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        highScorePaint.setAntiAlias(true);
    }

    private void gameOverStatus() {
        isGameOver = false;

        gameOverPaint = new Paint();
        gameOverPaint.setColor(getResources().getColor(R.color.object));
        gameOverPaint.setTextSize(64);
        gameOverPaint.setTypeface(Typeface.DEFAULT_BOLD);
        gameOverPaint.setAntiAlias(true);

        replayPaint = new Paint();
        replayPaint.setColor(getResources().getColor(R.color.object));
        replayPaint.setTextSize(48);
        replayPaint.setTypeface(Typeface.DEFAULT_BOLD);
        replayPaint.setAntiAlias(true);
    }

    private float getNextCloudXPosition(int index) {
        return (index + 1) * Constants.CLOUD_SPACING;
    }

    private float getRandomYPosition() {
        return (float) (Math.random() * (MAX_Y_POSITION - MIN_Y_POSITION) + MIN_Y_POSITION);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ResourceManager.getInstance(getContext()); // Initialize the resource manager

        dino = new Dino(getContext(), Constants.SCREEN_WIDTH / 4); // Adjust the dino's initial X position as needed
        cacti = new ArrayList<>();
        isPaused = false;

        gameThread = new GameThread(holder);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Not implemented
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {

            if (event.getX() >= replayX && event.getX() <= replayX + replayPaint.measureText("Tap to Replay") &&
                    event.getY() >= replayY - replayPaint.getTextSize() && event.getY() <= replayY) {
                restartGame();
                return true;
            }
        } else if (!isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isPaused) {
                if (!isJumping) {
                    dino.jump();
                    isJumping = true;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isJumping = false;
        }

        return true;
    }

    public void update() {
        if (!isPaused) {
            dino.update();

            // Update cacti and remove those that are offscreen or collided
            for (int i = cacti.size() - 1; i >= 0; i--) {
                Cactus cactus = cacti.get(i);
                cactus.update();
                if (cactus.getX() + cactus.getWidth() < 0) {
                    cacti.remove(i);
                } else if (CollisionDetector.isCollision(dino, cactus)) {
                    gameOver();
                }
            }

            // Add new cacti periodically
            if (cacti.isEmpty() || cacti.get(cacti.size() - 1).getX() < Constants.SCREEN_WIDTH - Constants.CACTUS_SPAWN_DISTANCE) {
                int randomHeight = (int) (Math.random() * (Constants.CACTUS_MAX_HEIGHT - Constants.CACTUS_MIN_HEIGHT + 1) + Constants.CACTUS_MIN_HEIGHT);
                cacti.add(new Cactus(getContext(), Constants.SCREEN_WIDTH, randomHeight));
            }
            updateScore();
        }
    }

    private void updateScore() {
        score++; // Increment the score by 1
        if (score > highScore) {
            highScore = score; // Update the high score
        }
    }
    private void drawClouds(Canvas canvas) {
        for (Cloud cloud : clouds) {
            if(!isPaused)
                cloud.update();
            cloud.draw(canvas);
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(getResources().getColor(R.color.background));
            if (isPaused) {
                // Draw bounding boxes for debugging
                if (Constants.DEBUG_MODE) {
                    Paint debugPaint = new Paint();
                    debugPaint.setColor(Color.RED);
                    debugPaint.setStyle(Paint.Style.STROKE);
                    debugPaint.setStrokeWidth(2f);

                    Rect dinoRect = dino.getBoundingBox();
                    canvas.drawRect(dinoRect, debugPaint);

                    for (Cactus cactus : cacti) {
                        Rect cactusRect = cactus.getBoundingBox();
                        canvas.drawRect(cactusRect, debugPaint);
                    }
                }
            }

            drawClouds(canvas);
            dino.draw(canvas);
            for (Cactus cactus : cacti) {
                cactus.draw(canvas);
            }
            earth.draw(canvas);


            // Draw the score and high score on the canvas
            String scoreText = "Score: " + score;
            String highScoreText = "High Score: " + highScore;

            float scoreX = getWidth() - scorePaint.measureText(scoreText) - 100;
            float scoreY = 100;
            canvas.drawText(scoreText, scoreX, scoreY, scorePaint);

            float highScoreX = getWidth() - highScorePaint.measureText(highScoreText) - 100;
            float highScoreY = scoreY + highScorePaint.getTextSize() + 16;
            canvas.drawText(highScoreText, highScoreX, highScoreY, highScorePaint);

            if (isGameOver) {
                String gameOverText = "Game Over";
                String replayText = "Tap to Replay";

                float gameOverX = (getWidth() - gameOverPaint.measureText(gameOverText)) / 2;
                float gameOverY = getHeight() / 2 - gameOverPaint.getTextSize() - getHeight()/4;

                replayX = (getWidth() - replayPaint.measureText(replayText)) / 2;
                replayY = getHeight() / 2 + replayPaint.getTextSize() - getHeight()/4;

                canvas.drawText(gameOverText, gameOverX, gameOverY, gameOverPaint);
                canvas.drawText(replayText, replayX, replayY, replayPaint);
            }
        }
    }

    private void restartGame() {
        isGameOver = false;
        isPaused = false;
        score = 0;

        // Remove the collided cactus
        if (cacti != null && cacti.size() > 0) {
            Iterator<Cactus> iterator = cacti.iterator();
            while (iterator.hasNext()) {
                Cactus cactus = iterator.next();
                if (CollisionDetector.isCollision(dino, cactus)) {
                    iterator.remove();
                }
            }
        }
    }


    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isGameOver = false;
        isPaused = false;
    }

    private void gameOver() {
        isGameOver = true;
        pauseGame();
    }

    private class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isRunning;

        public GameThread(SurfaceHolder holder) {
            this.surfaceHolder = holder;
            this.isRunning = false;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        update();
                        draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                // Sleep for a short duration to control the game speed
                try {
                    Thread.sleep(Constants.GAME_SPEED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
