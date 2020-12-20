package com.example.pingpong;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

public class Game {
    final static int BALL_RADIUS = 70;
    final static int PADDLE_WIDTH = 400;
    final static int PADDLE_HEIGHT = 100;
    final static int ACCELERATION_FACTOR = 10;
    final static double SPEED_FACTOR = 1000;
    int firstPlayerOffset = 0;
    int secondPlayerOffset = 0;
    int firstPlayerScore = 0;
    int secondPlayerScore = 0;
    double acceleration[] = {ACCELERATION_FACTOR, ACCELERATION_FACTOR};
    double speed[] = {0.0, SPEED_FACTOR};
    double current_speed[] = {0.0, SPEED_FACTOR};
    double initial_position[] = {0.0, 0.0};
    Boolean isInitialised = false;
    long start, current;
    int width, height;

    public Game(){
        startMoving();
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

    void startMoving(){
        speed[0] = (-1.0 + 2 * Math.random())*SPEED_FACTOR;
        double upDownDesider = Math.random();
        if(upDownDesider >= 0.5){
            speed[1] = -SPEED_FACTOR;
        }
        check_acceleration();
        restartTime();
        //System.out.println("Initial speed is (" + String.valueOf(speed[0]) + ", " + String.valueOf(speed[1]) + ")");
        isInitialised = false;
    }

    void restartTime(){
        start = SystemClock.uptimeMillis();
    }

    int[] getPosition(){
        if(!isInitialised){
            initial_position[0] = width/2;
            initial_position[1] = height/2;
            isInitialised = true;
        }
        current = SystemClock.uptimeMillis();
        double time = (current - start)*0.001;
        //System.out.println("current time is" + String.valueOf(current));
        //System.out.println("start time is" + String.valueOf(start));
        //System.out.println("Time = " + String.valueOf(time));
        current_speed[0] = speed[0] + acceleration[0] * time;
        current_speed[1] = speed[1] + acceleration[1] * time;
        int x = (int)(initial_position[0] + speed[0]*time + acceleration[0]*time*time/2);
        int y = (int)(initial_position[1] + speed[1]*time + acceleration[1]*time*time/2);
        //System.out.println("Speed is (" + String.valueOf(speed[0]) + ", " + String.valueOf(speed[1]) + ")");

        if(y <= PADDLE_HEIGHT + BALL_RADIUS){
            y = PADDLE_HEIGHT + BALL_RADIUS;
            if(x >= width/2 - PADDLE_WIDTH/2 + firstPlayerOffset - BALL_RADIUS &&
                    x <= width/2 + PADDLE_WIDTH/2 + firstPlayerOffset + BALL_RADIUS){
                speed = current_speed;
                speed[1] *= -1.0;
                check_acceleration();
                initial_position[0] = x;
                initial_position[1] = y;
                restartTime();
            }
            else{
                secondPlayerScore += 1;
                startMoving();
            }
        }
        else if (y >= height - PADDLE_HEIGHT - BALL_RADIUS){
            y = height - PADDLE_HEIGHT - BALL_RADIUS;
            if(x >= width/2 - PADDLE_WIDTH/2 + secondPlayerOffset - BALL_RADIUS &&
                    x <= width/2 + PADDLE_WIDTH/2 + secondPlayerOffset + BALL_RADIUS){
                speed = current_speed;
                speed[1] *= -1.0;
                check_acceleration();
                initial_position[0] = x;
                initial_position[1] = y;
                restartTime();
            }
            else{
                firstPlayerScore += 1;
                startMoving();
            }
        }
        else if(x <= BALL_RADIUS){
            x = BALL_RADIUS;
            initial_position[0] = x;
            initial_position[1] = y;
            speed = current_speed;
            speed[0] *= -1.0;
            check_acceleration();
            restartTime();
        }
        else if(x >= width - BALL_RADIUS){
            x = width - BALL_RADIUS;
            initial_position[0] = x;
            initial_position[1] = y;
            speed = current_speed;
            speed[0] *= -1.0;
            check_acceleration();
            restartTime();
        }
        int[] result = {x, y};
        return result;
    }

}
