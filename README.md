# RestaurantSearch
a simple exercise for my android skills

## Tech stack:
1. RxJava dealing with run time query with 500 milliseconds as debounce time
2. Hilt as dependency injection tool
3. MVVM + LiveData
4. SharedPreference is used to save and sync liked restaurants
5. navigation graph is used to do simple navigation

## hat's done:
1. Basic restaurant search functionality based user's current location
2. on time search with debounce time as 500ms
3. List view + Map View
4. onClick marker to show details of restaurant

## Assumption and adjudgements used:
1. Clicking physical back button will exit the whole activity.
2. The bottom toggle button is the only button to switch between map view and list view.
3. Search query typing debounce time is 500ms, which means any delay more than 500 ms will trigger a search.
