# DukeStudy
DukeStudy is a mobile Android application designed to help connect Duke University classmates in order to produce a more productive learning environment.

## Installation
DukeStudy should be imported into Android Studio as a project.

Note that our development was performed using Android Studio 2.3. Our testing was performed with the Nexus 5 Emulator, API 24:5554.

## Workflow
Once launched, the user will be prompted to sign in. If user is not currently signed up, registriation can be completed with an @duke.edu email address. Once logged in for the first time, the user can add classes for which they would like to find study groups. The first step is to press the Add Class button on the navigation bar. The added classes will be added to the navigation bar. By navigating to the added classes, groups can be added and navigated to in a similar fashion.
##Support Details
###Overall Structure of the code
For the overall stucture of the code, we have a Main Activity which calls fragments to deal with the different user interface components such as those for Course pages, Group pages, and Course Listings.
The main fragments and the fragments they utilize are:
##### 1. CourseFragment (which utilizes PostsFragment, GroupListFragment, and MembersFragment)
##### 2. GroupsFragment (Which utilizes PostsFragment, EventsListFragment, and MembersFragment)
##### 3. ProfileFragment (which utilized EditProfileFragment)
Each of these fragments are used to display and save user specific information to the database. These fragments create load, edit, and create objects which can subsequently saved to our database through the use of the Firebase API. The main object used are:
##### 1. Post
##### 2. Comment
##### 3. Course
##### 4. Event
##### 5. Group
##### 6. Student
##### 7. Util
It should be noted that the fragments and objects are in seperate files in the java directory for organizational simiplicity. Also, there were some auxiliary activitys used for Login and user data entry, however they are not core to the functioning of our application and were implemented mostly for UI stylistic reasons. 

###APIs used
The main API used in this application was the Firebase API. This is used beacuse it can convert developer defined objects into JSON for saving to database. On loads, it converts JSON to user define objects. Also, there are built-in listeners that trigger on changes to user specified portions to the database. These functionalities greatly simplified development.

###Additional Files
###Test scripts
Due to time constraint, manual testing was done to both 
:copyright: JBeibS 2017
