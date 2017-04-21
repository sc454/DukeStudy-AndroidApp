package com.ds.DukeStudy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.fragments.AddCourseFragment;
import com.ds.DukeStudy.fragments.CourseFragment;
import com.ds.DukeStudy.fragments.EditProfileFragment;
import com.ds.DukeStudy.fragments.GroupFragment;

import com.ds.DukeStudy.fragments.ProfileFragment;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// Main Activity which defaults to Profile page of the user
// This activity handles navigation drawer clicks

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        GroupFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener {
        //,CourseFragment.OnFragmentInteractionListener {

    // Fields

    private static final String TAG = "MainActivity";
    public Student student;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private FirebaseAuth.AuthStateListener authListener;
    private ValueEventListener studentListener;
    private DatabaseReference studentRef;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private NavigationView navView;
    private View navHeaderView;
    private TextView navUserName, navUserEmail;

    private HashMap<Integer,String> courseMenuIds = new HashMap<Integer,String>();
    private HashMap<Integer,String> groupMenuIds = new HashMap<Integer,String>();
    private ArrayList<Course> studentCourses = new ArrayList<>();
    private ArrayList<Group> studentGroups = new ArrayList<>();
    private static final int ADD_CLASS = 0;

    // Getters

//    public FirebaseUser getUser() {return user;}
    public Student getStudent() {return student;}

    // Other methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Course c = new Course("Random Signals and Noise", "ECE", "581", "Nolte");

        // Get view items
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navHeaderView = navView.inflateHeaderView(R.layout.main_nav_header);
        navUserName = (TextView) navHeaderView.findViewById(R.id.navUserName);
        navUserEmail = (TextView) navHeaderView.findViewById(R.id.navUserEmail);

        // Set tool bar and drawer layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        // Set references
        user = Database.getUser();
        auth = FirebaseAuth.getInstance();
        studentRef = Database.ref.child(Util.STUDENT_ROOT).child(user.getUid());

        // Initialize listeners
        createListeners();

        // Send to profile
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ProfileFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO
        switch (item.getItemId()) {
            case R.id.action_logout:
                if (authListener != null) {
                    auth.removeAuthStateListener(authListener);
                }
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportActionBar().setTitle(item.getTitle());
        Log.i(TAG, "Item " + item.getTitle() + " selected");

        // Handle navigation clicks
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_profile:
                fragment = new ProfileFragment(); break;
            case ADD_CLASS:
                fragment = new AddCourseFragment(); break;
            default:
                if (courseMenuIds.containsKey(item.getItemId())) {
                    fragment = new CourseFragment().newInstance(courseMenuIds.get(item.getItemId()));
                } else if (groupMenuIds.containsKey(item.getItemId())){
                    fragment = new GroupFragment().newInstance(groupMenuIds.get(item.getItemId()));
                } else {
                    Log.e(TAG, "Menu id not found");
                }
        }

        // Check item and close drawer
        unCheckAllMenuItems(navView.getMenu());
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);

        // Start fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
        return true;
    }

    //TODO
    @Override
    public void onFragmentInteraction(int tag, int view) {
        //On clicking edit profile, start up edit profile fragment
        if (tag == 0) {
            if (view == R.id.editProfileButton) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new EditProfileFragment()).commit();
            }
        }
    }

    public void onFragmentInteraction(int tag, String view) {}

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
        if (studentListener != null) {
            studentRef.removeEventListener(studentListener);
        }
        //FirebaseAuth.getInstance().signOut();
    }

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if(item.hasSubMenu()) {
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    void createListeners() {

        // Student listener
        studentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: studentListener");
                student = dataSnapshot.getValue(Student.class);

                // Create new student, if needed
                if (student == null) {
                    Toast.makeText(MainActivity.this, "Creating new student...", Toast.LENGTH_SHORT).show();
                    student = new Student(Database.getUser().getEmail());
                    student.put();
                }

                // Update UI
                updateStudent();
//                updateCourses();
//                updateGroups();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: studentListener", databaseError.toException());
            }
        };

        // Authorization listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "OnAuthChange: authListener");
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Log.i(TAG, "Loading auth...");
                    studentRef = Database.ref.child(Util.STUDENT_ROOT).child(user.getUid());
                    studentRef.addValueEventListener(studentListener);
                }
            }
        };
    }

