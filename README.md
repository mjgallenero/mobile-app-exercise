# mobile-app-exercise

This application is implemented to utilize the Flickr Search API and show the results in a 3-column scollable view.
It is developed in Kotlin language and followed the MVVM software architecture pattern. 

A SearchView is used for the input UI. This is where the user will input the search keyword and upon submitting, the request will be sent.
It utilizes Retrofit and OkHttp for sending request to the API. 
Coroutines were used while sending the request and waiting for the response. This is used to avoid blocking the main thread.

For the 3-column scrollable view, a RecyclerView is used. When a the user enters a search keyword, the view will be loaded with the images of the results.
When the user scrolls at the end of the list, the app will update and load more results.

Unit tests for the SearchViewModel is also added in this repository.
