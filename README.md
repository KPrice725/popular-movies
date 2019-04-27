# popular-movies
Udacity Android Nanodegree - Project 2: Popular Movies

![DEMO](/readme-resources/popular_movies_demo.gif)

## Project Overview
### Stage 1
- Implement a MovieActivity that displays a grid of movie posters, 
retrieved via the 'themoviedb.org' API.  The grid can be sorted according
to user preference, such as by popularity, rating, etc.
- Clicking a poster in the MovieActivity grid launches a DetailActivity
for that particular item, displaying additional information such as
the title, plot synopsis, user rating, release date, etc.

## Tools That I Used
### Model View Presenter Architecture
![MVP Diagram](/readme-resources/Popular_Movies_MVP_Diagram.jpg)

- The Model Component is the main source and repository for Movie and Genre entity data.  In our case, this handles all GET requests to the TMDB API and retrieves them for the Presenter.
- The View Component is the user interface element, which in this case is the MovieActivity and DetailActivity classes.  This component is responsible for calling on the Presenter when the key UI elements are triggered, such as when the user scrolls, selects a movie poster, or changes the sort parameter.
- The Presenter Component is the primary middle-man between the Model and the View.  The Presenter is responsibile for retrieving the necessary data from the Model and processing it to be handed off to the View for displaying to the user.  It is also called upon by the View during user interactions, after which the Presenter determines the ultimate handling of the event.
- The relationship and interaction between the View and Presenter is dictated by the respective Contract interfaces.

### OkHttp
- Used by the RemoteDataSources, this library is used for building and asynchronously executing network requests to the TMDB API.  This manages the number of requests executed at once, retrieves the JSON response, and provides callbacks for the requesting Repository. 

### Gson Deserialization
![GSON Diagram](/readme-resources/Gson_Deserialization_Diagram.jpg)


![Deserialization Diagram](/readme-resources/Deserialization_Classes.jpg)

- By using the Gson @SerializedName annotations, Gson has a means of deserializing the retrieved JSON responses into instantiated Movie and Genre objects that can be used by the Presenter and View.  For example, in our case, each TMDB Movie request returns a list of twenty movies per page request, which then are deserialized into a single MovieJSONResult object.  This object then contains a List of the twenty movie objects.  Gson uses the .fromJson() method as the principle deserialization method.

### Butterknife
- Butterknife's @BindView annotations cleans up the Activity files, providing a concise means of binding and layout resources to the Activity class.

### Picasso
- Picasso's image loading library provides proper handling, resizing, and caching of movie poster images.

### MovieGridLayoutManager
- My child class implementation of GridLayoutManager automatically scales images to be proportionally sized to fix the width of the MovieActivity RecyclerView.  This also handles setting the span count depending on screen orientation.

### Material Design Library
- Utilized the CoordinatorLayout, AppBarLayout, and CollapsingToolbarLayout to create a customized, collapsing toolbar for the DetailActivity view.  When expanded, this toolbar displays the selected movie's backdrop poster image.  When the user scrolls down, this toolbar collapses, displaying the movie title and freeing up space for the rest of the UI.
