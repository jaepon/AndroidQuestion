package com.jaepon.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Objects;

public class AnswerActivity extends AppCompatActivity {
    private TextView mAnswerTv;
    private ImageView mImageView;
    private static final String EXTRA_ANSWER = "answer";
    private static final String EXTRA_ANSWER_SHOW = "answer_show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_answer);

        mImageView = findViewById(R.id.image_view);
        mAnswerTv = findViewById(R.id.tv_answer);
        Intent data = getIntent();
        String answer = data.getStringExtra(EXTRA_ANSWER); // 获取答案
        mAnswerTv.setText(answer); // 显示答案

        data.putExtra(EXTRA_ANSWER_SHOW,"您已经查看了答案");
        setResult(RESULT_OK, data); // 返回结果

        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.animator_alpha); // 实例动画器
        animator.setTarget(mImageView); // 给图片设置动画
        animator.start(); // 开始动画
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 动画开始时

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束时

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // 动画取消时

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // 动画重复时

            }
        });

        ValueAnimator moneyAnimator = ValueAnimator.ofFloat(0f, 1000000.00f); // 实例动画器
        moneyAnimator.setInterpolator(new AccelerateInterpolator()); // 设置开始时慢中间加速
        moneyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 更新动画时
                float money = (float) animation.getAnimatedValue(); // 获取金额
                DecimalFormat df = new DecimalFormat(",###.00£");
                mAnswerTv.setText(df.format(money)); // 显示金额
            }
        });
//        moneyAnimator.setDuration(1000); // 设置动画时间
//        moneyAnimator.start(); // 开始动画

        int startColor = Color.parseColor("#FFDEAD"); // 开始时的颜色
        int endColor = Color.parseColor("#FF4500"); // 结束时的颜色
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(startColor,endColor); // 实例动画器
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue(); // 获取颜色
                mAnswerTv.setTextColor(color); // 设置文本颜色
            }
        });
//        colorAnimator.setDuration(1000); // 设置动画时间
//        colorAnimator.start(); // 开始动画

        AnimatorSet set = new AnimatorSet();
        set.playTogether(moneyAnimator,colorAnimator);
        set.setDuration(1000); // 设置动画时间
        set.start(); // 开始动画
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getDrawable(); // 获取逐帧动画对象
//        animationDrawable.start(); // 开始播放
    }
}
