package com.example.quizbuzz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Ques;

public class Question extends AppCompatActivity {

    TextView  timerTxt,tvPoints, questionsTxt;
    ImageView imageview;
    Button btn1, btn2, btn3, btn4;
    int points;
    int total=0;
    int totalCount=97;
    int correct = 0;
    DatabaseReference reference;
    int wrong = 0;
    long millisUntilFinished;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        int index;
        questionsTxt = findViewById(R.id.questionsTxt);
        imageview = (ImageView) findViewById(R.id.questionImage);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        tvPoints = findViewById(R.id.tvPoints);
        timerTxt = findViewById(R.id.timerTxt);
        millisUntilFinished = 30000;
        points = 0;
        getTotalCount();
        startQuestion();
    }

    private void getTotalCount(){
        FirebaseDatabase.getInstance().getReference().child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                totalCount = (int) snapshot.getChildrenCount();
                tvPoints.setText(total+"/"+totalCount);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void startQuestion() {
        updateQuestions();
    }

    private void updateQuestions() {
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        total++;
        tvPoints.setText(total+"/"+totalCount);
        if (total >totalCount) {
            Intent i = new Intent(Question.this, ResultActivity.class );
            i.putExtra("total", String.valueOf(totalCount));
            i.putExtra("correct", String.valueOf(correct));
            i.putExtra("incorrect", String.valueOf(wrong));
            startActivity(i);
        }
        else{
            reference = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Ques question = dataSnapshot.getValue(Ques.class);

                    assert question != null;
                    questionsTxt.setText(question.getQuestion());
                    btn1.setText(question.getOption1());
                    btn2.setText(question.getOption2());
                    btn3.setText(question.getOption3());
                    btn4.setText(question.getOption4());
                    String imageName = question.getImageName();

                    try {
                        if (imageName != null) {
                            if (imageview.getVisibility() == View.GONE)
                                imageview.setVisibility(View.VISIBLE);

                            String uri = "@drawable/" + imageName;
                            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imageResource);
                            imageview.setImageDrawable(res);
                        } else {
                            if (imageview.getVisibility() == View.VISIBLE)
                                imageview.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        imageview.setVisibility(View.GONE);
                    }


                    btn1.setOnClickListener(v -> {
                        if(btn1.getText().toString().equals(question.getAnswer()))
                        {
                            Toast.makeText(getApplicationContext(),"Correct answer",Toast.LENGTH_SHORT).show();
                            btn1.setBackgroundColor(Color.GREEN);
                            correct = correct +1;
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();


                            },1500);
                        }
                        else{
                            //if answer is wrong, find ans and color will be green
                            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
                            wrong = wrong +1;
                            btn1.setBackgroundColor(Color.RED);
                            if(btn2.getText().toString().equals(question.getAnswer())){
                                btn2.setBackgroundColor(Color.GREEN);
                            }
                            else if(btn3.getText().toString().equals(question.getAnswer()))
                                btn3.setBackgroundColor(Color.GREEN);
                            else if(btn4.getText().toString().equals(question.getAnswer())){
                                btn4.setBackgroundColor(Color.GREEN);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                        btn1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btn2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btn3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btn4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestions();
                                    },
                                    1500);


                        }


                    });
                    btn2.setOnClickListener(v -> {
                        if(btn2.getText().toString().equals(question.getAnswer()))
                        {
                            Toast.makeText(getApplicationContext(),"Correct answer",Toast.LENGTH_SHORT).show();
                            btn2.setBackgroundColor(Color.GREEN);
                            correct = correct +1;
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();


                            },1500);

                        }
                        else{
                            //if answer is wrong, find ans and color will be green
                            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
                            wrong = wrong +1;
                            btn2.setBackgroundColor(Color.RED);
                            if(btn1.getText().toString().equals(question.getAnswer())){
                                btn1.setBackgroundColor(Color.GREEN);
                            }
                            else if(btn3.getText().toString().equals(question.getAnswer())){
                                btn3.setBackgroundColor(Color.GREEN);

                            }
                            else if(btn4.getText().toString().equals(question.getAnswer())){
                                btn4.setBackgroundColor(Color.GREEN);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();
                            }, 1500);
                        }



                    });
                    btn3.setOnClickListener(v -> {
                        if(btn3.getText().toString().equals(question.getAnswer()))
                        {
                            Toast.makeText(getApplicationContext(),"Correct answer",Toast.LENGTH_SHORT).show();
                            btn3.setBackgroundColor(Color.GREEN);
                            correct = correct +1;
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                        btn3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestions();


                                    },
                                    1500);

                        }
                        else{
                            //if answer is wrong, find ans and color will be green
                            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
                            wrong = wrong +1;
                            btn3.setBackgroundColor(Color.RED);
                            if(btn1.getText().toString().equals(question.getAnswer())){
                                btn1.setBackgroundColor(Color.GREEN);
                            }
                            else if(btn2.getText().toString().equals(question.getAnswer())){
                                btn2.setBackgroundColor(Color.GREEN);

                            }
                            else if(btn4.getText().toString().equals(question.getAnswer())){
                                btn4.setBackgroundColor(Color.GREEN);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();
                            }, 1500);
                        }



                    });
                    btn4.setOnClickListener(v -> {
                        if(btn4.getText().toString().equals(question.getAnswer()))
                        {
                            Toast.makeText(getApplicationContext(),"Correct answer",Toast.LENGTH_SHORT).show();
                            btn4.setBackgroundColor(Color.GREEN);
                            correct = correct +1;
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();
                            },1500);

                        }
                        else{
                            //if answer is wrong, find ans and color will be green
                            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
                            wrong = wrong +1;
                            btn4.setBackgroundColor(Color.RED);
                            if(btn1.getText().toString().equals(question.getAnswer())){
                                btn1.setBackgroundColor(Color.GREEN);
                            }
                            else if(btn2.getText().toString().equals(question.getAnswer())){
                                btn2.setBackgroundColor(Color.GREEN);

                            }
                            else if(btn3.getText().toString().equals(question.getAnswer())){
                                btn3.setBackgroundColor(Color.GREEN);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                btn1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                btn4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                updateQuestions();
                            }, 1500);
                        }
                    });
                    reverseTimer(30,timerTxt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    public <millisUntilFinished> void reverseTimer(int seconds, final TextView tv){
        timer = new CountDownTimer(seconds* 1000+1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                tv.setText("Completed");
                Intent myIntent = new Intent(Question.this, ResultActivity.class);
                myIntent.putExtra("total",String.valueOf(total));
                myIntent.putExtra("correct",String.valueOf(correct));
                myIntent.putExtra("incorrect",String.valueOf(wrong));

                startActivity(myIntent);

            }
        }.start();
        }


    public void answerSelected(View view) {
    }

    public void nextQuestion(View view) {
    }
}
