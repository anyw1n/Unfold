package com.example.unfold.data

import android.net.Uri
import com.example.unfold.data.models.AuthInfo
import com.example.unfold.data.models.Collection
import com.example.unfold.data.models.Photo
import com.example.unfold.data.models.TokenBody
import com.example.unfold.data.models.User
import com.example.unfold.util.RedirectUri
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @POST("https://unsplash.com/oauth/token")
    suspend fun getAccessToken(@Body tokenData: TokenBody): AuthInfo

    @GET("me")
    suspend fun getMe(): User

    @GET("users/{username}/likes")
    suspend fun getLikedPhotos(@Query("page") page: Int, @Query("per_page") limit: Int): List<Photo>

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int, @Query("per_page") limit: Int): List<Photo>

    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id: String): Photo

    @POST("photos/{id}/like")
    suspend fun likePhoto(@Path("id") id: String): String

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(@Path("id") id: String): String

    @GET("photos/{id}/download")
    suspend fun downloadPhoto(@Path("id") id: String): String

    @GET("collections")
    suspend fun getCollections(@Query("page") page: Int, @Query("per_page") limit: Int): List<Collection>

    @GET("collections/{id}")
    suspend fun getCollection(@Path("id") id: String): Collection

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(@Query("page") page: Int, @Query("per_page") limit: Int): List<Photo>

    companion object {

        const val InitialPage = 1
        const val Limit = 20

        private const val BaseUrl = "https://api.unsplash.com/"
        const val ClientId = "4xwZcrsEMKPIcqZDdStM9i3jRnSE0czu_FTmy0L9pjs"
        const val ClientSecret = "QJREJKVqoJ9vfp171JoSvzc3fSWgp6zmHxn-EVwBJWU"
        val OAuthUri: Uri = Uri.parse(
            "https://unsplash.com/oauth/authorize" +
                "?client_id=$ClientId" +
                "&redirect_uri=$RedirectUri" +
                "&response_type=code" +
                "&scope=public+read_user+write_likes",
        )

        fun create(credentialsRepository: CredentialsRepository) = Retrofit.Builder().run {
            baseUrl(BaseUrl)
            client(
                OkHttpClient.Builder().run {
                    addInterceptor {
                        it.proceed(
                            it.request().newBuilder().run {
                                val token = credentialsRepository.token
                                if (token != null) {
                                    addHeader("Authorization", "Bearer $token")
                                }
                                build()
                            },
                        )
                    }
                    build()
                },
            )
            addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().run {
                        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        create()
                    },
                ),
            )
            build()
        }
            .create<Api>()
    }
}
