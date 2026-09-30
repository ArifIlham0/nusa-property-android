package com.nusaproperty.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.ui.theme.*

enum class AppScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    HOME("home", "Beranda", Icons.Default.Home),
    PROPERTY("property", "Properti", Icons.Default.LocationOn),
    CALCULATOR("calculator", "Kalkulator", Icons.Default.DateRange),
    PIPELINE("pipeline", "Pengajuan", Icons.AutoMirrored.Filled.List),
    STATUS("status", "Status KPR", Icons.Default.CheckCircle)
}

@Composable
fun NusaBottomNavBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = SurfaceCard.copy(alpha = 0.98f),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (screen in AppScreen.entries) {
                val isSelected = currentScreen == screen
                val iconBgColor by animateColorAsState(
                    targetValue = if (isSelected) PrimaryFixed else Color.Transparent,
                    label = "iconBg"
                )
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) OnPrimaryFixedVariant else TextSecondary,
                    label = "iconTint"
                )
                val textTint by animateColorAsState(
                    targetValue = if (isSelected) PrimaryNavy else TextSecondary,
                    label = "textTint"
                )
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onScreenSelected(screen) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 54.dp, height = 30.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(iconBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = textTint,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
