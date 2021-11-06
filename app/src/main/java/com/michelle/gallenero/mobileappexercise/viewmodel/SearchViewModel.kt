package com.michelle.gallenero.mobileappexercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michelle.gallenero.mobileappexercise.utils.ApiManager
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val mutablePhotoList = MutableLiveData<List<String>>()
    private var searchKeyword: String = ""
    private var pageToFetch: Int = 1

    /**
     * Retrieve the list of photos to display
     *
     * @return the live data list of image URLs in string
     */
    fun getPhotos(): LiveData<List<String>> {
        return mutablePhotoList
    }

    /**
     * Retrieve the first page of search results.
     * This method is called when user inputs search term in the search view.
     *
     * @param keyword user input for the search term
     */
    fun search(keyword: String) {
        pageToFetch = 1
        searchKeyword = keyword
        callSearchApi()
    }

    /**
     * Retrieve the next page of the search results.
     * This method is called when user scrolls at the end of the view.
     */
    fun fetchMoreImages() {
        pageToFetch++
        callSearchApi()
    }

    /**
     * Call the Flickr API for image search.
     */
    private fun callSearchApi() {
        // We need to put the call in a coroutine to wait for the response without
        // blocking the main thread
        viewModelScope.launch {
            val response = ApiManager.getInstance().searchImages(searchKeyword, pageToFetch)
            val photoList = response.photos.photo.map {
                // Once response is received, the URL of the image is created and put into a list
                "https://farm${it.farm}.static.flickr.com/${it.server}/${it.id}_${it.secret}.jpg"
            }
            // Update the Live data value
            mutablePhotoList.postValue(photoList)
        }
    }
}