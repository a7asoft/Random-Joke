package com.awto.randomjoke.data.remote

import com.awto.randomjoke.data.remote.model.JokeResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface JokeApi {
    @Headers("Content-Type: application/json")
    @GET("joke/Any")
    suspend fun getRandomJoke(): Response<JokeResponseModel>
}