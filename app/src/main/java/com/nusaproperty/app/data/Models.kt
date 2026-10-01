package com.nusaproperty.app.data

import com.google.gson.annotations.SerializedName

enum class PropertyTagType {
    SUBSIDI, PROMO, DISCOUNT
}

enum class DocumentStatus {
    VERIFIED,
    UPLOADED,
    REQUIRED,
    NOT_UPLOADED
}

enum class StepStatus {
    COMPLETED,
    ACTIVE,
    UPCOMING
}

data class PropertyItem(
    val id: String,
    val title: String,
    val location: String,
    val price: Long,
    @SerializedName(value = "priceFormatted", alternate = ["price_formatted"])
    val priceFormatted: String,
    @SerializedName(value = "installmentEstimate", alternate = ["installment_estimate"])
    val installmentEstimate: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val carports: Int = 1,
    @SerializedName(value = "buildingArea", alternate = ["building_area"])
    val buildingArea: Int = 36,
    @SerializedName(value = "surfaceArea", alternate = ["surface_area"])
    val surfaceArea: Int = 60,
    @SerializedName(value = "electricityVa", alternate = ["electricity_va"])
    val electricityVa: Int = 1300,
    @SerializedName(value = "certificateType", alternate = ["certificate_type"])
    val certificateType: String = "SHM",
    @SerializedName(value = "tagText", alternate = ["tag_text"])
    val tagText: String,
    @SerializedName(value = "tagType", alternate = ["tag_type"])
    val tagType: PropertyTagType,
    @SerializedName(value = "imageUrl", alternate = ["image_url"])
    val imageUrl: String,
    @SerializedName(value = "developerName", alternate = ["developer_name"])
    val developerName: String = "Harmoni Land Group (Verified Partner)",
    @SerializedName(value = "addressDetail", alternate = ["address_detail"])
    val addressDetail: String = "Jl. Raya Serang - Cibarusah, Cikarang Selatan",
    @SerializedName(value = "isFavorite", alternate = ["is_favorite"])
    val isFavorite: Boolean = false
)

data class KprCalculation(
    val propertyPrice: Long,
    val dpPercent: Int,
    val dpAmount: Long,
    val loanAmount: Long,
    val tenorYears: Int,
    val isSyariah: Boolean,
    val interestRate: Double,
    val monthlyInstallment: Long,
    val totalPayment: Long,
    val totalInterest: Long,
    val principalPercentage: Float,
    val interestPercentage: Float,
    val recommendedMinIncome: Long
)

data class DocumentItem(
    val id: String,
    val title: String,
    val description: String,
    val status: DocumentStatus,
    @SerializedName(value = "statusLabel", alternate = ["status_label"])
    val statusLabel: String,
    @SerializedName(value = "fileName", alternate = ["file_name"])
    val fileName: String? = null,
    @SerializedName(value = "fileMeta", alternate = ["file_meta"])
    val fileMeta: String? = null,
    @SerializedName(value = "actionLabel", alternate = ["action_label"])
    val actionLabel: String
)

data class DocumentsResponse(
    val totalDocuments: Int,
    val uploadedCount: Int,
    val summaryText: String,
    val completionPercent: Int,
    val documents: List<DocumentItem>
)

data class MortgageAdvisor(
    val name: String,
    val role: String,
    val bank: String,
    val phone: String,
    @SerializedName(value = "isOnline", alternate = ["is_online"])
    val isOnline: Boolean = true
)

data class Sp3kDetails(
    @SerializedName(value = "registrationNumber", alternate = ["registration_number"])
    val registrationNumber: String,
    val developer: String,
    @SerializedName(value = "unitName", alternate = ["unit_name"])
    val unitName: String,
    @SerializedName(value = "approvedAmount", alternate = ["approved_amount"])
    val approvedAmount: Long,
    @SerializedName(value = "interestRateText", alternate = ["interest_rate_text"])
    val interestRateText: String,
    @SerializedName(value = "monthlyInstallment", alternate = ["monthly_installment"])
    val monthlyInstallment: Long,
    @SerializedName(value = "tenorYears", alternate = ["tenor_years"])
    val tenorYears: Int,
    @SerializedName(value = "dpPaid", alternate = ["dp_paid"])
    val dpPaid: Long,
    @SerializedName(value = "akadDate", alternate = ["akad_date"])
    val akadDate: String? = null,
    @SerializedName(value = "akadLocation", alternate = ["akad_location"])
    val akadLocation: String? = null,
    val steps: List<StepItem> = emptyList(),
    val advisor: MortgageAdvisor? = null
)

data class StepItem(
    @SerializedName(value = "stepNumber", alternate = ["step_number"])
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val status: StepStatus,
    @SerializedName(value = "statusBadgeText", alternate = ["status_badge_text"])
    val statusBadgeText: String? = null
)

data class ToggleFavoriteResponse(
    val id: String,
    val isFavorite: Boolean,
    val message: String
)

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val type: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    @SerializedName("fullName")
    val fullName: String,
    val phone: String? = null
)

data class UserProfileData(
    val id: String,
    val name: String,
    val greeting: String? = "Halo",
    val subtitle: String? = null,
    @SerializedName("plafonEstimate")
    val plafonEstimate: Long = 0,
    @SerializedName("plafonEstimateFormatted")
    val plafonEstimateFormatted: String? = null,
    @SerializedName("financialScore")
    val financialScore: String? = null,
    @SerializedName("financialScoreGrade")
    val financialScoreGrade: String? = null
)

data class UserData(
    val id: String,
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    val phone: String? = null,
    val profile: UserProfileData? = null
)

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("tokenType")
    val tokenType: String = "bearer",
    val user: UserData
)
