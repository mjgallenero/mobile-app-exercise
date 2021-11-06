package com.michelle.gallenero.mobileappexercise.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michelle.gallenero.mobileappexercise.utils.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {
    private val mutablePhotoList = MutableLiveData<List<String>>()
    private val mutableHasError = MutableLiveData<Boolean>()
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

    fun hasErrorEncountered(): LiveData<Boolean> {
        return mutableHasError
    }

    /**
     * Call the Flickr API for image search.
     */
    private fun callSearchApi() {
        // We need to put the call in a coroutine to wait for the response without
        // blocking the main thread
        viewModelScope.launch (Dispatchers.IO) {
             ApiManager.getInstance().searchImages(searchKeyword, pageToFetch)

            // When response is successful, update the mutablePhotoList
            // When response is not successful for some reason, e.g. invalid API Key, server error
            // set mutableHasError to true
            try {
                val response = ApiManager.getInstance().searchImages(searchKeyword, pageToFetch)
                Log.d("tag", "$response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        val photoList = it.photos.photo.map { photo ->
                            // Once response is received, the URL of the image is created and put into a list
                            "https://farm${photo.farm}.static.flickr.com/" +
                                    "${photo.server}/${photo.id}_${photo.secret}.jpg"
                        }
                        // Update the Live data value
                        mutablePhotoList.postValue(photoList)
                        mutableHasError.postValue(false)
                    }
                } else {
                    // Response is not successful, set mutableHasError to true
                    mutableHasError.postValue(true)
                }
            } catch (exception: Exception) {
                // Response is not successful, set mutableHasError to true
                mutableHasError.postValue(true)
            }
        }
    }
}