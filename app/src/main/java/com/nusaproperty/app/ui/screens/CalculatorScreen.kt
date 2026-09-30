package com.nusaproperty.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.SampleData
import com.nusaproperty.app.ui.components.ModernPrimaryButton
import com.nusaproperty.app.ui.components.PropertyHeroGraphic
import com.nusaproperty.app.ui.theme.*

@Composable
fun CalculatorScreen(
    initialPrice: Long = 450_000_000L,
    onApplyKpr: (Long, Int, Int, Boolean) -> Unit,
    onShowInfoDialog: () -> Unit
) {
    var propertyPrice by remember { mutableLongStateOf(initialPrice) }
    var dpPercent by remember { mutableIntStateOf(10) }
    var tenorYears by remember { mutableIntStateOf(20) }
    var isSyariah by remember { mutableStateOf(false) }

    val calculation = remember(propertyPrice, dpPercent, tenorYears, isSyariah) {
        SampleData.calculateKpr(propertyPrice, dpPercent, tenorYears, isSyariah)
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Kalkulator KPR Pintar",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "✨",
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = "Hitung simulasi cicilan bulanan & batas kemampuan kreditmu",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(
                    onClick = onShowInfoDialog,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceContainer, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informasi Kalkulator",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    val konvenBg by animateColorAsState(
                        targetValue = if (!isSyariah) SurfaceCard else Color.Transparent,
                        label = "konvenBg"
                    )
                    val konvenText by animateColorAsState(
                        targetValue = if (!isSyariah) PrimaryNavy else TextSecondary,
                        label = "konvenText"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(konvenBg)
                            .clickable { isSyariah = false }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "KPR Konvensional",
                            style = MaterialTheme.typography.labelMedium,
                            color = konvenText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Fixed & Floating",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    val syariahBg by animateColorAsState(
                        targetValue = if (isSyariah) SurfaceCard else Color.Transparent,
                        label = "syariahBg"
                    )
                    val syariahText by animateColorAsState(
                        targetValue = if (isSyariah) PrimaryNavy else TextSecondary,
                        label = "syariahText"
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(syariahBg)
                            .clickable { isSyariah = true }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "KPR Syariah",
                            style = MaterialTheme.typography.labelMedium,
                            color = syariahText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Margin Flat",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Harga Properti",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Plafon Maksimal",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                modifier = Modifier
                                    .background(SurfaceContainer, RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceLow, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Rp",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = SampleData.formatRupiah(propertyPrice).replace("Rp ", ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                200_000_000L to "Rp 200Jt",
                                450_000_000L to "Rp 450Jt",
                                750_000_000L to "Rp 750Jt",
                                1_200_000_000L to "Rp 1,2M"
                            ).forEach { (amt, label) ->
                                val isSelected = propertyPrice == amt
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { propertyPrice = amt },
                                    label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryFixed,
                                        selectedLabelColor = PrimaryNavy,
                                        containerColor = SurfaceContainer,
                                        labelColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Uang Muka (DP)",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${SampleData.formatRupiah(calculation.dpAmount)} ($dpPercent%)",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Slider(
                            value = dpPercent.toFloat(),
                            onValueChange = { dpPercent = (it / 5).toInt() * 5 },
                            valueRange = 10f..50f,
                            steps = 7,
                            colors = SliderDefaults.colors(
                                thumbColor = SecondaryAmber,
                                activeTrackColor = PrimaryNavy,
                                inactiveTrackColor = SurfaceContainerHigh
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Min. 10%", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("30%", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("Maks. 50%", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(10, 20, 30, 40).forEach { p ->
                                val isSelected = dpPercent == p
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(if (isSelected) PrimaryFixed else SurfaceContainer)
                                        .clickable { dpPercent = p }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$p%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) PrimaryNavy else TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Jangka Waktu (Tenor)",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$tenorYears Tahun (${tenorYears * 12} Bulan)",
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusSuccess,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(10, 15, 20, 25).forEach { yr ->
                                val isSelected = tenorYears == yr
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) PrimaryContainer else SurfaceContainer)
                                        .clickable { tenorYears = yr }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$yr Thn",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) OnPrimary else TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Suku Bunga Promo",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "NusaProperty Special",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSecondaryFixedVariant,
                                modifier = Modifier
                                    .background(SecondaryFixed, RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceLow, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(PrimaryNavy.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = PrimaryNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (isSyariah) "5.15% Margin Flat" else "4.88% Fixed",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isSyariah) "Seluruh Masa Pembiayaan" else "3 Tahun Pertama",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isSyariah) {
                                    "Akad Murabahah dengan margin tetap tanpa risiko fluktuasi bunga pasar."
                                } else {
                                    "Setelah masa fixed berakhir, berlaku estimasi suku bunga floating ~10.5% p.a. (dapat berubah mengikuti BI rate)."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ESTIMASI ANGSURAN BULANAN",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnPrimaryContainer,
                                letterSpacing = 0.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isSyariah) "Flat Tenor" else "Tahun 1 - 3",
                                style = MaterialTheme.typography.labelSmall,
                                color = TertiaryFixed,
                                modifier = Modifier
                                    .background(StatusSuccess.copy(alpha = 0.25f), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = SampleData.formatRupiah(calculation.monthlyInstallment),
                                style = MaterialTheme.typography.headlineLarge,
                                color = OnPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "/bulan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnPrimaryContainer,
                                modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                            )
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.10f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Komposisi Angsuran",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnPrimaryContainer
                                    )
                                    Text(
                                        text = "Total Nilai Pinjaman",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                val principalWeight by animateFloatAsState(
                                    targetValue = calculation.principalPercentage / 100f,
                                    label = "principalWeight"
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.15f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(principalWeight.coerceIn(0.1f, 0.9f))
                                            .background(StatusSuccess)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight((1f - principalWeight).coerceIn(0.1f, 0.9f))
                                            .background(SecondaryContainer)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(StatusSuccess, CircleShape)
                                        )
                                        Column {
                                            Text(
                                                text = "Pokok Pinjaman",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = OnPrimaryContainer
                                            )
                                            Text(
                                                text = SampleData.formatRupiah(calculation.loanAmount),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = OnPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(SecondaryContainer, CircleShape)
                                        )
                                        Column {
                                            Text(
                                                text = "Bunga & Biaya",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = OnPrimaryContainer
                                            )
                                            Text(
                                                text = SampleData.formatRupiah(calculation.totalInterest),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = AccentGold,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "💡", fontSize = 18.sp)
                            Column {
                                Text(
                                    text = "Penghasilan Minimum yang Disarankan",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = OnPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${SampleData.formatRupiah(calculation.recommendedMinIncome)}/bln (Rasio angsuran 39% aman DSR perbankan nasional).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnPrimaryContainer,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PropertyHeroGraphic(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            title = "Depok"
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PILIHAN POPULER",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Cluster Harmoni Emerald",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Depok, Jawa Barat • Tipe 45/90",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(SurfaceContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
        
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = SurfaceCard.copy(alpha = 0.96f),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ModernPrimaryButton(
                    text = "Ajukan KPR Untuk Tipe Ini",
                    onClick = {
                        onApplyKpr(propertyPrice, dpPercent, tenorYears, isSyariah)
                    },
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Gratis konsultasi & simulasi 12+ bank rekanan",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
