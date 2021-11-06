package com.michelle.gallenero.mobileappexercise.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.michelle.gallenero.mobileappexercise.R
import com.michelle.gallenero.mobileappexercise.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}