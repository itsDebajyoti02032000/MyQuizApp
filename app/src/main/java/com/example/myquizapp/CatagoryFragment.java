package com.example.myquizapp;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.myquizapp.Adaptors.CatagoryAdapter;


public class CatagoryFragment extends Fragment {

    public CatagoryFragment() {
        // Required empty public constructor
    }

    private GridView catView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catagory, container, false);
        catView = view.findViewById(R.id.cat_Grid);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Catagories");
        CatagoryAdapter adapter = new CatagoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);

        return view;
    }

}