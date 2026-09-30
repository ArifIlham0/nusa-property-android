package com.nusaproperty.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusaproperty.app.data.DocumentStatus
import com.nusaproperty.app.data.PropertyTagType
import com.nusaproperty.app.ui.theme.*

@Composable
fun PropertyTagBadge(
    tagType: PropertyTagType,
    text: String,
    modifier: Modifier = Modifier
) {
    val (bgColor: Color, textColor: Color, icon: ImageVector) = when (tagType) {
        PropertyTagType.SUBSIDI -> Triple(StatusSuccess, OnPrimary, Icons.Default.Home)
        PropertyTagType.PROMO -> Triple(SecondaryAmber, OnSecondary, Icons.Default.Star)
        PropertyTagType.DISCOUNT -> Triple(PrimaryContainer, OnPrimary, Icons.Default.ShoppingCart)
    }

    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun DocumentStatusChip(
    status: DocumentStatus,
    label: String,
    modifier: Modifier = Modifier
) {
    val (bgColor: Color, textColor: Color, icon: ImageVector?) = when (status) {
        DocumentStatus.VERIFIED -> Triple(StatusSuccess.copy(alpha = 0.15f), StatusSuccess, Icons.Default.CheckCircle)
        DocumentStatus.UPLOADED -> Triple(StatusInfo.copy(alpha = 0.15f), StatusInfo, Icons.Default.Done)
        DocumentStatus.REQUIRED -> Triple(SecondaryAmber.copy(alpha = 0.20f), SecondaryBrown, Icons.Default.Warning)
        DocumentStatus.NOT_UPLOADED -> Triple(SurfaceContainer, TextSecondary, null)
    }

    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ModernPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
    backgroundColor: Color = PrimaryNavy,
    contentColor: Color = OnPrimary
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ModernSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryNavy),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceCard,
            contentColor = PrimaryNavy
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = PrimaryNavy,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryNavy,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PropertyHeroGraphic(
    modifier: Modifier = Modifier,
    title: String = "Modern Residence"
) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        PrimaryNavy.copy(alpha = 0.85f),
                        TertiaryTeal.copy(alpha = 0.95f),
                        PrimaryDark
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = AccentGold.copy(alpha = 0.9f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = OnPrimary.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
