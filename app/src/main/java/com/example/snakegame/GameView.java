package com.example.snakegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    private Bitmap bmGrass1, bmGrass2, bmSnake, bmApple;
    public static int sizeOfMap = 80 * Constants.SCREEN_WIDTH / 1080, h = 21, w = 12;
    private ArrayList<Grass> arrGrass = new ArrayList<>();
    private Snake snake;
    private Apple apple;
    private int redrawTime;
    private VariableChangeListener changeListener;
    private int score;
    private boolean move = false;
    private float mx, my;
    private Handler handler;
    private Runnable r;
    private boolean isGameOver = false;

    public void setChangeListener(VariableChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        score = 0;
        redrawTime = 230;
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeOfMap, sizeOfMap, true);

        bmGrass2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass03);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeOfMap, sizeOfMap, true);

        bmSnake = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake1);
        bmSnake = Bitmap.createScaledBitmap(bmSnake, 14 * sizeOfMap, sizeOfMap, true);

        bmApple = BitmapFactory.decodeResource(this.getResources(), R.drawable.apple);
        bmApple = Bitmap.createScaledBitmap(bmApple, sizeOfMap, sizeOfMap, true);

        // Playable map starts at
        // X:Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap
        // Y:100 * Constants.SCREEN_HEIGHT / 1920

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Bitmap grassToAdd;
                if ((i + j) % 2 == 0) {
                    grassToAdd = bmGrass1;
                } else grassToAdd = bmGrass2;

                arrGrass.add(new Grass(grassToAdd, j * sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap,
                        i * sizeOfMap + 100 * Constants.SCREEN_HEIGHT / 1920, sizeOfMap, sizeOfMap));
            }
        }

        snake = new Snake(bmSnake, arrGrass.get(126).getX(), arrGrass.get(126).getY(), 4);
        int[] pos = randomApple();
        apple = new Apple(bmApple, arrGrass.get(pos[0]).getX(), arrGrass.get(pos[1]).getY());
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    public void setSnakeDirection(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            if (x < -1.5f && !snake.isMove_left()) {
                snake.setMove_right(true);
            } else if (x > 1.5f && !snake.isMove_right()) {
                snake.setMove_left(true);
            }
        } else {
            if (y < -1.5f && !snake.isMove_down()) {
                snake.setMove_up(true);
            } else if (y > 2.5f && !snake.isMove_up()) {
                snake.setMove_down(true);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a = event.getActionMasked();
        switch (a) {
            case MotionEvent.ACTION_MOVE: {
                if (!move) {
                    mx = event.getX();
                    my = event.getY();
                    move = true;
                } else {
                    if (mx - event.getX() > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMove_right()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMove_left(true);
                    } else if (event.getX() - mx > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMove_left()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMove_right(true);
                    } else if (event.getY() - my > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMove_up()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMove_down(true);
                    } else if (my - event.getY() > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMove_down()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMove_up(true);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mx = my = 0;
                move = false;
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawColor(0xFF1A6100);
        for (int i = 0; i < arrGrass.size(); i++) {
            canvas.drawBitmap(arrGrass.get(i).getBm(), arrGrass.get(i).getX(), arrGrass.get(i).getY(), null);
        }
        if (!isGameOver && snake.isAlive(arrGrass.get(0).getY(),
                arrGrass.get(0).getX(),
                arrGrass.get(arrGrass.size() - 1).getY(),
                arrGrass.get(arrGrass.size() - 1).getX())) {
            snake.update();
            snake.draw(canvas);
            checkEatApple();
        } else if (!isGameOver) {
             lose();
        }

        apple.draw(canvas);
        if (!isGameOver) {
            handler.postDelayed(r, redrawTime);
        }
    }

    private void lose() {
        isGameOver = true;
        Intent intent = new Intent(getContext(), LostGameActivity.class);
        intent.putExtra("score", "X " + String.valueOf(score));
        getContext().startActivity(intent);
    }

    public void resetGame() {
        // Reset the game state
        isGameOver = false;

        // Reinitialize the snake
        snake = new Snake(bmSnake, arrGrass.get(126).getX(), arrGrass.get(126).getY(), 4);

        // Reinitialize the apple
        int[] pos = randomApple();
        apple = new Apple(bmApple, arrGrass.get(pos[0]).getX(), arrGrass.get(pos[1]).getY());

        // Invalidate to trigger a redraw
        invalidate();
    }

    private void checkEatApple() {
        if (snake.getArrPartSnake().get(0).getrBody().intersect(apple.getR())) {
            int[] pos = randomApple();
            apple.reset(arrGrass.get(pos[0]).getX(), arrGrass.get(pos[1]).getY());
            snake.addPart();
            score++;
            if (score % 2 == 0 && score != 0 && redrawTime > 130)
                redrawTime = redrawTime - 20;
            changeListener.notifyValueChanged(score);
        }
    }

    public int[] randomApple() {
        int[] xy = new int[2];
        Random r = new Random();
        xy[0] = r.nextInt(arrGrass.size() - 1);
        xy[1] = r.nextInt(arrGrass.size() - 1);

        Rect rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(),
                arrGrass.get(xy[0]).getX() + sizeOfMap, arrGrass.get(xy[1]).getY() + sizeOfMap);

        boolean check = true;

        while (check) {
            check = false;
            for (int i = 0; i < snake.getArrPartSnake().size(); i++)
                if (rect.intersect(snake.getArrPartSnake().get(i).getrBody())) {
                    check = true;
                    xy[0] = r.nextInt(arrGrass.size() - 1);
                    xy[1] = r.nextInt(arrGrass.size() - 1);
                    rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(),
                            arrGrass.get(xy[0]).getX() + sizeOfMap, arrGrass.get(xy[1]).getY() + sizeOfMap);
                }
        }
        return xy;
    }

}
