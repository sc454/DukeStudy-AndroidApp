# DukeStudy
DukeStudy is a mobile Android application designed to help connect Duke University classmates in order to produce a more productive learning environment.

## Overview
This initial version was built for Sprint 1. The goal was to create the basic user interface, which will serve as the foundation for the final application, as well as to setup a database, which will serve as the application's backend. In subsequent sprints, the application’s core functionalities will be developed into this initial framework.

## Installation
DukeStudy should be imported into Android Studio as a project.

Note that our development was performed using Android Studio 2.3. Our testing was performed with the Nexus 5 Emulator, API 24:5554.

## Workflow
Once launched, the user will be prompted to sign in. For now, there is no authentication. Then, the user is directed to the profile page. To navigate throughout the application, a side navigation menu is provided. Pages to explore included course and group pages, as well as a temporary database testing page.

## Task Summary
Listed below is a summary of our planned task cards and the completion status. See our task cards from Homework 3 for more information.

#### 1. Side Navigation
For this task, we planned to create an expandable side navigation bar to subsequently link to all pages. This menu can be reached by clicking the button on the top left corner of the application.

#### 2. Top Navigation Template
For this task, we planned to create a template for a top navigation bar to be used in multiple pages. This template is demonstrated in the sample class and group pages.

#### 3. Profile Page
For this task, we planned to create a profile page where the user could see and edit personal account information. This page can be reached by navigating to the "Profile" option in the side navigation. Note that only the name field is currently editable. 

#### 4. Class Page
For this task, we planned to create a class page, implementing the top navigation and filled with dummy data. This page can be reached by navigating to one of the options in the "Classes" section of the side navigation. The data displayed is read from the database.

#### 5. Add Class Page
For this task, we planned to create a non-functional Add Class page. This page can be reached by navigating to the "Add Class" option in the side navigation. The data displayed is read from the database.

#### 6. Groups Page
For this task, we planned to create a Groups page, implementing the top navigation and filled with dummy data. This page can be reached by navigating to one of the options in the "Groups" section of the side navigation. The data displayed is read from the database.

#### 7. Navigation Integration
For this task, we planned to integrate the navigation with each page by creating links. This feature is demonstrated within the side navigation.

#### 8. Database Setup
For this task, we planned to setup a database and create a page to display the reading and writing process. This feature can be reached by navigating to the "Database" option in the side navigation. The page displays current values in the database and can be updated by typing in the text field and pressing "SUBMIT."

#### Extra Features
In addition to the above tasks, we also created a Sign In page. For now, any text can be entered in the username and password fields. After signing in, the user is taken to the profile page. In subsequent sprints, this will communicate with the Firebase authentication module.

Finally, we planned to use static, dummy data across pages. Instead, we actually went a step further and utilized our database to fill the data on these pages instead.

:copyright: JBeibS 2017
