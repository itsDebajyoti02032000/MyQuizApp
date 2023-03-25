package com.example.myquizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private LinearLayout logoutBtn;
    private TextView profileImageTV,name,score,rank;
    private LinearLayout leaderB,profileB,bookmarkB;
    private BottomNavigationView bottomNavigationView;
    private Dialog progressDialog;
    private TextView dialogText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        initViews(view);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");
        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Loading ......" );

        String username = DbQuery.myProfile.getName();
        profileImageTV.setText(username.toUpperCase().substring(0,1));
        name.setText(username);
        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if(DbQuery.g_user_list.size()==0)
        {
            progressDialog.show();
            DbQuery.getTopuser(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    if(DbQuery.myPerformance.getScore()!=0)
                    {

                        if(!DbQuery.isMeOnTopList)
                        {
                            calculateRank();
                        }
                        score.setText("Score : " + DbQuery.myPerformance.getScore());
                        rank.setText("Rank :"+DbQuery.myPerformance.getRank());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            score.setText("Score : " + DbQuery.myPerformance.getScore());
            if(DbQuery.myPerformance.getScore() != 0)
            rank.setText("Rank :"+DbQuery.myPerformance.getRank());
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                 GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleClient  = GoogleSignIn.getClient(getContext(),gso);
                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      Intent intent =new Intent(getContext(),LoginActivity.class)  ;
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      getActivity().finish();
                    }
                });
            }
        });

        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(getContext(),MyProfileActivity.class);
                startActivity(i);
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            }
        });
        return view;
    }
    private void initViews(View view){
        logoutBtn =view.findViewById(R.id.logoutB);
        profileImageTV = view.findViewById(R.id.profile_img_text);
        name =view.findViewById(R.id.name);
        score =view.findViewById(R.id.total_score);
        rank =view.findViewById(R.id.rank);
        leaderB =view.findViewById(R.id.leaderboardB);
        bookmarkB =view.findViewById(R.id.bookmarkB);
        profileB =view.findViewById(R.id.profileB);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
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