package com.nusaproperty.app.data.api

import com.nusaproperty.app.data.*
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface NusaApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun getMe(@Header("Authorization") token: String? = null): UserData

    @GET("api/user/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String? = null): UserProfileData

    @GET("api/properties")
    suspend fun getProperties(): List<PropertyItem>

    @GET("api/properties/featured")
    suspend fun getFeaturedProperty(): PropertyItem

    @GET("api/properties/{id}")
    suspend fun getPropertyById(@Path("id") id: String): PropertyItem

    @PATCH("api/properties/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: String): ToggleFavoriteResponse

    @GET("api/documents")
    suspend fun getDocuments(): DocumentsResponse

    @Multipart
    @POST("api/documents/{id}/upload")
    suspend fun uploadDocument(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): DocumentItem

    @GET("api/sp3k")
    suspend fun getSp3kDetails(): Sp3kDetails

    @POST("api/sp3k/schedule-akad")
    suspend fun scheduleAkad(@Body request: Map<String, String>): Sp3kDetails

    @POST("api/kpr/calculate")
    suspend fun calculateKpr(@Body request: Map<String, Any>): KprCalculation

    @GET("api/notifications")
    suspend fun getNotifications(): List<NotificationItem>
}

object ApiClient {
    const val BASE_URL = "https://4de0-103-121-244-252.ngrok-free.app/"

    var tokenProvider: (() -> String?)? = null

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
            .header("ngrok-skip-browser-warning", "1")
            .header("Accept", "application/json")

        val token = tokenProvider?.invoke()
        if (!token.isNullOrBlank() && original.header("Authorization") == null) {
            builder.header("Authorization", "Bearer $token")
        }

        chain.proceed(builder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: NusaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NusaApiService::class.java)
    }
}