//    private void updateCourses() {
//        studentCourses.clear();
//        Database.ref.child(Util.COURSE_ROOT).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (String key : student.getCourseKeys()) {
//                    if (key != null && dataSnapshot.hasChild(key)) {
//                        Course course = dataSnapshot.child(key).getValue(Course.class);
//                        studentCourses.add(course);
//                        Log.i(TAG, "Adding " + course.getTitle());
//                    } else {
//                        Log.e(TAG, "Course is unexpectedly null");
//                        Toast.makeText(MainActivity.this, "Error: Could not fetch course.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                updateUi();
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "OnDataChangeCancelled: courses", databaseError.toException());
//            }
//        });
//    }

    private void updateStudent() {
        // Clear courses and groups
        studentCourses.clear();
        studentGroups.clear();

        // Get new values
        Database.ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Load courses
                DataSnapshot courseSnapshot = dataSnapshot.child(Util.COURSE_ROOT);
                for (String key : student.getCourseKeys()) {
                    if (key != null && courseSnapshot.hasChild(key)) {
                        Course course = courseSnapshot.child(key).getValue(Course.class);
                        studentCourses.add(course);
                        Log.i(TAG, "Adding " + course.getTitle());
                    } else {
                        Log.e(TAG, "Course is unexpectedly null");
                        Toast.makeText(MainActivity.this, "Error: Could not fetch course.", Toast.LENGTH_SHORT).show();
                    }
                }

                // Load groups
                DataSnapshot groupSnapshot = dataSnapshot.child(Util.GROUP_ROOT);
                for (String key : student.getGroupKeys()) {
                    if (key != null && groupSnapshot.hasChild(key)) {
                        Group group = groupSnapshot.child(key).getValue(Group.class);
                        studentGroups.add(group);
                        Log.i(TAG, "Adding " + group.getName());
                    } else {
                        Log.e(TAG, "Course is unexpectedly null");
                        Toast.makeText(MainActivity.this, "Error: Could not fetch course.", Toast.LENGTH_SHORT).show();
                    }
                }

                // Update menu
                updateUi();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: courses", databaseError.toException());
            }
        });
    }

    public void updateUi() {
        Log.i(TAG, "Updating nav list");

        // Update nav
        navUserName.setText(student.getName());
        navUserEmail.setText(student.getEmail());

        // Clear navigation drawer options
        Menu menu = navView.getMenu();
        SubMenu classSubMenu = menu.getItem(1).getSubMenu();
        SubMenu groupSubMenu = menu.getItem(2).getSubMenu();
        classSubMenu.clear(); groupSubMenu.clear();
        courseMenuIds.clear(); groupMenuIds.clear();

        Log.i(TAG, "Found " + studentCourses.size() + " courses");
        Log.i(TAG, "Found " + studentGroups.size() + " groups");
        Integer itemId = 1;

        // Fill courses
        for (Course course : studentCourses) {
            String title = course.getDepartment() + " " + course.getCode();
            classSubMenu.add(0, itemId, Menu.NONE, title).setIcon(R.drawable.ic_menu_class);
            courseMenuIds.put(itemId, course.getKey());
//            Log.i(TAG, "Mapping item " + itemId + " to " + course.getKey() + " course");
            itemId++;
        }

        // Add course
        classSubMenu.add(0, ADD_CLASS, 0, "Add Class").setIcon(R.drawable.ic_menu_addclass);

        // Fill groups
        for (Group group : studentGroups) {
            groupSubMenu.add(0, itemId, Menu.NONE, group.getName()).setIcon(R.drawable.ic_menu_groups);
            groupMenuIds.put(itemId, group.getKey());
//            Log.i(TAG, "Mapping item " + itemId + " to " + group.getKey() + " group");
            itemId++;
        }
    }
}