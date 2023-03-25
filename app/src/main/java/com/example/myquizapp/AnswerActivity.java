package com.example.myquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myquizapp.Adaptors.AnswersAdaptor;

public class AnswerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView answersViewRV ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        toolbar=findViewById(R.id.aa_toolbar);
        answersViewRV= findViewById(R.id.aa_recyclerview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Answers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersViewRV.setLayoutManager(layoutManager);

        AnswersAdaptor adaptor = new AnswersAdaptor(DbQuery.g_question_list);
        answersViewRV.setAdapter(adaptor);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            AnswerActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}