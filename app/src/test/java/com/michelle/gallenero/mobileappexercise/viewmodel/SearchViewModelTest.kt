package com.michelle.gallenero.mobileappexercise.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var lifeCycle: Lifecycle

    @MockK
    private lateinit var lifeCycleOwner: LifecycleOwner

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("new thread")

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
        lifeCycle = LifecycleRegistry(lifeCycleOwner)
        searchViewModel = SearchViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun testGetPhotos() {
        // Set dummy data
        val sampleListMutableLiveData = MutableLiveData<List<String>>()
        val sampleList = mutableListOf<String>()
        sampleList.add("https://sample.url.com")
        sampleList.add("https://sample.url2.com")

        sampleListMutableLiveData.postValue(sampleList)

        // Set the dummy data to the mutablePhotoList variable of SearchViewModel
        val field = SearchViewModel::class.java.getDeclaredField("mutablePhotoList")
        field.isAccessible = true
        field[searchViewModel] = sampleListMutableLiveData

        val test = searchViewModel.getPhotos()

        // Assert that the value retrieved is the same
        Assert.assertEquals(sampleListMutableLiveData, test)
    }

    @Test
    fun testSearch() = runBlocking {
        searchViewModel.search("cat")

        // Get the value of searchKeyword variable from the SearchViewModel
        val field = SearchViewModel::class.java.getDeclaredField("searchKeyword")
        field.isAccessible = true
        val searchKeyword = field[searchViewModel]

        // Assert that the keyword passed in the viewmodel is set to the variable searchKeyword
        Assert.assertEquals("cat", searchKeyword)

        searchViewModel.getPhotos().observeForever {
            // assert that the list of URLs received is not null
            Assert.assertNotNull(it)
            Assert.assertTrue(it.size == 100)
        }
    }
}