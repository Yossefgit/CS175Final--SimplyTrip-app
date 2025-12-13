Course: CS 175 
Platform: Android 
Group Members 
• Yossef Eini  (yossef.eini@sjsu.edu)  
• Tasnim Oyshi (tasnim.oyshi@sjsu.edu) 
Hardware & Software Requirements 
• Minimum Android API Level: 29 (Android 10) 
• Target Platform: Android mobile devices 
• Development Environment: Android Studio 
• Programming Language: Java 
• UI Design: XML with Material Design components 
• Data Storage: Local storage using SharedPreferences with Gson serialization 
No external hardware is required. The app runs on Android emulators and physical Android 
devices. 
Project Overview 
SimplyTrip is a mobile travel planning application designed to help users organize trips in a 
structured and user-friendly way. Instead of using notes, spreadsheets, or multiple apps, users can 
store all trip-related information in one place. The app supports trip creation, activity planning, 
budget tracking, and packing preparation. The app focuses on usability, clean UI, and persistent 
local storage so that all data remains available even after closing the app. 
User Guide – How the App Works 
Video Link - www.youtube.com/watch?v=ytjSQD-1Sxw  
1. Launching the App 
When the user opens SimplyTrip, they are greeted with a welcome screen. Tapping “Get Started” 
navigates the user to the main trip menu. 
2. Creating a New Trip 
From the Trip Menu screen, the user can tap “Add New Trip” to create a trip. 
For each trip, the user can enter: 
• Trip name 
• Trip destination 
• Trip start and end dates 
• Trip length (calculated from selected dates) 
• Budget amount 
• Number of travelers 
• Additional notes (places to visit, restaurants, reminders, etc.) 
Once completed, the user taps Save Trip, and the trip is stored locally. 
3. Viewing and Managing Trips 
All saved trips appear in a list on the My Trips screen. 
From this list, the user can: 
• Select a trip to view full details 
• Pin a trip to easily find important trips 
• Delete a trip 
• Update trip information 
Trips remain saved even after closing the app. 
4. Trip Details Screen 
When a trip is selected, the Trip Details screen displays: 
• Trip title and destination 
• Travel dates and trip length 
• Budget and cost per person 
• Number of travelers 
• Notes entered during creation 
The user can edit trip details at any time. 
5. Adding Activities to a Trip 
Inside each trip, users can add activities. 
For each activity, the user can: 
• Enter the activity name (e.g., hiking, watching a movie, sightseeing) 
• Select a start time and end time 
• Automatically calculate the activity duration 
• Add optional notes for the activity 
Users can: 
• Edit activities 
• Delete activities 
• Save updated activities 
All activities are displayed in a list under the selected trip. 
6. Budget Management 
The app allows users to logically organize trip budgets. 
Users can: 
• Enter a total trip budget 
• Split the budget across different categories such as: 
◦ Food 
◦ Accommodation 
◦ Activities 
This helps users better understand how money is allocated for the trip. 
7. Baggage and Packing List 
SimplyTrip includes a baggage checklist feature. 
Users can: 
• Add baggage items such as clothes, shoes, electronics, or documents 
• Mark items as packed 
• Delete individual items 
• Update the list at any time 
This feature helps ensure nothing important is forgotten before travel. 
8. Saving and Persistence 
All data (trips, activities, budgets, and baggage lists) are saved locally using SharedPreferences. 
Data is serialized using Gson so that complex objects can be stored and restored accurately. Even 
after closing or restarting the app, all saved trips remain available. 
Implementation Overview 
Frontend Implementation 
• XML layouts are used to design screens such as: 
◦ Main screen 
◦ Trip menu 
◦ Add trip screen 
◦ Trip details screen 
• RecyclerView is used to display lists of trips and activities. 
• Material Design components ensure a consistent and polished UI. 
Backend Implementation 
• TripModel: Represents trip data including destination, dates, budget, travelers, and notes. 
• TripAdapter: Connects trip data to RecyclerView items. 
• TripRepository: Handles saving, loading, updating, and deleting trips using 
SharedPreferences. 
The code follows modular design principles and separates UI logic from data handling. 
APK Location 
The APK file is included in the repository at: /app/build/outputs/apk/debug/app-debug.apk 
This APK can be installed on any Android device running API level 29 or higher. 
Backlog Information 
The project backlog is included in the repository as a spreadsheet file. 
• All backlog tasks are marked Completed 
• All tasks have Priority 4 
• No cells contain errors or red backgrounds 
• Only presentation-related tasks were excluded from completion, as allowed
