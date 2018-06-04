package sgtmelon.handynotes.service;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.InfoPageReply;

public class InfoPageEmpty implements Animation.AnimationListener{

    private LinearLayout notificationEmpty;
    private Animation alphaOut, alphaIn;

    public InfoPageEmpty(Context context, LinearLayout notificationEmpty) {
        this.notificationEmpty = notificationEmpty;

        alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);
        alphaOut.setAnimationListener(this);
        alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
        alphaIn.setAnimationListener(this);
    }

    private InfoPageReply infoPageReply;

    public void setInfoPageReply(InfoPageReply infoPageReply) {
        this.infoPageReply = infoPageReply;
    }

    public void setVisible(boolean needAnim, int listSize) {
        if (needAnim) {
            if (listSize == 0) notificationEmpty.startAnimation(alphaIn);
        } else {
            if (listSize == 0) notificationEmpty.setVisibility(View.VISIBLE);
            else notificationEmpty.setVisibility(View.GONE);
        }
    }

    public void hide(){
        notificationEmpty.startAnimation(alphaOut);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == alphaIn) notificationEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == alphaOut) {
            notificationEmpty.setVisibility(View.GONE);
            infoPageReply.notifyInsert(0);
        }
        notificationEmpty.setAnimation(null);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
