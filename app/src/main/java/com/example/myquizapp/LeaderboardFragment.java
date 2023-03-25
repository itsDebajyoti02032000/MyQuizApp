package com.example.myquizapp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myquizapp.Adaptors.RankAdaptor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {
    private TextView totalUsersTV,myImageTextTV,myScoreTV,myRankTV;
    private RecyclerView usersView;
    RankAdaptor adaptor;
    private Dialog progressDialog;
    private TextView dialogText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leaderboard");
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leaderboard");
        initViews(view);


        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Loading ......" );
        progressDialog.show();

        LinearLayoutManager layoutManager =  new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);
        adaptor  = new RankAdaptor(DbQuery.g_user_list);
        usersView.setAdapter(adaptor);

        DbQuery.getTopuser(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adaptor.notifyDataSetChanged();
                if(DbQuery.myPerformance.getScore()!=0)
                {

                    if(!DbQuery.isMeOnTopList)
                    {
                        calculateRank();
                    }
                    myScoreTV.setText("Score : " + DbQuery.myPerformance.getScore());
                    myRankTV.setText("Rank :"+DbQuery.myPerformance.getRank());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        totalUsersTV.setText("Total users : "+DbQuery.g_usersCount);
        myImageTextTV.setText(DbQuery.myPerformance.getName().toUpperCase().substring(0,1));
        return view;
    }
    private void initViews(View view)
    {
        totalUsersTV = view.findViewById(R.id.total_users);
        myImageTextTV = view.findViewById(R.id.dp_letter);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.recyclerview);
    }

    private void calculateRank()
    {
        int lowTopScore = DbQuery.g_user_list.get(DbQuery.g_user_list.size()-1).getScore();
        int remaining_slots = DbQuery.g_usersCount -20;
        int mySlot = (DbQuery.myPerformance.getScore()*remaining_slots) / lowTopScore;
        int rank ;
        if(lowTopScore != DbQuery.myPerformance.getScore())
        {
            rank = DbQuery.g_usersCount - mySlot ;
        }
        else
        {
            rank = 21 ;
        }
        DbQuery.myPerformance.setRank(rank);
    }
}