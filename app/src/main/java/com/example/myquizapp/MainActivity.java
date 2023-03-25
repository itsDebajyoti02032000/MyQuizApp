package com.example.myquizapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizapp.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private TextView drawerProfileName;
    private TextView drawerProfileText;
    private NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_home:
                {
                   // bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    setFragment(new CatagoryFragment());
                    return true;
                }
                case R.id.nav_leaderboard:
                {
                    //bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
                    setFragment(new LeaderboardFragment());
                    return true;
                }
                case R.id.nav_account:
                {
                    //bottomNavigationView.setSelectedItemId(R.id.nav_account);
                    setFragment(new AccountFragment());
                    return true;
                }
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        getSupportActionBar().setTitle("Catagories");
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);

        main_frame = findViewById(R.id.main_frame);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DbQuery.myProfile.getName();
        drawerProfileName.setText(name);

        drawerProfileText.setText(name.toUpperCase().substring(0,1));
        setFragment(new CatagoryFragment());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.nav_home){
            //setFragment(new CatagoryFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;

        }else if (id==R.id.nav_account){
            //setFragment(new AccountFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_account);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;

        }else if(id == R.id.nav_leaderboard){
           // setFragment(new LeaderboardFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;

        }
        DrawerLayout drawer = (DrawerLayout)  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(),fragment);
        transaction.commit();
    }
}