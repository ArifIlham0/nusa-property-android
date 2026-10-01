package com.nusaproperty.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.R
import com.nusaproperty.app.data.SessionManager
import com.nusaproperty.app.data.UserData
import com.nusaproperty.app.data.api.ApiClient
import com.nusaproperty.app.data.repository.NusaPropertyRepository
import com.nusaproperty.app.ui.components.ModernPrimaryButton
import com.nusaproperty.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onAuthSuccess: (UserData) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { NusaPropertyRepository() }
    val sessionManager = remember { SessionManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var isRegisterMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun doLogin(loginEmail: String, loginPass: String) {
        if (loginEmail.isBlank() || loginPass.isBlank()) {
            errorMessage = "Silakan isi email dan kata sandi."
            return
        }
        isLoading = true
        errorMessage = null
        coroutineScope.launch {
            val result = repository.login(loginEmail, loginPass)
            isLoading = false
            result.fold(
                onSuccess = { authResp ->
                    sessionManager.saveSession(authResp.accessToken, authResp.user)
                    ApiClient.tokenProvider = { authResp.accessToken }
                    onAuthSuccess(authResp.user)
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Gagal masuk. Periksa email dan kata sandi Anda."
                }
            )
        }
    }

    fun doRegister() {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            errorMessage = "Silakan lengkapi nama, email, dan kata sandi."
            return
        }
        if (password.length < 6) {
            errorMessage = "Kata sandi minimal 6 karakter."
            return
        }
        isLoading = true
        errorMessage = null
        coroutineScope.launch {
            val result = repository.register(email, password, fullName, phone)
            isLoading = false
            result.fold(
                onSuccess = { authResp ->
                    sessionManager.saveSession(authResp.accessToken, authResp.user)
                    ApiClient.tokenProvider = { authResp.accessToken }
                    onAuthSuccess(authResp.user)
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Pendaftaran gagal. Silakan coba kembali."
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_nusa_logo),
                contentDescription = "Nusa Property Logo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "NusaProperty",
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Text(
                text = if (isRegisterMode) "Daftar akun baru untuk mengajukan KPR" else "Masuk untuk melanjutkan pengajuan KPR",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Button(
                        onClick = {
                            isRegisterMode = false
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isRegisterMode) PrimaryNavy else Color.Transparent,
                            contentColor = if (!isRegisterMode) OnPrimary else TextSecondary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) {
                        Text("Masuk", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            isRegisterMode = true
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRegisterMode) PrimaryNavy else Color.Transparent,
                            contentColor = if (isRegisterMode) OnPrimary else TextSecondary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) {
                        Text("Daftar", fontWeight = FontWeight.Bold)
                    }
                }
            }
            AnimatedVisibility(visible = errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = StatusError.copy(alpha = 0.12f))
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = StatusError,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                        fontWeight = FontWeight.Medium
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
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (isRegisterMode) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nama Lengkap") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryNavy) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Nomor Handphone (opsional)") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryNavy) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryNavy) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Kata Sandi") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryNavy) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Sembunyikan" else "Tampilkan"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryNavy, modifier = Modifier.size(32.dp))
                        }
                    } else {
                        ModernPrimaryButton(
                            text = if (isRegisterMode) "Daftar Sekarang" else "Masuk Akun",
                            onClick = {
                                if (isRegisterMode) doRegister() else doLogin(email, password)
                            }
                        )
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryFixed.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Akses Cepat Pengujian (Akun Demo)",
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Masuk otomatis sebagai Dimas Nugraha dengan data pengajuan & KPR yang telah disiapkan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Button(
                        onClick = {
                            email = "dimas@nusaproperty.com"
                            password = "dimas123"
                            doLogin("dimas@nusaproperty.com", "dimas123")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Masuk sebagai Dimas Nugraha (Demo)")
                    }
                }
            }
        }
    }
}
