package com.via.adits.FunctionalUses;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*------------------------------This class has been created for controlling the swipe actions on the application screens--------------------*/
public class OnSwipeTouchListener implements OnTouchListener {

    /*-------------------------Defining the global variable for using in the processes of this class---------------------------------------*/
    private final GestureDetector gestureDetector;

    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    /*--------------------------This function controls if there is a touch on the screen or not----------------------------------------------*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /*----------------------This function controls the swipe direction and returns it-----------------------------------------------------*/
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        }
                        if(diffX < 0) {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    /*----------------------------------This is a constructor function to define what will happen when swiping Right----------------------------------*/
    public void onSwipeRight() {
    }

    /*----------------------------------This is a constructor function to define what will happen when swiping Left----------------------------------*/
    public void onSwipeLeft() {
    }

    /*----------------------------------This is a constructor function to define what will happen when swiping Up----------------------------------*/
    public void onSwipeTop() {
    }

    /*----------------------------------This is a constructor function to define what will happen when swiping Down----------------------------------*/
    public void onSwipeBottom() {
    }
}