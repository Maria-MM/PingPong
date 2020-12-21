package com.example.pingpong;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

public class Game {
    final static float BALL_RADIUS = 0.05f; //in terms of width
    final static float PADDLE_WIDTH = 0.3f; //in terms of width
    final static float PADDLE_HEIGHT = 0.05f; //in terms of height
    final static float ACCELERATION_FACTOR = 0.0005f;
    final static float SPEED_FACTOR = 0.5f;
    float firstPlayerOffset = 0;
    float secondPlayerOffset = 0;
    int firstPlayerScore = 0;
    int secondPlayerScore = 0;
    float acceleration[] = {ACCELERATION_FACTOR, ACCELERATION_FACTOR};
    float speed[] = {0.0f, SPEED_FACTOR};
    float current_speed[] = {0.0f, SPEED_FACTOR};
    float initial_position[] = {0.5f, 0.5f};
    Boolean isInitialised = true, isStarted = false;
    float[] curBallPosition = {0.5f, 0.5f};
    long start, current;

    public Game(){

    }

    void check_acceleration(){
        if(speed[0] < 0){
            acceleration[0] = -ACCELERATION_FACTOR;
        }
        else{
            acceleration[0] = ACCELERATION_FACTOR;
        }
        if(speed[1] < 0){
            acceleration[1] = -ACCELERATION_FACTOR;
        }
        else{
            acceleration[1] = ACCELERATION_FACTOR;
        }
    }

    void updateTime(){
        check_acceleration();
        restartTime();
    }

    void startMoving(){
        speed[0] = 0.0f;

        if(firstPlayerScore >= secondPlayerScore){
            speed[1] = -SPEED_FACTOR;
        }
        else{
            speed[1] = SPEED_FACTOR;
        }
        check_acceleration();
        restartTime();
        //System.out.println("Initial speed is (" + String.valueOf(speed[0]) + ", " + String.valueOf(speed[1]) + ")");
        isInitialised = false;
    }

    void restartTime(){
        start = SystemClock.uptimeMillis();
    }
    float[] getPosition(){
        float[] result = {0.5f, 0.5f};
        if(!isStarted){
            return result;
        }
        if(!isInitialised){
            initial_position[0] = 0.5f;
            initial_position[1] = 0.5f;
            isInitialised = true;
        }
        current = SystemClock.uptimeMillis();
        double time = (current - start)*0.001;

        current_speed[0] = (float) (speed[0] + acceleration[0] * time);
        current_speed[1] = (float) (speed[1] + acceleration[1] * time);
        float x = (float) (initial_position[0] + speed[0]*time + acceleration[0]*time*time/2);
        float y = (float) (initial_position[1] + speed[1]*time + acceleration[1]*time*time/2);
        //System.out.println("Speed is (" + String.valueOf(speed[0]) + ", " + String.valueOf(speed[1]) + ")");

        if(y <= PADDLE_HEIGHT + BALL_RADIUS){
            y = PADDLE_HEIGHT + BALL_RADIUS;
            if(x >= 0.5f - PADDLE_WIDTH/2 + firstPlayerOffset - BALL_RADIUS &&
                    x <= 0.5f + PADDLE_WIDTH/2 + firstPlayerOffset + BALL_RADIUS){
                speed = current_speed;
                speed[1] *= -1.0f;
                check_acceleration();
                initial_position[0] = x;
                initial_position[1] = y;
                restartTime();
            }
            else{
                startMoving();
            }
        }
        else if (y >= 1.0f - PADDLE_HEIGHT - BALL_RADIUS){
            y = 1.0f - PADDLE_HEIGHT - BALL_RADIUS;
            if(x >= 0.5f - PADDLE_WIDTH/2 + secondPlayerOffset - BALL_RADIUS &&
                    x <= 0.5f + PADDLE_WIDTH/2 + secondPlayerOffset + BALL_RADIUS){
                speed = current_speed;
                speed[1] *= -1.0f;
                check_acceleration();
                initial_position[0] = x;
                initial_position[1] = y;
                restartTime();
            }
            else{
                startMoving();
            }
        }
        else if(x <= BALL_RADIUS){
            x = BALL_RADIUS;
            initial_position[0] = x;
            initial_position[1] = y;
            speed = current_speed;
            speed[0] *= -1.0f;
            check_acceleration();
            restartTime();
        }
        else if(x >= 1.0f - BALL_RADIUS){
            x = 1.0f - BALL_RADIUS;
            initial_position[0] = x;
            initial_position[1] = y;
            speed = current_speed;
            speed[0] *= -1.0f;
            check_acceleration();
            restartTime();
        }
        result = new float[]{x, y};
        return result;
    }

    float[] getBallPosition(){
        return curBallPosition;
    }

    float getMyOffset(){
        return secondPlayerOffset;
    }

    void updateGameState(String s){
        String[] tokens = s.split("\\|");
        System.out.println("tokens size is " + String.valueOf(tokens.length));
        for(String str : tokens){
            System.out.println(str);
        }
        int len = tokens.length;
        firstPlayerOffset = -Float.parseFloat(tokens[0]);
        initial_position[0] = 1.0f - Float.parseFloat(tokens[1]);
        initial_position[1] = 1.0f - Float.parseFloat(tokens[2]);
        if(len > 3)
        {
            float speedX = Float.parseFloat(tokens[3]);
            speed[0] = -speedX;
            current_speed[0] = -speedX;
        }
        if(len > 4){
            float speedY = Float.parseFloat(tokens[4]);
            speed[1] = -speedY;
            current_speed[1] = -speedY;
        }
        if(len > 5)
        {
            int new_score = Integer.parseInt(tokens[5]);
            if(new_score > secondPlayerScore){
                secondPlayerScore = new_score;
            }
        }
        if(len > 6){
            int new_score = Integer.parseInt(tokens[6]);
            if(new_score > firstPlayerScore){
                firstPlayerScore = new_score;
            }
        }

        updateTime();
    }

}
