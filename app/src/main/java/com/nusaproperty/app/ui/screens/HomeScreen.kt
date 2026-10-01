package com.nusaproperty.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.PropertyItem
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import com.nusaproperty.app.ui.components.PropertyHeroGraphic
import com.nusaproperty.app.ui.components.PropertyTagBadge
import com.nusaproperty.app.ui.theme.*
import com.nusaproperty.app.data.UserProfileData
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    userName: String = "Dimas Nugraha",
    onNavigateToCalculator: () -> Unit,
    onNavigateToPipeline: () -> Unit,
    onNavigateToProperty: (PropertyItem?) -> Unit,
    onNavigateToStatus: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val repository = remember { NusaPropertyRepository() }
    val coroutineScope = rememberCoroutineScope()
    var properties by remember { mutableStateOf<List<PropertyItem>>(emptyList()) }
    var userProfile by remember { mutableStateOf<UserProfileData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        isLoading = true
        properties = repository.getProperties()
        val prof = repository.getUserProfile()
        if (prof != null) {
            userProfile = prof
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Halo, $userName",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = userProfile?.subtitle ?: "Selamat pagi, wujudkan rumah impianmu hari ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(SecondaryFixed.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = SecondaryBrown,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .offset(x = 180.dp, y = (-20).dp)
                        .background(AccentGold.copy(alpha = 0.12f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .offset(x = (-40).dp, y = 80.dp)
                        .background(StatusSuccess.copy(alpha = 0.12f), CircleShape)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "KPR READINESS & STATUS",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .background(StatusSuccess.copy(alpha = 0.25f), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(StatusSuccess, CircleShape)
                            )
                            Text(
                                text = "Aktif",
                                style = MaterialTheme.typography.labelSmall,
                                color = TertiaryFixed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Plafon Estimasi KPR",
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryFixedDim
                        )
                        Text(
                            text = userProfile?.plafonEstimateFormatted ?: "Rp 650.000.000",
                            style = MaterialTheme.typography.headlineLarge,
                            color = OnPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Skor Finansial: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryFixed
                            )
                            Text(
                                text = userProfile?.financialScore ?: "Sangat Baik (A+)",
                                style = MaterialTheme.typography.bodySmall,
                                color = AccentGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToCalculator,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryContainer,
                            contentColor = OnSecondaryContainer
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Cek Kelayakan KPR",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionItem(
                title = "Simulasi KPR",
                icon = Icons.Default.DateRange,
                bgColor = PrimaryFixed.copy(alpha = 0.5f),
                iconColor = PrimaryNavy,
                onClick = onNavigateToCalculator
            )
            QuickActionItem(
                title = "Upload Dokumen",
                icon = Icons.AutoMirrored.Filled.List,
                bgColor = SecondaryFixed.copy(alpha = 0.5f),
                iconColor = SecondaryBrown,
                onClick = onNavigateToPipeline
            )
            QuickActionItem(
                title = "Jelajah Cluster",
                icon = Icons.Default.LocationOn,
                bgColor = TertiaryFixed.copy(alpha = 0.7f),
                iconColor = TertiaryTeal,
                onClick = { onNavigateToProperty(properties.firstOrNull()) }
            )
            QuickActionItem(
                title = "Konsultasi Agen",
                icon = Icons.Default.Person,
                bgColor = SurfaceContainerHigh,
                iconColor = OnSurfaceVariant,
                onClick = onNavigateToStatus
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cluster Hunian Pilihan Klien",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { onNavigateToProperty(properties.firstOrNull()) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatusInfo,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = StatusInfo,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PrimaryNavy,
                    modifier = Modifier.size(36.dp)
                )
            }
        } else if (properties.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        text = "Belum ada cluster properti yang tersedia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(properties, key = { it.id }) { property ->
                    PropertyCard(
                        property = property,
                        onPropertyClick = { onNavigateToProperty(property) },
                        onToggleFavorite = {
                            val targetFav = !property.isFavorite
                            properties = properties.map {
                                if (it.id == property.id) it.copy(isFavorite = targetFav) else it
                            }
                            coroutineScope.launch {
                                repository.toggleFavorite(property.id)
                            }
                            onShowMessage(
                                if (targetFav) "Disimpan ke favorit: ${property.title}"
                                else "Dihapus dari favorit"
                            )
                        }
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLow),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryFixed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = OnPrimaryFixedVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tips Lolos KPR Bank",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Jaga BI Checking / SLIK OJK tetap bersih dan rasio cicilan maksimal 40% penghasilan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            lineHeight = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun PropertyCard(
    property: PropertyItem,
    onPropertyClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onPropertyClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                PropertyHeroGraphic(
                    modifier = Modifier.fillMaxSize(),
                    title = property.location
                )

                PropertyTagBadge(
                    tagType = property.tagType,
                    text = property.tagText,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                )

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .background(SurfaceCard.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (property.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Simpan ke favorit",
                        tint = if (property.isFavorite) StatusError else TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SecondaryAmber,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = property.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    SpecBadge(icon = Icons.Default.Home, text = "${property.bedrooms} KT")
                    SpecBadge(icon = Icons.Default.CheckCircle, text = "${property.bathrooms} KM")
                }

                Spacer(modifier = Modifier.height(4.dp))
                
                Column {
                    Text(
                        text = property.priceFormatted,
                        style = MaterialTheme.typography.headlineSmall,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = property.installmentEstimate,
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SpecBadge(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .background(SurfaceLow, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
