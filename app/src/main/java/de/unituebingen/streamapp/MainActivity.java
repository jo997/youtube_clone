package de.unituebingen.streamapp;


import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = " ";
    private AppBarConfiguration mAppBarConfiguration;
    private UserData userStatus;

    // drawer menu
    public static Menu drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_register, R.id.nav_categories, R.id.nav_videos, R.id.nav_playlists)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        // set ThreadPolicy to allow network activity on main thread
        // (should be avoided and not needed, if requests are made correctly!)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // Initialize User Status
        this.userStatus = new UserData(this);

        // get drawer menu
        drawerMenu = navigationView.getMenu();

        if (!userStatus.isLoggedIn()){
            drawerMenu.findItem(R.id.nav_logout).setVisible(false);
            drawerMenu.findItem(R.id.nav_login).setVisible(true);
            drawerMenu.findItem(R.id.nav_register).setVisible(true);
            drawerMenu.findItem(R.id.nav_profile).setVisible(false);

        }
        if (userStatus.isLoggedIn()) {
            drawerMenu.setGroupVisible(R.id.LogReg, true);
            drawerMenu.findItem(R.id.nav_login).setVisible(false);
            drawerMenu.findItem(R.id.nav_register).setVisible(false);
            Log.d(TAG, "onCreateWWWWWWW: " + userStatus.getAuthentication());
        }


        // logout function
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_logout) {
                    userStatus.resetUserData();
                    drawerMenu.findItem(R.id.nav_login).setVisible(true);
                    drawerMenu.findItem(R.id.nav_register).setVisible(true);
                    drawerMenu.findItem(R.id.nav_profile).setVisible(false);
                    drawerMenu.findItem(R.id.nav_logout).setVisible(false);
                }

                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });


        if (userStatus.getAuthentication() != null)
            Log.println(Log.ERROR, "APIKEY Main", userStatus.getAuthentication());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}