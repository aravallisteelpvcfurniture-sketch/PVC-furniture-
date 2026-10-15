package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.AravalliGreenDark
import com.example.ui.theme.AravalliGreenHeader
import com.example.ui.theme.AravalliGreenLight
import com.example.ui.theme.AravalliGreenPrimary

@Composable
fun AravalliBottomNavBar(
    currentScreen: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(64.dp)
            .testTag("aravalli_bottom_nav"),
        shape = RoundedCornerShape(32.dp),
        color = AravalliGreenPrimary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home
            val isHome = currentScreen == Screen.Home
            if (isHome) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF43A047), // Active light green pill background matching screenshot
                    modifier = Modifier.testTag("nav_home_active")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Home",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                IconButton(
                    onClick = { onTabSelected(Screen.Home) },
                    modifier = Modifier.testTag("nav_home_inactive")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Tab 2: Catalog / Orders
            val isCatalog = currentScreen == Screen.Catalog
            IconButton(
                onClick = { onTabSelected(Screen.Catalog) },
                modifier = Modifier.testTag("nav_catalog")
            ) {
                Icon(
                    imageVector = if (isCatalog) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                    contentDescription = "Catalog",
                    tint = if (isCatalog) Color(0xFFFFD54F) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Tab 3: QR Scanner
            val isScanner = currentScreen == Screen.QrScanner
            IconButton(
                onClick = { onTabSelected(Screen.QrScanner) },
                modifier = Modifier.testTag("nav_qr_scanner")
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "QR Scanner",
                    tint = if (isScanner) Color(0xFFFFD54F) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Tab 4: Profile / Directory
            val isProfile = currentScreen == Screen.Profile
            IconButton(
                onClick = { onTabSelected(Screen.Profile) },
                modifier = Modifier.testTag("nav_profile")
            ) {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = if (isProfile) Color(0xFFFFD54F) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Tab 5: Messages / WhatsApp Support
            val isMessages = currentScreen == Screen.Messages
            IconButton(
                onClick = { onTabSelected(Screen.Messages) },
                modifier = Modifier.testTag("nav_messages")
            ) {
                Icon(
                    imageVector = if (isMessages) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Messages",
                    tint = if (isMessages) Color(0xFFFFD54F) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
