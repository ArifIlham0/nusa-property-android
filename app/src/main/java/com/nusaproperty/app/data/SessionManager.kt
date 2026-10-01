package com.nusaproperty.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREF_NAME = "nusa_property_session"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "user_data"
        private const val KEY_PROFILE = "user_profile"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveSession(token: String?, user: UserData?) {
        if (token.isNullOrBlank() || user == null) return

        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER, gson.toJson(user))
            .apply()

        if (user.profile != null) {
            saveProfile(user.profile)
        }

        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
    }

    fun saveProfile(profile: UserProfileData) {
        prefs.edit()
            .putString(KEY_PROFILE, gson.toJson(profile))
            .apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getUser(): UserData? {
        val userJson = prefs.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(userJson, UserData::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun getUserProfile(): UserProfileData? {
        val profileJson = prefs.getString(KEY_PROFILE, null) ?: return null
        return try {
            gson.fromJson(profileJson, UserProfileData::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && !getToken().isNullOrBlank()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
