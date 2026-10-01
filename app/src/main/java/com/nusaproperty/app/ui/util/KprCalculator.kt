package com.nusaproperty.app.ui.util

import com.nusaproperty.app.data.KprCalculation
import kotlin.math.pow
import kotlin.math.roundToLong

object KprCalculator {
    fun calculate(
        price: Long,
        dpPercent: Int,
        tenorYears: Int,
        isSyariah: Boolean
    ): KprCalculation {
        val dpAmount = (price * (dpPercent / 100.0)).roundToLong()
        val loanAmount = (price - dpAmount).coerceAtLeast(0L)
        val rate = if (isSyariah) 0.0515 else 0.0488
        val totalMonths = tenorYears * 12
        val monthlyRate = rate / 12.0

        val monthlyInstallment = if (loanAmount > 0 && monthlyRate > 0) {
            val factor = (1.0 + monthlyRate).pow(totalMonths.toDouble())
            ((loanAmount * monthlyRate * factor) / (factor - 1.0)).roundToLong()
        } else 0L

        val totalPayment = monthlyInstallment * totalMonths
        val totalInterest = (totalPayment - loanAmount).coerceAtLeast(0L)

        val principalPct = if (totalPayment > 0) {
            ((loanAmount.toDouble() / totalPayment) * 100).toFloat()
        } else 60f
        val interestPct = (100f - principalPct).coerceAtLeast(0f)

        val recommendedMinIncome = ((monthlyInstallment / 0.38) / 500_000).roundToLong() * 500_000

        return KprCalculation(
            propertyPrice = price,
            dpPercent = dpPercent,
            dpAmount = dpAmount,
            loanAmount = loanAmount,
            tenorYears = tenorYears,
            isSyariah = isSyariah,
            interestRate = rate,
            monthlyInstallment = monthlyInstallment,
            totalPayment = totalPayment,
            totalInterest = totalInterest,
            principalPercentage = principalPct,
            interestPercentage = interestPct,
            recommendedMinIncome = recommendedMinIncome.coerceAtLeast(5_000_000L)
        )
    }
}
