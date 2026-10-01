package com.nusaproperty.app.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.nusaproperty.app.data.*
import com.nusaproperty.app.data.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class NusaPropertyRepository {

    private val api = ApiClient.apiService

    suspend fun login(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(LoginRequest(email.trim(), password))
            Result.success(response)
        } catch (e: Exception) {
            Log.e("NusaRepo", "Login error: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String?
    ): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.register(RegisterRequest(email.trim(), password, fullName.trim(), phone?.trim()))
            Result.success(response)
        } catch (e: Exception) {
            Log.e("NusaRepo", "Register error: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getMe(): UserData? = withContext(Dispatchers.IO) {
        try {
            api.getMe()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching user info: ${e.message}", e)
            null
        }
    }

    suspend fun getUserProfile(): UserProfileData? = withContext(Dispatchers.IO) {
        try {
            api.getUserProfile()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching user profile: ${e.message}", e)
            null
        }
    }

    suspend fun getProperties(): List<PropertyItem> = withContext(Dispatchers.IO) {
        try {
            api.getProperties()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching properties from backend: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getFeaturedProperty(): PropertyItem? = withContext(Dispatchers.IO) {
        try {
            api.getFeaturedProperty()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching featured property from backend: ${e.message}", e)
            null
        }
    }

    suspend fun getPropertyById(id: String): PropertyItem? = withContext(Dispatchers.IO) {
        try {
            api.getPropertyById(id)
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching property $id: ${e.message}", e)
            null
        }
    }

    suspend fun toggleFavorite(propertyId: String): ToggleFavoriteResponse = withContext(Dispatchers.IO) {
        try {
            api.toggleFavorite(propertyId)
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error toggling favorite on backend: ${e.message}", e)
            ToggleFavoriteResponse(propertyId, true, "Status favorit diperbarui secara lokal")
        }
    }

    suspend fun getDocuments(): List<DocumentItem> = withContext(Dispatchers.IO) {
        try {
            api.getDocuments().documents
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching documents from backend: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun uploadDocument(
        context: Context,
        documentId: String,
        uri: Uri
    ): Result<DocumentItem> = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            var fileName = "berkas_${documentId}.pdf"

            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    val name = cursor.getString(nameIndex)
                    if (!name.isNullOrBlank()) {
                        fileName = name
                    }
                }
            }

            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return@withContext Result.failure(Exception("Gagal membaca file dari penyimpanan"))

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", fileName, requestBody)

            val updatedDoc = api.uploadDocument(documentId, part)
            Result.success(updatedDoc)
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error uploading document $documentId: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getSp3kDetails(): Sp3kDetails? = withContext(Dispatchers.IO) {
        try {
            api.getSp3kDetails()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching SP3K from backend: ${e.message}", e)
            null
        }
    }

    suspend fun scheduleAkad(registrationNumber: String, date: String, location: String): Sp3kDetails? = withContext(Dispatchers.IO) {
        try {
            api.scheduleAkad(
                mapOf(
                    "registrationNumber" to registrationNumber,
                    "akadDate" to date,
                    "akadLocation" to location
                )
            )
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error scheduling akad on backend: ${e.message}", e)
            null
        }
    }

    suspend fun getNotifications(): List<NotificationItem> = withContext(Dispatchers.IO) {
        try {
            api.getNotifications()
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error fetching notifications: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun calculateKpr(
        price: Long,
        dpPercent: Int,
        tenorYears: Int,
        isSyariah: Boolean
    ): KprCalculation? = withContext(Dispatchers.IO) {
        try {
            api.calculateKpr(
                mapOf(
                    "propertyPrice" to price,
                    "dpPercent" to dpPercent,
                    "tenorYears" to tenorYears,
                    "isSyariah" to isSyariah
                )
            )
        } catch (e: Exception) {
            Log.e("NusaRepo", "Error calculating KPR on backend: ${e.message}", e)
            null
        }
    }
}
