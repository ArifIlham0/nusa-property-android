package com.nusaproperty.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.SessionManager
import com.nusaproperty.app.data.UserData
import com.nusaproperty.app.data.UserProfileData
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import com.nusaproperty.app.ui.theme.*

@Composable
fun ProfileScreen(
    user: UserData?,
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { NusaPropertyRepository() }
    val sessionManager = remember { SessionManager.getInstance(context) }
    var currentProfile by remember { mutableStateOf<UserProfileData?>(user?.profile ?: sessionManager.getUserProfile()) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val liveProfile = repository.getUserProfile()
        if (liveProfile != null) {
            currentProfile = liveProfile
            sessionManager.saveProfile(liveProfile)
        }
    }

    val displayName = user?.fullName ?: currentProfile?.name ?: "Pengguna Nusa"
    val displayEmail = user?.email ?: "email@nusaproperty.com"
    val displayPhone = user?.phone ?: "Belum diatur"
    val initial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "U"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(SurfaceContainer, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = PrimaryNavy
                )
            }
            Text(
                text = "Profil Pengguna",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(PrimaryNavy, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.headlineLarge,
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = displayEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Row(
                    modifier = Modifier
                        .background(StatusSuccess.copy(alpha = 0.15f), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Akun Terverifikasi",
                        style = MaterialTheme.typography.labelSmall,
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "STATUS KELAYAKAN KPR",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryFixed,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = currentProfile?.plafonEstimateFormatted ?: "Rp 650.000.000",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnPrimary,
                    fontWeight = FontWeight.ExtraBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Skor Finansial: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryFixed
                    )
                    Text(
                        text = currentProfile?.financialScore ?: "Sangat Baik (A+)",
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentGold,
                        fontWeight = FontWeight.Bold
                    )
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Informasi Akun",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                ProfileInfoRow(label = "Nama Lengkap", value = displayName)
                ProfileInfoRow(label = "Email", value = displayEmail)
                ProfileInfoRow(label = "Nomor Telepon", value = displayPhone)
                ProfileInfoRow(label = "Status BI Checking", value = "SLIK OJK Kolektibilitas 1 (Lancar)")
            }
        }
        Button(
            onClick = {
                sessionManager.logout()
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StatusError.copy(alpha = 0.12f),
                contentColor = StatusError
            )
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Keluar dari Akun", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
