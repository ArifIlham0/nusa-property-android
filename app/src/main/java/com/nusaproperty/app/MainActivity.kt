package com.nusaproperty.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nusaproperty.app.data.NotificationItem
import com.nusaproperty.app.data.PropertyItem
import com.nusaproperty.app.data.SessionManager
import com.nusaproperty.app.data.api.ApiClient
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import com.nusaproperty.app.ui.components.AppScreen
import com.nusaproperty.app.ui.components.NusaBottomNavBar
import com.nusaproperty.app.ui.components.NusaTopAppBar
import com.nusaproperty.app.ui.screens.*
import com.nusaproperty.app.ui.theme.NusaPropertyAndroidTheme
import com.nusaproperty.app.ui.theme.PrimaryNavy
import com.nusaproperty.app.ui.theme.TextPrimary
import com.nusaproperty.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NusaPropertyAndroidTheme {
                NusaPropertyApp()
            }
        }
    }
}

@Composable
fun NusaPropertyApp() {
    val context = LocalContext.current
    val repository = remember { NusaPropertyRepository() }
    val sessionManager = remember { SessionManager.getInstance(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var currentUser by remember { mutableStateOf(sessionManager.getUser()) }
    var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }
    var showProfileScreen by remember { mutableStateOf(false) }

    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var selectedProperty by remember { mutableStateOf<PropertyItem?>(null) }
    var calculatorPrice by remember { mutableLongStateOf(450_000_000L) }
    var activeSp3kRegNumber by remember { mutableStateOf("KPR-2026-NUSA-0918") }

    var showInfoDialog by remember { mutableStateOf(false) }
    var showAkadDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    var notifications by remember { mutableStateOf<List<NotificationItem>>(emptyList()) }
    var isLoadingNotifications by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ApiClient.tokenProvider = { sessionManager.getToken() }
    }

    LaunchedEffect(showNotificationDialog) {
        if (showNotificationDialog) {
            isLoadingNotifications = true
            notifications = repository.getNotifications()
            isLoadingNotifications = false
        }
    }

    fun showMessage(msg: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    // 1. If not logged in, show AuthScreen
    if (!isLoggedIn) {
        AuthScreen(
            onAuthSuccess = { user ->
                currentUser = user
                isLoggedIn = true
                showMessage("Selamat datang, ${user.fullName}!")
            }
        )
        return
    }

    // 2. If profile screen requested
    if (showProfileScreen) {
        ProfileScreen(
            user = currentUser,
            onBackClick = { showProfileScreen = false },
            onLogout = {
                isLoggedIn = false
                currentUser = null
                showProfileScreen = false
                showMessage("Berhasil keluar dari akun")
            }
        )
        return
    }

    val (topTitle, topSubtitle, canGoBack) = when (currentScreen) {
        AppScreen.HOME -> Triple("NusaProperty", "Home", false)
        AppScreen.PROPERTY -> Triple("Detail Properti", "NusaProperty", true)
        AppScreen.CALCULATOR -> Triple("NusaProperty", "Kalkulator KPR", false)
        AppScreen.PIPELINE -> Triple("Upload Berkas", "NusaProperty", false)
        AppScreen.STATUS -> Triple("Status KPR SP3K", "NusaProperty", false)
    }

    val currentDisplayName = currentUser?.fullName ?: "Dimas Nugraha"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NusaTopAppBar(
                title = topTitle,
                subtitle = topSubtitle,
                showBackButton = canGoBack,
                userName = currentDisplayName,
                onBackClick = { currentScreen = AppScreen.HOME },
                onNotificationClick = { showNotificationDialog = true },
                onProfileClick = { showProfileScreen = true }
            )
        },
        bottomBar = {
            if (currentScreen != AppScreen.PROPERTY) {
                NusaBottomNavBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "screenTransition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(
                        userName = currentDisplayName,
                        onNavigateToCalculator = {
                            calculatorPrice = 450_000_000L
                            currentScreen = AppScreen.CALCULATOR
                        },
                        onNavigateToPipeline = { currentScreen = AppScreen.PIPELINE },
                        onNavigateToProperty = { prop ->
                            selectedProperty = prop
                            currentScreen = AppScreen.PROPERTY
                        },
                        onNavigateToStatus = { currentScreen = AppScreen.STATUS },
                        onShowMessage = ::showMessage
                    )

                    AppScreen.PROPERTY -> PropertyDetailScreen(
                        property = selectedProperty,
                        onBackClick = { currentScreen = AppScreen.HOME },
                        onSimulateKpr = { price ->
                            calculatorPrice = price
                            currentScreen = AppScreen.CALCULATOR
                            showMessage("Harga properti diterapkan ke kalkulator KPR")
                        },
                        onContactAgent = {
                            showMessage("Menghubungi agen via WhatsApp...")
                        },
                        onShowMessage = ::showMessage
                    )

                    AppScreen.CALCULATOR -> CalculatorScreen(
                        initialPrice = calculatorPrice,
                        onApplyKpr = { price, dp, tenor, syariah ->
                            currentScreen = AppScreen.PIPELINE
                            showMessage("Simulasi disimpan. Melanjutkan ke upload berkas...")
                        },
                        onShowInfoDialog = { showInfoDialog = true }
                    )

                    AppScreen.PIPELINE -> DocumentUploadScreen(
                        onNavigateToStatus = {
                            currentScreen = AppScreen.STATUS
                            showMessage("Berkas terkirim! Mengarahkan ke status SP3K...")
                        },
                        onContactSupport = {
                            showMessage("Membuka layanan konsultasi WhatsApp...")
                        },
                        onShowMessage = ::showMessage
                    )

                    AppScreen.STATUS -> ApprovalStatusScreen(
                        onScheduleAkad = { regNo ->
                            activeSp3kRegNumber = regNo
                            showAkadDialog = true
                        },
                        onDownloadPdf = { regNo ->
                            activeSp3kRegNumber = regNo
                            showDownloadDialog = true
                        },
                        onCallAdvisor = { phone ->
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                showMessage("Menghubungi advisor: $phone")
                            }
                        },
                        onChatAdvisor = { phone ->
                            val cleanPhone = if (phone.startsWith("0")) "62" + phone.substring(1) else phone
                            val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanPhone?text=Halo%20Advisor,%20saya%20ingin%20konsultasi%20terkait%20jadwal%20akad%20KPR."))
                            try {
                                context.startActivity(waIntent)
                            } catch (_: Exception) {
                                showMessage("Membuka chat WhatsApp advisor ($phone)...")
                            }
                        }
                    )
                }
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Text(
                    text = "Tentang Kalkulator KPR Pintar",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Suku Bunga Promo: 4.88% fixed selama 3 tahun pertama (Konvensional) atau 5.15% margin flat seluruh tenor (Syariah).")
                    Text("• Rasio Angsuran (DSR): Direkomendasikan maksimal 38-40% dari total penghasilan bersih bulanan.")
                    Text("• Perhitungan ini merupakan simulasi dan dapat disesuaikan dengan syarat bank rekanan.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("Mengerti", color = PrimaryNavy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showAkadDialog) {
        AlertDialog(
            onDismissRequest = { showAkadDialog = false },
            title = {
                Text(
                    text = "Pilih Jadwal Akad Kredit",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Pilih waktu penandatanganan akad kredit basah untuk No: $activeSp3kRegNumber:")
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📅 Senin, 6 Oktober 2026", fontWeight = FontWeight.Bold)
                            Text("⏰ 10:00 - 11:30 WIB", color = PrimaryNavy)
                            Text("📍 KC Bank Mandiri Cikarang City Walk", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAkadDialog = false
                        coroutineScope.launch {
                            repository.scheduleAkad(
                                registrationNumber = activeSp3kRegNumber,
                                date = "Senin, 6 Oktober 2026 10:00 - 11:30 WIB",
                                location = "KC Bank Mandiri Cikarang City Walk"
                            )
                        }
                        showMessage("Jadwal akad berhasil dikonfirmasi! Notifikasi dikirimkan.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Konfirmasi Jadwal")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAkadDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = {
                Text(
                    text = "Unduh Dokumen SP3K",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                Text("Surat Penegasan Persetujuan Penyediaan Kredit (SP3K) resmi dengan nomor $activeSp3kRegNumber siap diunduh dalam format PDF resmi bertanda tangan digital.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDownloadDialog = false
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${ApiClient.BASE_URL}api/sp3k/pdf"))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            showMessage("Mengunduh SP3K_${activeSp3kRegNumber}.pdf...")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Unduh PDF")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDownloadDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Text(
                    text = "Notifikasi KPR",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                if (isLoadingNotifications) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryNavy)
                    }
                } else if (notifications.isEmpty()) {
                    Text(
                        text = "Belum ada notifikasi baru.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        notifications.forEachIndexed { index, notif ->
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = notif.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = notif.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            if (index < notifications.size - 1) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Tutup", color = PrimaryNavy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}