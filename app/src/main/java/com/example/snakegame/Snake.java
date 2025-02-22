package com.example.snakegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Snake {
    private boolean move_left, move_up, move_down, move_right;
    private Bitmap bm, bm_head_up, bm_head_down, bm_head_left, bm_head_right, bm_body_vertical, bm_body_horizontal, bm_body_top_right,
            bm_body_top_left, bm_body_bottom_right, bm_body_bottom_left, bm_tail_left, bm_tail_right, bm_tail_up, bm_tail_down;
    private int x, y, length;
    private ArrayList<PartSnake> arrPartSnake = new ArrayList<>();

    public Snake(Bitmap bm, int x, int y, int length) {
        this.bm = bm;
        this.x = x;
        this.y = y;
        this.length = length;


        bm_body_bottom_left = Bitmap.createBitmap(bm, 0, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_body_bottom_right = Bitmap.createBitmap(bm, GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_body_horizontal = Bitmap.createBitmap(bm, 2 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_body_top_left = Bitmap.createBitmap(bm, 3 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_body_top_right = Bitmap.createBitmap(bm, 4 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_body_vertical = Bitmap.createBitmap(bm, 5 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_head_down = Bitmap.createBitmap(bm, 6 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_head_left = Bitmap.createBitmap(bm, 7 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_head_right = Bitmap.createBitmap(bm, 8 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_head_up = Bitmap.createBitmap(bm, 9 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_tail_up = Bitmap.createBitmap(bm, 10 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_tail_right = Bitmap.createBitmap(bm, 11 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_tail_left = Bitmap.createBitmap(bm, 12 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);
        bm_tail_down = Bitmap.createBitmap(bm, 13 * GameView.sizeOfMap, 0, GameView.sizeOfMap, GameView.sizeOfMap);


        arrPartSnake.add(new PartSnake(bm_head_right, x, y));
        for (int i = 1; i < length - 1; i++)
            arrPartSnake.add(new PartSnake(bm_body_horizontal, arrPartSnake.get(i - 1).getX() - GameView.sizeOfMap, y));

        arrPartSnake.add(new PartSnake(bm_tail_right, arrPartSnake.get(length - 2).getX() - GameView.sizeOfMap, y));

        setMove_right(true);
    }
    //map starts at
    // X:Constants.SCREEN_WIDTH / 2 - (w / 2) * sizeOfMap
    // Y:100 * Constants.SCREEN_HEIGHT / 1920

    public boolean isAlive(int topBound, int leftBound, int bottomBound, int rightBound) {
        if (arrPartSnake.get(0).getX() < leftBound ||
                arrPartSnake.get(0).getY() < topBound ||
                arrPartSnake.get(0).getX() > rightBound ||
                arrPartSnake.get(0).getY() > bottomBound) {
            return false;
        }

        for (int i = 4; i < length; i++) {
            if (arrPartSnake.get(0).getrBody().intersect(arrPartSnake.get(i).getrBody())) {
                return false;
            }
        }

        return true;
    }

    public void update() {
        for (int i = length - 1; i > 0; i--) {
            arrPartSnake.get(i).setX(arrPartSnake.get(i - 1).getX());
            arrPartSnake.get(i).setY(arrPartSnake.get(i - 1).getY());
        }
        if (move_down) {
            arrPartSnake.get(0).setY(arrPartSnake.get(0).getY() + GameView.sizeOfMap);
            arrPartSnake.get(0).setBm(bm_head_down);
        } else if (move_right) {
            arrPartSnake.get(0).setX(arrPartSnake.get(0).getX() + GameView.sizeOfMap);
            arrPartSnake.get(0).setBm(bm_head_right);
        } else if (move_left) {
            arrPartSnake.get(0).setX(arrPartSnake.get(0).getX() - GameView.sizeOfMap);
            arrPartSnake.get(0).setBm(bm_head_left);
        } else if (move_up) {
            arrPartSnake.get(0).setY(arrPartSnake.get(0).getY() - GameView.sizeOfMap);
            arrPartSnake.get(0).setBm(bm_head_up);
        }

        for (int i = 1; i < length - 1; i++) {
            if (arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_bottom_left);
            } else if (arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_bottom_right);
            } else if (arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_top_left);
            } else if (arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_top_right);
            } else if (arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrTop().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrBottom().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_vertical);
            } else if (arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i + 1).getrBody())
                    && arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i - 1).getrBody())
                    || arrPartSnake.get(i).getrLeft().intersect(arrPartSnake.get(i - 1).getrBody())
                    && arrPartSnake.get(i).getrRight().intersect(arrPartSnake.get(i + 1).getrBody())) {
                arrPartSnake.get(i).setBm(bm_body_horizontal);
            }
        }
        if (arrPartSnake.get(length - 1).getrRight().intersect(arrPartSnake.get(length - 2).getrBody())) {
            arrPartSnake.get(length - 1).setBm(bm_tail_right);
        } else if (arrPartSnake.get(length - 1).getrLeft().intersect(arrPartSnake.get(length - 2).getrBody())) {
            arrPartSnake.get(length - 1).setBm(bm_tail_left);
        } else if (arrPartSnake.get(length - 1).getrTop().intersect(arrPartSnake.get(length - 2).getrBody())) {
            arrPartSnake.get(length - 1).setBm(bm_tail_up);
        } else if (arrPartSnake.get(length - 1).getrBottom().intersect(arrPartSnake.get(length - 2).getrBody())) {
            arrPartSnake.get(length - 1).setBm(bm_tail_down);
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < length; i++) {
            canvas.drawBitmap(arrPartSnake.get(i).getBm(), arrPartSnake.get(i).getX(), arrPartSnake.get(i).getY(), null);
        }
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBm_head_up() {
        return bm_head_up;
    }

    public void setBm_head_up(Bitmap bm_head_up) {
        this.bm_head_up = bm_head_up;
    }

    public Bitmap getBm_head_down() {
        return bm_head_down;
    }

    public void setBm_head_down(Bitmap bm_head_down) {
        this.bm_head_down = bm_head_down;
    }

    public Bitmap getBm_head_left() {
        return bm_head_left;
    }

    public void setBm_head_left(Bitmap bm_head_left) {
        this.bm_head_left = bm_head_left;
    }

    public Bitmap getBm_head_right() {
        return bm_head_right;
    }

    public void setBm_head_right(Bitmap bm_head_right) {
        this.bm_head_right = bm_head_right;
    }

    public Bitmap getBm_body_vertical() {
        return bm_body_vertical;
    }

    public void setBm_body_vertical(Bitmap bm_body_vertical) {
        this.bm_body_vertical = bm_body_vertical;
    }

    public Bitmap getBm_body_horizontal() {
        return bm_body_horizontal;
    }

    public void setBm_body_horizontal(Bitmap bm_body_horizontal) {
        this.bm_body_horizontal = bm_body_horizontal;
    }

    public Bitmap getBm_body_top_right() {
        return bm_body_top_right;
    }

    public void setBm_body_top_right(Bitmap bm_body_top_right) {
        this.bm_body_top_right = bm_body_top_right;
    }

    public Bitmap getBm_body_top_left() {
        return bm_body_top_left;
    }

    public void setBm_body_top_left(Bitmap bm_body_top_left) {
        this.bm_body_top_left = bm_body_top_left;
    }

    public Bitmap getBm_body_bottom_right() {
        return bm_body_bottom_right;
    }

    public void setBm_body_bottom_right(Bitmap bm_body_bottom_right) {
        this.bm_body_bottom_right = bm_body_bottom_right;
    }

    public Bitmap getBm_body_bottom_left() {
        return bm_body_bottom_left;
    }

    public void setBm_body_bottom_left(Bitmap bm_body_bottom_left) {
        this.bm_body_bottom_left = bm_body_bottom_left;
    }

    public Bitmap getBm_tail_left() {
        return bm_tail_left;
    }

    public void setBm_tail_left(Bitmap bm_tail_left) {
        this.bm_tail_left = bm_tail_left;
    }

    public Bitmap getBm_tail_right() {
        return bm_tail_right;
    }

    public void setBm_tail_right(Bitmap bm_tail_right) {
        this.bm_tail_right = bm_tail_right;
    }

    public Bitmap getBm_tail_up() {
        return bm_tail_up;
    }

    public void setBm_tail_up(Bitmap bm_tail_up) {
        this.bm_tail_up = bm_tail_up;
    }

    public Bitmap getBm_tail_down() {
        return bm_tail_down;
    }

    public void setBm_tail_down(Bitmap bm_tail_down) {
        this.bm_tail_down = bm_tail_down;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<PartSnake> getArrPartSnake() {
        return arrPartSnake;
    }

    public void setArrPartSnake(ArrayList<PartSnake> arrPartSnake) {
        this.arrPartSnake = arrPartSnake;
    }


    public boolean isMove_left() {
        return move_left;
    }

    public void setMove_left(boolean move_left) {
        s();
        this.move_left = move_left;
    }

    public boolean isMove_up() {
        return move_up;
    }

    public void setMove_up(boolean move_up) {
        s();
        this.move_up = move_up;
    }

    public boolean isMove_down() {
        return move_down;
    }

    public void setMove_down(boolean move_down) {
        s();
        this.move_down = move_down;
    }

    public boolean isMove_right() {
        return move_right;
    }

    public void setMove_right(boolean move_right) {
        s();
        this.move_right = move_right;
    }

    public void s() {
        this.move_down = this.move_left = this.move_right = this.move_up = false;
    }

    public void addPart() {
        PartSnake part = arrPartSnake.get(length - 1);
        length++;
        if (part.getBm() == bm_tail_right)
            arrPartSnake.add(new PartSnake(bm_tail_right, part.getX() - GameView.sizeOfMap, part.getY()));
        else if (part.getBm() == bm_tail_left)
            arrPartSnake.add(new PartSnake(bm_tail_right, part.getX() + GameView.sizeOfMap, part.getY()));
        else if (part.getBm() == bm_tail_down)
            arrPartSnake.add(new PartSnake(bm_tail_right, part.getX(), part.getY() - GameView.sizeOfMap));
        else if (part.getBm() == bm_tail_up)
            arrPartSnake.add(new PartSnake(bm_tail_right, part.getX(), part.getY() + GameView.sizeOfMap));

    }
}
