package com.ds.DukeStudy;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.SubMenu;

import com.ds.DukeStudy.fragments.CourseListFragment;
import com.ds.DukeStudy.fragments.CoursesFragment;
import com.ds.DukeStudy.fragments.EditProfileFragment;
import com.ds.DukeStudy.fragments.FirebaseExFragment;
import com.ds.DukeStudy.fragments.GroupsFragment;
import com.ds.DukeStudy.fragments.ProfileFragment;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Post;
import com.ds.DukeStudy.objects.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

// Main Activity which defaults to Profile page of the user
// This activity handles navigation drawer clicks

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,FirebaseExFragment.OnFragmentInteractionListener,EditProfileFragment.OnFragmentInteractionListener,CoursesFragment.OnFragmentInteractionListener {

    // Fields

    public Student student;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private String TAG = "MAIN";
    private ValueEventListener userListener;
    private DatabaseReference userReference;
    private FirebaseAuth.AuthStateListener authListener;

//    private boolean isCourse;
//    private String identificationKey;

    // Getters

    public FirebaseUser getUser() {return user;}
//    public String getIDkey() {return identificationKey;}

    // Android methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create database listener

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                Log.i(TAG, "loadUser:onDataChange");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };

        // Initialize user

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onCreate:onAuthStateChanged");
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    userReference = Database.ref.child("students").child(user.getUid());
                    userReference.addValueEventListener(userListener);
                }
            }
        };

        // Add fragments to navigate between items in the navigation bar
        // Set profile page as default

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

//        Menu menu = navigationView.getMenu();
//        MenuItem item = menu.getItem(1);
//        SubMenu subMenu = item.getSubMenu();
//        subMenu.add("Test");
//        subMenu.add(0, R.id.sampleClass1, 0, "Tester").setIcon(R.drawable.ic_menu_class);
//        subMenu.add(0, R.id.sampleClass2, 0, "New Item").setIcon(R.drawable.ic_menu_class);
//        subMenu.add(R.id.course_submenu, R.id.sampleClass1, 0, "Tester");
//        menudd()
//        menu.add(R.id.course_submenu, R.id.sampleClass1, 0, "Tester");
//        menu.add(0, 0, 0, "Playerzz");//.setIcon(R.drawable.music_audio);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menu.add(R.id.sampleClass1, 1, 1, "Test");
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
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        getSupportActionBar().setTitle(item.getTitle());

        // Handle navigation clicks

        switch (item.getItemId()) {
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class; break;
            case R.id.firebase_ex:
                fragmentClass = FirebaseExFragment.class; break;
            case R.id.nav_addClass:
                fragmentClass = CourseListFragment.class; break;
            case R.id.sampleClass1:
            case R.id.sampleClass2:
            case R.id.sampleClass3:
                fragmentClass = CoursesFragment.class; break;
            case R.id.nav_groups1:
            case R.id.nav_groups2:
                fragmentClass = GroupsFragment.class; break;
        }

        // Begin fragment

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Close drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int tag, int view) {
        //On clicking edit profile, start up edit profile fragment
        Fragment fragment = null;
        Class fragmentClass = null;
        if (tag == 0) {
            if (view == R.id.editProfileButton) {
                //   Log.d("MyApp","I am here");
                fragmentClass = EditProfileFragment.class;
            }
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(int tag, String userName) {
        //Allow interaction between fragments.
        // Change tags for different fragments
//        Fragment fragment = null;
//        Class fragmentClass = null;
//        if (tag == 1) {
////            Log.d("MyApp","I am here");
//            fragmentClass = ProfileFragment.class;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("UserName",userName);
        // bundle.putString("Email",profileEmail);
//            try {
//                fragment = (Fragment) fragmentClass.newInstance();
//            }
//
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        if (userListener != null) {
            userReference.removeEventListener(userListener);
        }
        //FirebaseAuth.getInstance().signOut();
    }
}
