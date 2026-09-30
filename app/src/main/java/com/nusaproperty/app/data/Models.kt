package com.nusaproperty.app.data

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
    val priceFormatted: String,
    val installmentEstimate: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val carports: Int = 1,
    val buildingArea: Int = 36,
    val surfaceArea: Int = 60,
    val electricityVa: Int = 1300,
    val certificateType: String = "SHM",
    val tagText: String,
    val tagType: PropertyTagType,
    val imageUrl: String,
    val developerName: String = "Harmoni Land Group (Verified Partner)",
    val addressDetail: String = "Jl. Raya Serang - Cibarusah, Cikarang Selatan",
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
    val statusLabel: String,
    val fileName: String? = null,
    val fileMeta: String? = null,
    val actionLabel: String
)

data class Sp3kDetails(
    val registrationNumber: String,
    val developer: String,
    val unitName: String,
    val approvedAmount: Long,
    val interestRateText: String,
    val monthlyInstallment: Long,
    val tenorYears: Int,
    val dpPaid: Long
)

data class StepItem(
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val status: StepStatus,
    val statusBadgeText: String? = null
)
