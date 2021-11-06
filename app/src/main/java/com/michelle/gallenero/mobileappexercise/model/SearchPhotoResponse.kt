package com.michelle.gallenero.mobileappexercise.model

/**
 * This data class defines the data from the response of calling the API
 */

data class SearchPhotoResponse(
    var photos: PhotoList
)

data class PhotoList(
    var perpage: String? = null,
    var page: Int? = null,
    var total: String? = null,
    var photo: List<PhotoRawResponse>
)

data class PhotoRawResponse(
    var id: String? = null,
    var owner: String? = null,
    var secret: String? = null,
    var server: String? = null,
    var farm: Int? = null,
    var title: String? = null,
    var ispublic: Int? = null,
    var isfriend: Int? = null,
    var isfamily: Int? = null
)
