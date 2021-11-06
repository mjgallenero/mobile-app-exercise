package com.michelle.gallenero.mobileappexercise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.michelle.gallenero.mobileappexercise.R
import com.michelle.gallenero.mobileappexercise.databinding.FragmentSearchPhotoBinding
import com.michelle.gallenero.mobileappexercise.viewmodel.SearchViewModel

class SearchPhotoFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var progressBar: LinearProgressIndicator
    private val photosAdapter = PhotosViewAdapter()

    // indicates whether a request to load more images is called
    private var isLoadingMoreImages = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSearchPhotoBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_search_photo, container, false)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.viewModel = searchViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_horizontal)

        val searchView = view.findViewById(R.id.search_view) as SearchView
        searchView.setOnQueryTextListener(this)

        val recyclerView = view.findViewById(R.id.rv_photos) as RecyclerView
        recyclerView.adapter = photosAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // check if the view cannot be scrolled vertically or is at the end of the view
                if (!recyclerView.canScrollVertically(1) && dy != 0 &&
                    !isLoadingMoreImages) {
                    // Call API to fetch more images of the same search keyword
                    searchViewModel.fetchMoreImages()
                    progressBar.visibility = View.VISIBLE
                    isLoadingMoreImages = true
                }
            }
        })

        // observe if the list of URLs is updated
        searchViewModel.getPhotos().observe(viewLifecycleOwner, Observer {
            // update the recycler view with the list of images to show
            with(photosAdapter) {
                // When isLoadingMoreImages is not true, it means that the list of URLs
                // received is a result from a different keyword. Clear the recycler view first before
                // displaying new results
                // When isLoadingMoreImages is true, the results received will just be added to the view
                if (!isLoadingMoreImages) {
                    photoList.clear()
                }
                photoList.addAll(it)
                notifyDataSetChanged()
            }
            progressBar.visibility = View.INVISIBLE
            isLoadingMoreImages = false
        })
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        text?.let {
            // when the text is not empty, call the API to search for the keyword set by user
            searchViewModel.search(text)
            progressBar.visibility = View.VISIBLE
            isLoadingMoreImages = false
        }
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }
}