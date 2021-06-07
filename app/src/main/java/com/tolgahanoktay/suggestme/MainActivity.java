package com.tolgahanoktay.suggestme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tolgahanoktay.suggestme.Fragments.FragmentHome;
import com.tolgahanoktay.suggestme.Fragments.FragmentNews;
import com.tolgahanoktay.suggestme.Fragments.FragmentSearch;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        navigationView = findViewById(R.id.navigation_bottom);
        navigationView.setOnNavigationItemSelectedListener(navSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, new FragmentHome()).commit();


        checkConnection();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {

                case R.id.nav_menu_home:
                    selectedFragment = new FragmentHome();
                    break;

                case R.id.nav_menu_search:
                    selectedFragment = new FragmentSearch();
                    break;

                case R.id.nav_menu_news:
                    selectedFragment = new FragmentNews();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_fragment, selectedFragment).commit();

            return true;
        }
    };



    public void checkConnection(){
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (null!=networkInfo){
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this,"Wifi ",Toast.LENGTH_LONG).show();
            }
            if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                //Toast.makeText(this,"Mobile ",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this,"No Internet Connection Please Check Your Network Connection",Toast.LENGTH_LONG).show();
        }

    }
}