package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabasePicture

data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)

fun PictureOfDay.asDatabaseModel() = DatabasePicture(
    mediaType = mediaType,
    title = title,
    url = url
)