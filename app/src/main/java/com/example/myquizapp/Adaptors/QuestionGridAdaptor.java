package com.example.myquizapp.Adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myquizapp.DbQuery;
import com.example.myquizapp.QuestionActivity;
import com.example.myquizapp.R;

public class QuestionGridAdaptor extends BaseAdapter {
    private int numOfQues;
    public Context context;

    public QuestionGridAdaptor(Context context,int numOfQues) {
        this.context=context;
        this.numOfQues = numOfQues;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;
        if(view==null){
            myView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);
        }
        else
        {
            myView=view;
        }
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof QuestionActivity)
                {
                    ((QuestionActivity)context).goToQuestion(i);
                }
            }
        });

        TextView quesTV = myView.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(i+1));

        switch(DbQuery.g_question_list.get(i).getStatus())
        {
            case  DbQuery.NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.white)));
                break;

            case  DbQuery.UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.Red)));
                break;

            case  DbQuery.ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.Green)));
                break;

            case  DbQuery.REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.Pink)));
                break;

            default:
                break;
        }
        return myView;
    }
}
