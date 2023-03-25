package com.example.myquizapp.Adaptors;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizapp.DbQuery;
import com.example.myquizapp.Models.TestModel;
import com.example.myquizapp.R;
import com.example.myquizapp.StartTestActivity;

import java.util.List;

public class TestAdaptor extends RecyclerView.Adapter<TestAdaptor.ViewHolder> {
    private List<TestModel> testList;

    public TestAdaptor(List<TestModel> testList) {
        this.testList = testList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();

        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testNo;
        private TextView topScore;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testNo = itemView.findViewById(R.id.testNo);
            topScore = itemView.findViewById(R.id.scoreTest);
            progressBar = itemView.findViewById(R.id.testProgressBar);


        }
        private void setData(int pos,int progress)
        {
            testNo.setText("Test No : "+String.valueOf(pos+1));
            topScore.setText(String.valueOf(progress)+"%");
            progressBar.setProgress(progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
