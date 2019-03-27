package com.jaepon.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mQuestionTxt;
    private Button mTrueBtn, mFalseBtn, mNextBtn, mShowBtn;
    private int mQuestionIndex = 0; // 题目索引
    private Question[] mQuestions = new Question[]{
            new Question(R.string.Q1, false),
            new Question(R.string.Q2, true),
            new Question(R.string.Q3, true),
            new Question(R.string.Q4, true),
            new Question(R.string.Q5, true),
            new Question(R.string.Q6, true),
            new Question(R.string.Q7, true),
            new Question(R.string.Q8, true)
    };
    private static final String TAG = "MainActivity"; // 日志附加
    private static final String KEY_INDEX = "index"; // 索引键
    private static final int REQUEST_CODE_ANSWER = 10;
    private static final String EXTRA_ANSWER = "answer";
    private static final String EXTRA_ANSWER_SHOW = "answer_show";
    private TranslateAnimation mTranslateAnimation; // 平移动画
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() 在内存创建Activity");
        if (savedInstanceState != null){
            mQuestionIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Log.d(TAG, "恢复状态");
        }
        setContentView(R.layout.activity_main);
        mQuestionTxt = findViewById(R.id.tv_question);
        updateQuestion();
        mTrueBtn = findViewById(R.id.btn_true);
        mFalseBtn = findViewById(R.id.btn_false);
        mNextBtn = findViewById(R.id.btn_next);
        mShowBtn = findViewById(R.id.btn_show);
        mTrueBtn.setOnClickListener(this);
        mFalseBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mShowBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() 准备显示界面");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() 前台可以看见界面 恢复界面");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause() 准备跳转到另一个界面");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() 界面在后台 前台不可见");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() 界面被销毁");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart() 准备再次回到前台");
    }

    private void updateQuestion() {
        int resId = mQuestions[mQuestionIndex].getResId();
        mQuestionTxt.setText(resId); // 设置文本
//        mTranslateAnimation  = new TranslateAnimation(-20,20,0,0);
//        mTranslateAnimation.setDuration(2000); // 动画持续时间
//        mTranslateAnimation.setRepeatCount(1); // 重复次数（并不包括第一次）
//        mTranslateAnimation.setRepeatMode(Animation.REVERSE); // 动画执行模式 RESTART是重新播放 REVERSE是倒序播放
//        mQuestionTxt.startAnimation(mTranslateAnimation); // 文本组件 加载指定的动画
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_alpha_rotate_scale);
        mQuestionTxt.startAnimation(mAnimation);
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answer = mQuestions[mQuestionIndex].isAnswer(); // 正确答案
        if (userAnswer == answer) {
            Toast.makeText(MainActivity.this, R.string.yes, Toast.LENGTH_SHORT).show(); // 回答正确
            mNextBtn.setEnabled(true);
            return;
        }
        Toast.makeText(MainActivity.this, R.string.no, Toast.LENGTH_SHORT).show(); // 回答错误
    }

    private void nextQuestion() {
        mQuestionIndex = (mQuestionIndex + 1) % mQuestions.length;
        updateQuestion();
        if (mQuestionIndex == mQuestions.length - 1) {
            setDrawable(R.drawable.ic_reset);
            mNextBtn.setText(R.string.btn_next_reset);
        }
        if (mQuestionIndex == 0) {
            setDrawable(R.drawable.ic_move_to_next);
            mNextBtn.setText(R.string.btn_next);
        }
        mNextBtn.setEnabled(false);
    }

    private void setDrawable(int resId) {
        Drawable drawable = getDrawable(resId);
        if (drawable != null){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mNextBtn.setCompoundDrawablesRelative(null, null, drawable, null);
        }
    }

    private void showAnswer() {
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
        // 隐式跳转  不知道具体的Activity
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
        startActivity(intent);*/

        // 显示跳转  知道具体的Activity
        String answer = mQuestions[mQuestionIndex].isAnswer() ? "正确" : "错误"; // 正确答案
        Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
        intent.putExtra(EXTRA_ANSWER, answer); // 传值 putExtra()
        startActivityForResult(intent, REQUEST_CODE_ANSWER); // 需要返回值的启动Activity方法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_true:
                checkAnswer(true);
                break;
            case R.id.btn_false:
                checkAnswer(false);
                break;
            case R.id.btn_next:
                nextQuestion();
                break;
            case R.id.btn_show:
                showAnswer();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mQuestionIndex);
        Log.d(TAG, "保存状态");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ANSWER){ // 请求代码一致
            if (resultCode == RESULT_OK){ // 返回代码一致
                if (data != null){
                    String result = data.getStringExtra(EXTRA_ANSWER_SHOW); // 取值
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
