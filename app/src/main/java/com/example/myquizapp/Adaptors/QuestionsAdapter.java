package com.example.myquizapp.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizapp.DbQuery;
import com.example.myquizapp.Models.QuestionModel;
import com.example.myquizapp.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questionsList;

    public QuestionsAdapter(List<QuestionModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ques;
        private Button optionA,optionB,optionC,optionD,prevSelectedBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques= itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedBtn = null;
        }
        private void  setData(final int pos){
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOptions(optionA,1,pos);
            setOptions(optionB,2,pos);
            setOptions(optionC,3,pos);
            setOptions(optionD,4,pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   selectOption(optionA,1,pos);
                }
            });
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB,2,pos);
                }
            });
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC,3,pos);
                }
            });
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD,4,pos);
                }
            });
        }
        private void selectOption(Button btn,int option_num,int quesID)
        {
            if(prevSelectedBtn==null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_question_list.get(quesID).setSelectedAns(option_num);

                changeStatus(quesID,DbQuery.ANSWERED);
                prevSelectedBtn = btn;
            }
            else
            {
                if(prevSelectedBtn.getId()==btn.getId())
                {
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_question_list.get(quesID).setSelectedAns(-1);
                    changeStatus(quesID,DbQuery.UNANSWERED);
                    prevSelectedBtn=null;
                }
                else
                {
                    prevSelectedBtn.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);
                    DbQuery.g_question_list.get(quesID).setSelectedAns(option_num);
                    changeStatus(quesID,DbQuery.ANSWERED);
                    prevSelectedBtn = btn;

                }
            }

        }
        private void setOptions(Button btn,int option_num,int quesID)
        {
            if(DbQuery.g_question_list.get(quesID).getSelectedAns()==option_num)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }

        private  void changeStatus(int id,int status)
        {
            if(DbQuery.g_question_list.get(id).getStatus() !=DbQuery.REVIEW)
            {
                DbQuery.g_question_list.get(id).setStatus(status);
            }
        }
    }
}
