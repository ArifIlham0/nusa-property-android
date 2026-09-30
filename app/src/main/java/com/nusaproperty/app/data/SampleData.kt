package com.nusaproperty.app.data

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToLong

object SampleData {

    fun formatRupiah(amount: Long): String {
        val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp ${format.format(amount)}"
    }

    val sampleProperties = listOf(
        PropertyItem(
            id = "prop_1",
            title = "Cluster Harmoni Asri - Tipe 36/60",
            location = "Bekasi Timur",
            price = 185_000_000L,
            priceFormatted = "Rp 185.000.000",
            installmentEstimate = "Cicilan mulai Rp 1,1 Jt/bln",
            bedrooms = 2,
            bathrooms = 1,
            carports = 1,
            buildingArea = 36,
            surfaceArea = 60,
            tagText = "Subsidi / FLPP",
            tagType = PropertyTagType.SUBSIDI,
            imageUrl = "https://images.unsplash.com/photo-1570129477492-45c003edd2be?auto=format&fit=crop&w=800&q=80",
            isFavorite = false
        ),
        PropertyItem(
            id = "prop_2",
            title = "Grand Tana Verde - Tipe 45/84",
            location = "Cikarang Barat",
            price = 420_000_000L,
            priceFormatted = "Rp 420.000.000",
            installmentEstimate = "Cicilan mulai Rp 2,7 Jt/bln",
            bedrooms = 3,
            bathrooms = 2,
            carports = 1,
            buildingArea = 45,
            surfaceArea = 84,
            tagText = "Promo DP 0%",
            tagType = PropertyTagType.PROMO,
            imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80",
            isFavorite = true
        ),
        PropertyItem(
            id = "prop_3",
            title = "Puri Botanical Garden - Tipe 54/90",
            location = "Serpong Selatan",
            price = 680_000_000L,
            priceFormatted = "Rp 680.000.000",
            installmentEstimate = "Cicilan mulai Rp 4,3 Jt/bln",
            bedrooms = 3,
            bathrooms = 2,
            carports = 2,
            buildingArea = 54,
            surfaceArea = 90,
            tagText = "Diskon Biaya Akad",
            tagType = PropertyTagType.DISCOUNT,
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=800&q=80",
            isFavorite = false
        )
    )

    val featuredProperty = PropertyItem(
        id = "prop_featured",
        title = "Cluster Harmoni Botanical - Tipe 36/72",
        location = "Jl. Raya Serang - Cibarusah, Cikarang Selatan",
        price = 375_000_000L,
        priceFormatted = "Rp 375.000.000",
        installmentEstimate = "Cicilan est. Rp 2,3 Jt/bln",
        bedrooms = 2,
        bathrooms = 1,
        carports = 1,
        buildingArea = 36,
        surfaceArea = 72,
        electricityVa = 1300,
        certificateType = "SHM",
        tagText = "Subsidi Eligible",
        tagType = PropertyTagType.SUBSIDI,
        imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=1200&q=80",
        developerName = "Harmoni Land Group (Verified Partner)",
        addressDetail = "Jl. Raya Serang - Cibarusah, Cikarang Selatan",
        isFavorite = false
    )

    val initialDocuments = listOf(
        DocumentItem(
            id = "doc_1",
            title = "e-KTP & NPWP Pribadi",
            description = "Format foto e-KTP & kartu NPWP fisik",
            status = DocumentStatus.VERIFIED,
            statusLabel = "Terverifikasi",
            fileName = "ktp_npwp_final.jpg",
            fileMeta = "Ditinjau otomatis AI",
            actionLabel = "Lihat Berkas"
        ),
        DocumentItem(
            id = "doc_2",
            title = "Slip Gaji (3 Bulan Terakhir)",
            description = "Slip resmi berstempel perusahaan / e-payslip",
            status = DocumentStatus.UPLOADED,
            statusLabel = "Upload Berhasil",
            fileName = "SlipGaji_Okt-Des2024.pdf",
            fileMeta = "Ukuran berkas 2.4 MB",
            actionLabel = "Ganti"
        ),
        DocumentItem(
            id = "doc_3",
            title = "Rekening Koran Operasional (3 Bulan Terakhir)",
            description = "Mutasi buku tabungan bank payroll / operasional",
            status = DocumentStatus.REQUIRED,
            statusLabel = "Dibutuhkan",
            fileName = null,
            fileMeta = null,
            actionLabel = "Pilih Berkas"
        ),
        DocumentItem(
            id = "doc_4",
            title = "Surat Keterangan Kerja (SK Kerja Aktif)",
            description = "Masa kerja minimal 1 tahun karyawan tetap / kontrak",
            status = DocumentStatus.NOT_UPLOADED,
            statusLabel = "Belum Diunggah",
            fileName = null,
            fileMeta = null,
            actionLabel = "Unggah SK"
        )
    )

    val sampleSp3k = Sp3kDetails(
        registrationNumber = "KPR-2026-NUSA-0918",
        developer = "Harmoni Land Group",
        unitName = "Cluster Botanical A-12 (Tipe 36/72)",
        approvedAmount = 350_000_000L,
        interestRateText = "4.88% p.a. Fixed 3 Tahun",
        monthlyInstallment = 2_740_000L,
        tenorYears = 20,
        dpPaid = 25_000_000L
    )

    fun calculateKpr(
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
