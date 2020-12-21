package com.example.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    Paint paint;
    Game game;
    int width;
    int height;
    public DrawView(Context context, Game game_) {
        super(context);
        paint = new Paint();
        game = game_;
    }
    int[] floatToInt(float[] initial){
        int[] result = {(int)(initial[0] * width), (int)(initial[1] * height)};
        return result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        // Narysowanie czarnego prostokąta jako tło aplikacji.
        paint.setARGB(255, 0, 0, 0);
        canvas.drawRect(0,0, width, height, paint);
        // draw ball
        paint.setARGB(255, 255, 255, 255);
        //System.out.println("position is (" + String.valueOf(game.getPosition()[0]) + ", " + String.valueOf(game.getPosition()[1]) +  ")");
        int[] ball_position = floatToInt(game.getPosition());
        canvas.drawCircle(ball_position[0], ball_position[1], (float) (game.BALL_RADIUS * width), paint);
        // draw paddle 1
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(width/2 - game.PADDLE_WIDTH * width/2 + game.firstPlayerOffset * width, 0,
                width/2 + game.PADDLE_WIDTH * width/2 + game.firstPlayerOffset * width, game.PADDLE_HEIGHT * height, paint);
        // draw paddle 2
        canvas.drawRect(width/2 - game.PADDLE_WIDTH * width/2 + game.secondPlayerOffset * width,
                height - game.PADDLE_HEIGHT * height, width/2 + game.PADDLE_WIDTH * width/2 + game.secondPlayerOffset * width, height, paint);
        // draw scores
        paint.setARGB(255, 200, 200, 200);
        paint.setTextSize(150);
        paint.setAlpha(128);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(game.firstPlayerScore), width/2, height/4, paint);
        canvas.drawText(String.valueOf(game.secondPlayerScore), width/2, 3*height/4, paint);
        this.invalidate();
    }
    public boolean onTouchEvent(MotionEvent event) {
        final int pointerCount = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                for (int p = 0; p < pointerCount; p++) {
                    float x = event.getX(p);
                    float y = event.getY(p);
                    /*if (y <= game.PADDLE_HEIGHT * height * 2 && y >= 0) {
                        game.firstPlayerOffset = Math.max(Math.min((int)(x - width/2),
                                (width - game.PADDLE_WIDTH * width)/2), - (width - game.PADDLE_WIDTH * width)/2) / width;
                    } else */if (y <= height && y >= height - game.PADDLE_HEIGHT * height * 2){
                        game.secondPlayerOffset = Math.max(Math.min((int)(x - width/2),
                                (width - game.PADDLE_WIDTH * width)/2), -(width - game.PADDLE_WIDTH * width)/2) / width;
                    }
                }
                break;
        }
        return true;
    }
}
