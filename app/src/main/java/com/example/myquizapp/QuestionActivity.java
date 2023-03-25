package com.example.myquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myquizapp.Adaptors.QuestionGridAdaptor;
import com.example.myquizapp.Adaptors.QuestionsAdapter;

import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID,timerTV,catNameTV;
    private Button submitBtn,markBtn,clearSelBtn;
    private ImageButton prevQuesBtn,nextQuesBtn;
    private ImageView quesListBtn;
    private  int quesID;
    private CountDownTimer timer;
    QuestionsAdapter quesAdapter;

    private DrawerLayout drawer;
    private ImageButton drawerClose;
    private GridView quesListGridView;
    private QuestionGridAdaptor gridAdaptor;

    private ImageView markImage;
    private long timeLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_activity);
        init();
        quesAdapter=new QuestionsAdapter(DbQuery.g_question_list);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // for swiping the recycler view horizontaly
        questionsView.setLayoutManager(layoutManager);


        gridAdaptor = new QuestionGridAdaptor(this,DbQuery.g_question_list.size());
        quesListGridView.setAdapter(gridAdaptor);

        setSnapHelper();

        setClickListeners();

        startTimer();
    }
    private void init()
    {
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        submitBtn = findViewById(R.id.submitBtn);
        markBtn = findViewById(R.id.markBtn);
        clearSelBtn = findViewById(R.id.clear_selBtn);
        prevQuesBtn = findViewById(R.id.prevQuesBtn);
        nextQuesBtn = findViewById(R.id.next_quesBtn);
        quesListBtn = findViewById(R.id.ques_list_gridBtn);
        quesID=0;
        tvQuesID.setText("1/"+String.valueOf(DbQuery.g_question_list.size()));
        catNameTV.setText(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        drawer = findViewById(R.id.drawer_layout);
        drawerClose = findViewById(R.id.drawer_closeBtn);
        markImage = findViewById( R.id.marked_image);
        quesListGridView = findViewById(R.id.quesListGrid);
        DbQuery.g_question_list.get(0).setStatus(DbQuery.UNANSWERED);
    }
    private void setSnapHelper()
    {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID=recyclerView.getLayoutManager().getPosition(view);
                if(DbQuery.g_question_list.get(quesID).getStatus() ==DbQuery.NOT_VISITED)
                {
                    DbQuery.g_question_list.get(quesID).setStatus(DbQuery.UNANSWERED);
                }
                if(DbQuery.g_question_list.get(quesID).getStatus()==DbQuery.REVIEW){
                    markImage.setVisibility(View.VISIBLE);
                }
                else
                {
                    markImage.setVisibility(View.GONE);
                }
                tvQuesID.setText(String.valueOf(quesID+1)+"/"+String.valueOf(DbQuery.g_question_list.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setClickListeners()
    {
        prevQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quesID>0)
                {
                    questionsView.smoothScrollToPosition(quesID-1);
                }
            }
        });

        nextQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quesID<DbQuery.g_question_list.size()-1)
                {
                    questionsView.smoothScrollToPosition(quesID+1);
                }
            }
        });

        clearSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbQuery.g_question_list.get(quesID).setSelectedAns(-1);
                DbQuery.g_question_list.get(quesID).setStatus(DbQuery.UNANSWERED);
                markImage.setVisibility(View.GONE);
                quesAdapter.notifyDataSetChanged();
            }
        });

        quesListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !drawer.isDrawerOpen(GravityCompat.END))
                {
                    gridAdaptor.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });

        markBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markImage.getVisibility() !=View.VISIBLE)
                {
                    markImage.setVisibility(View.VISIBLE);
                    DbQuery.g_question_list.get(quesID).setStatus(DbQuery.REVIEW);

                }
                else
                {
                    markImage.setVisibility(View.GONE);

                    if(DbQuery.g_question_list.get(quesID).getSelectedAns()!=-1)
                    {
                        DbQuery.g_question_list.get(quesID).setStatus(DbQuery.ANSWERED);

                    }
                    else
                    {
                        DbQuery.g_question_list.get(quesID).setStatus(DbQuery.UNANSWERED);
                    }
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }
        });
    }

    private void submitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
        builder.setCancelable(true);

        View view =getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);
        Button cancelB =view.findViewById(R.id.cancelB);
        Button confirmB=view.findViewById(R.id.confirmB);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               timer.cancel();
               alertDialog.dismiss();

               Intent i = new Intent(QuestionActivity.this,ScoreActivity.class);
               long totalTime=DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()*60*1000;
               i.putExtra("TIME_TAKEN",totalTime-timeLeft) ;
               startActivity(i);
               QuestionActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    private void startTimer()
    {
        long totalTime = DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()*60*1000;
         timer = new CountDownTimer(totalTime+1000,1000) {
            @Override
            public void onTick(long remainingTime) {
                timeLeft=remainingTime;
                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                        );
                timerTV.setText(time);
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(QuestionActivity.this,ScoreActivity.class);
                long totalTime=DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()*60*1000;
                i.putExtra("TIME_TAKEN",totalTime-timeLeft) ;
                startActivity(i);
                QuestionActivity.this.finish();
            }
        };
        timer.start();
    }

    public void goToQuestion(int position){
        questionsView.smoothScrollToPosition(position);
        if(drawer.isDrawerOpen(GravityCompat.END))
        {
            drawer.closeDrawer(GravityCompat.END);
        }
    }
}