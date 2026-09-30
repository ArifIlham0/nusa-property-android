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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nusaproperty.app.ui.components.AppScreen
import com.nusaproperty.app.ui.components.NusaBottomNavBar
import com.nusaproperty.app.ui.components.NusaTopAppBar
import com.nusaproperty.app.ui.screens.*
import com.nusaproperty.app.ui.theme.NusaPropertyAndroidTheme
import com.nusaproperty.app.ui.theme.PrimaryNavy
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
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var calculatorPrice by remember { mutableLongStateOf(450_000_000L) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showAkadDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    fun showMessage(msg: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    val (topTitle, topSubtitle, canGoBack) = when (currentScreen) {
        AppScreen.HOME -> Triple("NusaProperty", "Home", false)
        AppScreen.PROPERTY -> Triple("Property Detail", "NusaProperty", true)
        AppScreen.CALCULATOR -> Triple("NusaProperty", "Calculator", true)
        AppScreen.PIPELINE -> Triple("Document Upload", "NusaProperty", true)
        AppScreen.STATUS -> Triple("Sp3K Review", "NusaProperty", true)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NusaTopAppBar(
                title = topTitle,
                subtitle = topSubtitle,
                showBackButton = canGoBack,
                onBackClick = { currentScreen = AppScreen.HOME },
                onNotificationClick = { showNotificationDialog = true },
                onProfileClick = { showMessage("Profil pengguna: Dimas Nugraha") }
            )
        },
        bottomBar = {
            NusaBottomNavBar(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
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
                        onNavigateToCalculator = {
                            calculatorPrice = 450_000_000L
                            currentScreen = AppScreen.CALCULATOR
                        },
                        onNavigateToPipeline = { currentScreen = AppScreen.PIPELINE },
                        onNavigateToProperty = { currentScreen = AppScreen.PROPERTY },
                        onNavigateToStatus = { currentScreen = AppScreen.STATUS },
                        onShowMessage = ::showMessage
                    )

                    AppScreen.PROPERTY -> PropertyDetailScreen(
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
                        onScheduleAkad = { showAkadDialog = true },
                        onDownloadPdf = { showDownloadDialog = true },
                        onCallAdvisor = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:081234567890"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                showMessage("Menghubungi advisor: 081234567890")
                            }
                        },
                        onChatAdvisor = {
                            showMessage("Membuka chat WhatsApp advisor Rian Anggara...")
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
                    Text("Pilih waktu penandatanganan akad kredit basah di Bank Mandiri Cabang Cikarang:")
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
                Text("Surat Penegasan Persetujuan Penyediaan Kredit (SP3K) resmi dengan nomor KPR-2026-NUSA-0918 siap diunduh dalam format PDF resmi bertanda tangan digital.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDownloadDialog = false
                        showMessage("Mengunduh SP3K_KPR-2026-NUSA-0918.pdf...")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Unduh PDF (1.8 MB)")
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
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🎉 Selamat! Pengajuan KPR untuk Cluster Botanical A-12 telah disetujui bank.")
                    HorizontalDivider()
                    Text("📄 Dokumen e-KTP & NPWP Anda telah tervalidasi 100% oleh sistem.")
                    HorizontalDivider()
                    Text("💡 Tips baru: Simak simulasi angsuran terbaru di fitur Kalkulator.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Tutup", color = PrimaryNavy)
                }
            }
        )
    }
}