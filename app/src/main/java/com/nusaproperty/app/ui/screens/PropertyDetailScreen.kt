package com.nusaproperty.app.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.PropertyItem
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import com.nusaproperty.app.ui.components.PropertyHeroGraphic
import com.nusaproperty.app.ui.theme.*
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PropertyDetailScreen(
    property: PropertyItem? = null,
    propertyId: String? = null,
    onBackClick: () -> Unit = {},
    onSimulateKpr: (Long) -> Unit,
    onContactAgent: () -> Unit = {},
    onShowMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { NusaPropertyRepository() }
    val coroutineScope = rememberCoroutineScope()
    var currentProperty by remember { mutableStateOf(property) }
    var isFavorite by remember { mutableStateOf(property?.isFavorite ?: false) }
    var isLoading by remember { mutableStateOf(property == null) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scrollState = rememberScrollState()

    LaunchedEffect(property?.id, propertyId) {
        if (property != null) {
            currentProperty = property
            isFavorite = property.isFavorite
            isLoading = false
        } else if (propertyId != null) {
            isLoading = true
            val fetched = repository.getPropertyById(propertyId)
            currentProperty = fetched
            isFavorite = fetched?.isFavorite ?: false
            isLoading = false
        } else {
            isLoading = true
            val liveProp = repository.getFeaturedProperty()
            currentProperty = liveProp
            isFavorite = liveProp?.isFavorite ?: false
            isLoading = false
        }
    }

    val galleryTitles = listOf(
        "Fasad Rumah Minimalis Modern",
        "Ruang Tamu & Ruang Keluarga",
        "Kamar Tidur Utama Natural"
    )

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceCanvas),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryNavy)
        }
    } else if (currentProperty == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceCanvas)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Data properti tidak ditemukan.",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Kembali")
                }
            }
        }
    } else {
        val prop = currentProperty!!
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    PropertyHeroGraphic(
                        modifier = Modifier.fillMaxSize(),
                        title = galleryTitles[page]
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    PrimaryNavy.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    PrimaryNavy.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onShowMessage("Link properti disalin!") },
                        modifier = Modifier
                            .size(38.dp)
                            .background(SurfaceCard.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            coroutineScope.launch {
                                repository.toggleFavorite(prop.id)
                            }
                            onShowMessage(if (isFavorite) "Disimpan ke favorit!" else "Dihapus dari favorit")
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(SurfaceCard.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (isFavorite) StatusError else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                        .background(PrimaryNavy.copy(alpha = 0.8f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/8 Foto",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isSelected = pagerState.currentPage == index
                        val dotWidth by animateDpAsState(
                            targetValue = if (isSelected) 18.dp else 6.dp,
                            label = "dotWidth"
                        )
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(dotWidth)
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) OnPrimary else OnPrimary.copy(alpha = 0.4f))
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-14).dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                            Row(
                                modifier = Modifier
                                    .background(SurfaceLow, RoundedCornerShape(50))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusInfo,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = prop.developerName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "Subsidi Eligible",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .background(StatusSuccess.copy(alpha = 0.12f), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Column {
                            Text(
                                text = prop.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SecondaryAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = prop.addressDetail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable {
                                        val gmapsQuery = Uri.encode("${prop.title}, ${prop.location}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$gmapsQuery"))
                                        try {
                                            context.startActivity(mapIntent)
                                        } catch (_: Exception) {
                                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$gmapsQuery"))
                                            try {
                                                context.startActivity(webIntent)
                                            } catch (_: Exception) {
                                                onShowMessage("Membuka lokasi di peta...")
                                            }
                                        }
                                    }
                            ) {
                                Text(
                                    text = "Lihat di Google Maps",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = StatusInfo,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = StatusInfo,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceLow, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Harga Tunai Mulai",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = prop.priceFormatted,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = PrimaryNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Estimasi KPR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = prop.installmentEstimate,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Spesifikasi Unit",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ready Stock Ready Huni",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SpecCard(
                                icon = Icons.Default.Home,
                                label = "Kamar Tidur",
                                value = "${prop.bedrooms} Kamar Tidur",
                                modifier = Modifier.weight(1f)
                            )
                            SpecCard(
                                icon = Icons.Default.CheckCircle,
                                label = "Kamar Mandi",
                                value = "${prop.bathrooms} Kamar Mandi",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SpecCard(
                                icon = Icons.Default.LocationOn,
                                label = "Parkir Mobil",
                                value = "${prop.carports} Carport",
                                modifier = Modifier.weight(1f)
                            )
                            SpecCard(
                                icon = Icons.Default.Home,
                                label = "Luas Bangunan/Tanah",
                                value = "LB ${prop.buildingArea}m² / LT ${prop.surfaceArea}m²",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SpecCard(
                                icon = Icons.Default.Star,
                                label = "Kapasitas Listrik",
                                value = "Daya: ${prop.electricityVa} VA",
                                modifier = Modifier.weight(1f)
                            )
                            SpecCard(
                                icon = Icons.Default.CheckCircle,
                                label = "Legalitas Tanah",
                                value = "Sertifikat: ${prop.certificateType}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SecondaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Promo Spesial Developer",
                                style = MaterialTheme.typography.titleLarge,
                                color = OnPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Khusus pengajuan KPR via aplikasi NusaProperty bulan ini:",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnPrimaryContainer
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PromoChip(text = "Free BPHTB", modifier = Modifier.weight(1f))
                            PromoChip(text = "Subsidi DP 5%", modifier = Modifier.weight(1f))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PromoChip(text = "Gratis Kanopi", modifier = Modifier.weight(1f))
                            PromoChip(text = "Smart Door Lock", modifier = Modifier.weight(1f))
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fasilitas Sekitar Lingkungan",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Radius Aksesibilitas",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                TertiaryTeal.copy(alpha = 0.35f),
                                                PrimaryFixed.copy(alpha = 0.45f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .background(SurfaceCard.copy(alpha = 0.92f), RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .background(StatusSuccess, CircleShape)
                                    )
                                    Text(
                                        text = "Strategis & Bebas Banjir",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                PoiItem(icon = Icons.Default.Place, text = "🚆 5 Menit ke Stasiun KRL Cikarang")
                                PoiItem(icon = Icons.Default.LocationOn, text = "🚗 3 Menit ke Gerbang Tol Cikarang Utama")
                                PoiItem(icon = Icons.Default.Place, text = "🏥 7 Menit ke RS Siloam Lippo Cikarang")
                                PoiItem(icon = Icons.Default.Place, text = "🏫 4 Menit ke Sekolah Islam Terpadu")
                            }
                        }
                    }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val message = Uri.encode("Halo Tim NusaProperty, saya tertarik dengan unit *${prop.title}* di ${prop.location}. Boleh minta info brosur dan jadwal survei lokasi?")
                        val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281234567890?text=$message"))
                        try {
                            context.startActivity(waIntent)
                        } catch (_: Exception) {
                            onContactAgent()
                        }
                    },
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusSuccess.copy(alpha = 0.12f),
                        contentColor = StatusSuccess
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hubungi Agen",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { onSimulateKpr(prop.price) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryNavy,
                        contentColor = OnPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Simulasi KPR Unit Ini",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    }
}

@Composable
fun SpecCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceContainer, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PromoChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SecondaryContainer,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = OnPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PoiItem(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(SurfaceContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryNavy,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
    }
}
