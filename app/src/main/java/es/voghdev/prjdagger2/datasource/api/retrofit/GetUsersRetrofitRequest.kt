package es.voghdev.prjdagger2.datasource.api.retrofit

import es.voghdev.prjdagger2.datasource.api.GetUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetUsersRetrofitRequest {
	@GET("/")
	fun getRandomUsers(@Query("results") maxUsers: Int, @Query("seed") page: Int): Call<GetUsersResponse>
}
