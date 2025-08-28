package com.proj.tubialert

// AccuWeatherApiService.kt (create a new file)
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path // If using path parameters

interface AccuWeatherApiService {
    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("details") details: Boolean = true
    ): Response<List<CurrentConditionResponse>>
}
