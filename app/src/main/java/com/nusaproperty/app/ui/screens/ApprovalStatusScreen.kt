package com.nusaproperty.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.SampleData
import com.nusaproperty.app.data.Sp3kDetails
import com.nusaproperty.app.ui.components.ModernPrimaryButton
import com.nusaproperty.app.ui.components.ModernSecondaryButton
import com.nusaproperty.app.ui.theme.*

@Composable
fun ApprovalStatusScreen(
    sp3k: Sp3kDetails = SampleData.sampleSp3k,
    onScheduleAkad: () -> Unit,
    onDownloadPdf: () -> Unit,
    onCallAdvisor: () -> Unit,
    onChatAdvisor: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                PrimaryNavy.copy(alpha = 0.08f),
                                SurfaceContainerLow,
                                SurfaceCard
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = SecondaryAmber.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(24.dp)
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = AccentGold.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(26.dp)
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = StatusSuccess.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(22.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .background(AccentGold.copy(alpha = 0.20f), CircleShape)
                        )

                        Card(
                            modifier = Modifier.size(width = 64.dp, height = 76.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = PrimaryNavy,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .background(StatusSuccess, CircleShape)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.7f)
                                            .height(3.dp)
                                            .background(SurfaceContainerHigh, RoundedCornerShape(2.dp))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .background(SurfaceContainerHigh, RoundedCornerShape(2.dp))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .height(3.dp)
                                            .background(SurfaceContainerHigh, RoundedCornerShape(2.dp))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .size(18.dp)
                                        .background(SecondaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = OnSecondaryFixedVariant,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(listOf(SecondaryAmber, AccentGold)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .background(StatusSuccess, RoundedCornerShape(50))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = "Approved",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "Selamat! Pengajuan KPR Anda Telah Disetujui 🎉",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "Surat Penegasan Persetujuan Penyediaan Kredit (SP3K) resmi telah diterbitkan oleh bank rekanan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(PrimaryFixed, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "DOKUMEN RESMI SP3K",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "No: ${sp3k.registrationNumber}",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .background(SurfaceContainer, RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(StatusSuccess, CircleShape)
                        )
                        Text(
                            text = "Valid",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLow)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Developer Mitra:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(sp3k.developer, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Unit Terpilih:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(sp3k.unitName, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryNavy)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PLAFON KREDIT DISETUJUI",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryFixed,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = SampleData.formatRupiah(sp3k.approvedAmount),
                            style = MaterialTheme.typography.headlineLarge,
                            color = OnPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Penyaluran dana siap diagendakan saat penandatanganan akad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryFixedDim
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            label = "Suku Bunga",
                            value = "4.88% p.a.",
                            sub = "Fixed 3 Tahun",
                            subColor = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Angsuran Bulanan",
                            value = SampleData.formatRupiah(sp3k.monthlyInstallment),
                            sub = "Estimasi Flat",
                            subColor = TextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            label = "Tenor Pinjaman",
                            value = "${sp3k.tenorYears} Tahun",
                            sub = "(${sp3k.tenorYears * 12} Bulan)",
                            subColor = TextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Uang Muka (DP)",
                            value = SampleData.formatRupiah(sp3k.dpPaid),
                            sub = "Lunas Terverifikasi",
                            subColor = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tahapan Menuju Rumah Impian",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Langkah 2 dari 3",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                TimelineStepRow(
                    stepNumber = 1,
                    title = "Verifikasi Data & Dokumen Akhir",
                    desc = "Seluruh berkas finansial dan identitas telah tervalidasi oleh analis kredit perbankan.",
                    status = StepStatusType.FINISHED,
                    badgeText = "Selesai"
                )

                TimelineStepRow(
                    stepNumber = 2,
                    title = "Pemilihan Jadwal Akad Kredit",
                    desc = "Pilih tanggal dan lokasi kantor cabang bank atau notaris untuk tanda tangan basah.",
                    status = StepStatusType.ACTIVE,
                    badgeText = "Langkah Selanjutnya"
                )

                TimelineStepRow(
                    stepNumber = 3,
                    title = "Pembayaran Biaya Administrasi & Notaris",
                    desc = "Pelunasan biaya asuransi jiwa, kebakaran, dan legalitas notaris di hari penandatanganan.",
                    status = StepStatusType.UPCOMING,
                    badgeText = "Menunggu Akad"
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
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
                        text = "DEDICATED KPR SPECIALIST",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(StatusSuccess, CircleShape)
                        )
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(PrimaryFixed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Rian Anggara",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Senior Mortgage Advisor",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = "Bank Mandiri Rekanan",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryNavy,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onCallAdvisor,
                            modifier = Modifier
                                .size(40.dp)
                                .background(SurfaceContainer, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Telepon",
                                tint = PrimaryNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onChatAdvisor,
                            modifier = Modifier
                                .size(40.dp)
                                .background(StatusSuccess.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Chat",
                                tint = StatusSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ModernPrimaryButton(
                text = "Pilih Jadwal Akad Kredit",
                onClick = onScheduleAkad,
                leadingIcon = Icons.Default.DateRange
            )

            ModernSecondaryButton(
                text = "Unduh Surat Penawaran Kredit (SP3K / PDF)",
                onClick = onDownloadPdf,
                leadingIcon = Icons.Default.ArrowDropDown
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = StatusSuccess,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dokumen sah terenkripsi 256-bit dan diawasi oleh OJK.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    }
}

enum class StepStatusType {
    FINISHED, ACTIVE, UPCOMING
}

@Composable
fun TimelineStepRow(
    stepNumber: Int,
    title: String,
    desc: String,
    status: StepStatusType,
    badgeText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        val (bgColor, iconColor) = when (status) {
            StepStatusType.FINISHED -> StatusSuccess to OnPrimary
            StepStatusType.ACTIVE -> PrimaryNavy to OnPrimary
            StepStatusType.UPCOMING -> SurfaceContainer to TextSecondary
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(bgColor, CircleShape)
                .then(
                    if (status == StepStatusType.ACTIVE) {
                        Modifier.border(3.dp, PrimaryFixed, CircleShape)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (status == StepStatusType.FINISHED) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            } else if (status == StepStatusType.ACTIVE) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = iconColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (status == StepStatusType.ACTIVE) PrimaryNavy else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = when (status) {
                        StepStatusType.FINISHED -> StatusSuccess
                        StepStatusType.ACTIVE -> SecondaryBrown
                        StepStatusType.UPCOMING -> TextSecondary
                    },
                    modifier = Modifier
                        .background(
                            when (status) {
                                StepStatusType.FINISHED -> StatusSuccess.copy(alpha = 0.12f)
                                StepStatusType.ACTIVE -> SecondaryFixed
                                StepStatusType.UPCOMING -> SurfaceContainer
                            },
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    sub: String,
    subColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
            Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(sub, style = MaterialTheme.typography.labelSmall, color = subColor, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
        }
    }
}
