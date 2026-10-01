package com.nusaproperty.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.DocumentItem
import com.nusaproperty.app.data.DocumentStatus
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.nusaproperty.app.ui.components.DocumentStatusChip
import com.nusaproperty.app.ui.components.ModernPrimaryButton
import com.nusaproperty.app.ui.theme.*

@Composable
fun DocumentUploadScreen(
    onNavigateToStatus: () -> Unit,
    onContactSupport: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { NusaPropertyRepository() }
    var documents by remember { mutableStateOf<List<DocumentItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isUploading by remember { mutableStateOf(false) }
    var activeDocumentId by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val docId = activeDocumentId
        if (uri != null && docId != null) {
            isUploading = true
            onShowMessage("Mengunggah dokumen ke server...")
            coroutineScope.launch {
                val result = repository.uploadDocument(context, docId, uri)
                isUploading = false
                result.fold(
                    onSuccess = { updatedDoc ->
                        documents = documents.map { if (it.id == updatedDoc.id) updatedDoc else it }
                        onShowMessage("Berhasil mengunggah ${updatedDoc.title}!")
                    },
                    onFailure = { err ->
                        onShowMessage("Gagal mengunggah berkas: ${err.localizedMessage ?: "Terjadi kesalahan"}")
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        documents = repository.getDocuments()
        isLoading = false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "stepperPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 120.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(SurfaceContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = PrimaryNavy,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Berkas & Verifikasi KPR",
                            style = MaterialTheme.typography.headlineSmall,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tahap 2 dari 4 persetujuan instan",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = { onShowMessage("Panduan: Pastikan dokumen terbaca jelas dan tidak buram.") },
                    modifier = Modifier
                        .size(38.dp)
                        .background(SurfaceContainerLow, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Bantuan",
                        tint = PrimaryNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
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
                            text = "Status Pengajuan",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
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
                                text = "50% Selesai",
                                style = MaterialTheme.typography.labelMedium,
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepperNode(
                            stepNumber = 1,
                            title = "Data Diri",
                            isCompleted = true,
                            isActive = false
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .background(StatusSuccess)
                        )

                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .scale(pulseScale)
                                    .background(PrimaryFixed.copy(alpha = 0.5f), CircleShape)
                            )
                            StepperNode(
                                stepNumber = 2,
                                title = "Upload Berkas",
                                isCompleted = false,
                                isActive = true
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .background(SurfaceContainerHigh)
                        )

                        StepperNode(
                            stepNumber = 3,
                            title = "Verifikasi",
                            isCompleted = false,
                            isActive = false
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .background(SurfaceContainerHigh)
                        )

                        StepperNode(
                            stepNumber = 4,
                            title = "Keputusan",
                            isCompleted = false,
                            isActive = false
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryFixed.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(PrimaryNavy.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = PrimaryNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Unggah dokumen asli dengan pencahayaan jelas untuk mempercepat proses persetujuan (maks 24 jam kerja).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Dokumen Wajib",
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                    val uploadedCount = documents.count { it.status == DocumentStatus.UPLOADED || it.status == DocumentStatus.VERIFIED }
                    Text(
                        text = "$uploadedCount dari ${documents.size} Diunggah",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier
                            .background(SurfaceContainer, RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryNavy)
                    }
                } else if (documents.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada dokumen yang perlu diunggah saat ini.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    if (isUploading) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = PrimaryFixed.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(color = PrimaryNavy, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                                Text(
                                    text = "Sedang mengunggah dokumen ke server...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryNavy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    documents.forEach { doc ->
                        val icon = when {
                            doc.title.contains("KTP", ignoreCase = true) || doc.title.contains("NPWP", ignoreCase = true) -> Icons.Default.Person
                            doc.title.contains("Slip", ignoreCase = true) -> Icons.Default.DateRange
                            doc.title.contains("Rekening", ignoreCase = true) -> Icons.Default.Home
                            else -> Icons.Default.DateRange
                        }
                        DocumentCard(
                            icon = icon,
                            title = doc.title,
                            subtitle = doc.description,
                            status = doc.status,
                            statusLabel = doc.statusLabel,
                            fileName = doc.fileName,
                            fileSubtitle = doc.fileMeta,
                            actionText = doc.actionLabel,
                            onActionClick = {
                                activeDocumentId = doc.id
                                filePickerLauncher.launch("*/*")
                            }
                        )
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
                        Box(
                            modifier = Modifier
                                .size(44.dp)
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Butuh Panduan Dokumen?",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Konsultan NusaProperty siap membantu via WhatsApp",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        IconButton(
                            onClick = onContactSupport,
                            modifier = Modifier
                                .size(38.dp)
                                .background(StatusSuccess.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Chat",
                                tint = StatusSuccess,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = PrimaryNavy,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Data Anda dienkripsi 256-bit AES & diawasi oleh OJK.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = SurfaceCard.copy(alpha = 0.98f),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress Kelengkapan Berkas",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "2 / 4 Dokumen",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                }

                ModernPrimaryButton(
                    text = "Kirim Dokumen & Lanjut ke Verifikasi",
                    onClick = onNavigateToStatus,
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                )
            }
        }
    }
}

@Composable
fun StepperNode(
    stepNumber: Int,
    title: String,
    isCompleted: Boolean,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val (bgColor, contentColor) = when {
            isCompleted -> StatusSuccess to OnPrimary
            isActive -> PrimaryNavy to OnPrimary
            else -> SurfaceContainerHigh to TextSecondary
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
            } else if (isActive) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive || isCompleted) PrimaryNavy else TextSecondary,
            fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Medium,
            fontSize = 10.sp
        )
    }
}

@Composable
fun DocumentCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    status: DocumentStatus? = DocumentStatus.REQUIRED,
    statusLabel: String,
    fileName: String? = null,
    fileSubtitle: String? = null,
    actionText: String,
    onActionClick: () -> Unit
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
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(SurfaceContainerLow, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = PrimaryNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                DocumentStatusChip(status = status, label = statusLabel)
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerLow.copy(alpha = 0.7f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(StatusInfo.copy(alpha = 0.12f), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = StatusInfo,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = fileName ?: "Belum ada file diunggah",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (fileName != null) TextPrimary else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = fileSubtitle ?: "Format JPG, PNG, atau PDF",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                FilledTonalButton(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = SurfaceCard,
                        contentColor = PrimaryNavy
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
