package com.via.adits.FunctionalUses;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.via.adits.R;

public class AnimationsClass {

    public void rotateClockwise (ImageView a, Context c){
        Animation rotateClockwise = AnimationUtils.loadAnimation(c, R.anim.rotate_clockwise);
        rotateClockwise.setDuration(2500);
    }

}
