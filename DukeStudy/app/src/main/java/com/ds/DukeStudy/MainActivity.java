package com.ds.DukeStudy;
/**
 * Main Activity which defaults to Profile page of the user
 * This activity handles navigation drawer clicks
* */
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,FirebaseExFragment.OnFragmentInteractionListener,EditProfileFragment.OnFragmentInteractionListener,CoursesFragment.OnFragmentInteractionListener {
    // Add Firebase authentication listener
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else{
                    user = auth.getCurrentUser();
                }

            }
        };

        /* Add fragments to navigate between items in the navigation bar. Set profile page as default*/
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = ProfileFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
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
  //  @Override
    /*
    public void onStop(){
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
            FirebaseAuth.getInstance().signOut();
        }

    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_logout:
                if (authListener != null) {

                    auth.removeAuthStateListener(authListener);
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        getSupportActionBar().setTitle(item.getTitle());
        if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        }
        else if (id == R.id.nav_groups1) {
            fragmentClass = GroupsFragment.class;
        }
        else if (id == R.id.nav_groups2) {
            fragmentClass = GroupsFragment.class;
        }
        else if (id == R.id.firebase_ex) {
            fragmentClass = FirebaseExFragment.class;
        }
        else if(id == R.id.nav_addClass) {
            fragmentClass = CourseListFragment.class;
        }
        else if (id==R.id.sampleClass1||id==R.id.sampleClass2||id==R.id.sampleClass3){
            //Going to need to figure out how to pass information to the fragment for individual courses
            fragmentClass = CoursesFragment.class;
        }



        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int tag,int view) {
        //On clicking edit profile, start up edit profile fragment
        Fragment fragment = null;
        Class fragmentClass = null;
       if(tag==0){
           if(view==R.id.editProfileButton){
            //   Log.d("MyApp","I am here");
               fragmentClass = EditProfileFragment.class;
           }
       }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(int tag,String userName) {
        //Allow interaction between fragments.
        // Change tags for different fragments
        Fragment fragment = null;
        Class fragmentClass = null;
        if (tag == 1) {
         //   Log.d("MyApp","I am here");
            fragmentClass = ProfileFragment.class;
        }
        Bundle bundle = new Bundle();
        bundle.putString("UserName",userName);
        // bundle.putString("Email",profileEmail);
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            }

            catch (Exception e) {
                e.printStackTrace();
            }
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}
