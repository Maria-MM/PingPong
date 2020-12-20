package com.example.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    Paint paint;
    Game game;
    public DrawView(Context context) {
        super(context);
        paint = new Paint();
        game = new Game();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        game.width = canvas.getWidth();
        game.height = canvas.getHeight();
        System.out.println("Height: " + game.height);
        // Narysowanie czarnego prostokąta jako tło aplikacji.
        paint.setARGB(255, 0, 0, 0);
        canvas.drawRect(0,0, game.width, game.height, paint);
        // draw ball
        paint.setARGB(255, 255, 255, 255);
        int[] ball_position = game.getPosition();
        canvas.drawCircle(ball_position[0], ball_position[1], game.BALL_RADIUS, paint);
        // draw paddle 1
        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(game.width/2 - game.PADDLE_WIDTH/2 + game.firstPlayerOffset, 0,
                game.width/2 + game.PADDLE_WIDTH/2 + game.firstPlayerOffset, game.PADDLE_HEIGHT, paint);
        // draw paddle 2
        canvas.drawRect(game.width/2 - game.PADDLE_WIDTH/2 + game.secondPlayerOffset,
                game.height - game.PADDLE_HEIGHT, game.width/2 + game.PADDLE_WIDTH/2 + game.secondPlayerOffset, game.height, paint);
        // draw scores
        paint.setARGB(255, 200, 200, 200);
        paint.setTextSize(150);
        paint.setAlpha(128);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(game.firstPlayerScore), game.width/2, game.height/4, paint);
        canvas.drawText(String.valueOf(game.secondPlayerScore), game.width/2, 3*game.height/4, paint);
        this.invalidate();
    }
    public boolean onTouchEvent(MotionEvent event) {
        final int pointerCount = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                for (int p = 0; p < pointerCount; p++) {
                    float x = event.getX(p);
                    float y = event.getY(p);
                    if (y <= game.PADDLE_HEIGHT*2 && y >= 0) {
                        game.firstPlayerOffset = Math.max(Math.min((int)(x - game.width/2),
                                (game.width - game.PADDLE_WIDTH)/2), -(game.width - game.PADDLE_WIDTH)/2);
                    } else if (y <= game.height && y >= game.height - game.PADDLE_HEIGHT*2){
                        game.secondPlayerOffset = Math.max(Math.min((int)(x - game.width/2),
                                (game.width - game.PADDLE_WIDTH)/2), -(game.width - game.PADDLE_WIDTH)/2);
                    }
                }
                break;
        }
        return true;
    }
}
