package com.example.myquizapp.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizapp.Models.QuestionModel;
import com.example.myquizapp.R;

import java.util.List;

public class AnswersAdaptor extends RecyclerView.Adapter<AnswersAdaptor.ViewHolder> {

    private List<QuestionModel> quesList;

    public AnswersAdaptor(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public AnswersAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout,parent,false);
        return new AnswersAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdaptor.ViewHolder holder, int position) {
        String ques = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b= quesList.get(position).getOptionB();
        String c= quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        int selected = quesList.get(position).getSelectedAns();
        int ans = quesList.get(position).getCorrectAns();

        holder.setData(position,ques,a,b,c,d,selected,ans);
    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView quesNo , question , optionA,optionB ,optionC , optionD,result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.question_no);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);
        }

        private void setData(int pos , String ques,String a,String b,String c, String d,int selected , int correctAns)
        {
            quesNo.setText("Question no."+String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. "+a);
            optionB.setText("B. "+b);
            optionC.setText("C."+c);
            optionD.setText("D. "+d);
            if(selected== -1)
            {
                result.setText("Unanswered");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
            }
            else
            {
                if(selected == correctAns)
                {
                    result.setText("Correct");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.Green));
                    setOptionColor(selected,R.color.Green);
                }
                else
                {
                    result.setText("Wrong");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.Red));
                    setOptionColor(selected,R.color.Red);
                }
            }

        }

        private void setOptionColor(int selected,int color)
        {
            switch(selected)
            {
                case 1 :
                    optionA.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 2:
                    optionB.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 3:
                    optionC.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 4:
                    optionD.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
            }
        }
    }
}
