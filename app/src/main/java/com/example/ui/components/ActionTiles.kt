package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ActionTilesSection(
    onEstimateClick: () -> Unit,
    onGreetingsClick: () -> Unit,
    onVisitorsClick: () -> Unit,
    onConfirmOrderClick: () -> Unit,
    onMyCarpenterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: Estimate & Greetings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ActionTileCard(
                title = "Estimate",
                badgeColor = BadgeEstimateRed,
                icon = Icons.Default.Calculate,
                onClick = onEstimateClick,
                testTag = "tile_estimate",
                modifier = Modifier.weight(1f)
            )
            ActionTileCard(
                title = "Greetings",
                badgeColor = BadgeGreetingsPurple,
                icon = Icons.Default.Handshake,
                onClick = onGreetingsClick,
                testTag = "tile_greetings",
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: Visitors & Confirm Order
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ActionTileCard(
                title = "Visitors",
                badgeColor = BadgeVisitorsNavy,
                icon = Icons.Default.Badge,
                onClick = onVisitorsClick,
                testTag = "tile_visitors",
                modifier = Modifier.weight(1f)
            )
            ActionTileCard(
                title = "Confirm Order",
                badgeColor = BadgeConfirmOrderTeal,
                icon = Icons.Default.AssignmentTurnedIn,
                onClick = onConfirmOrderClick,
                testTag = "tile_confirm_order",
                modifier = Modifier.weight(1f)
            )
        }

        // Full width Row 3: My Carpenter
        Surface(
            onClick = onMyCarpenterClick,
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF6EBE88), // Frosted green surface matching screenshot
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .testTag("tile_my_carpenter")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Circular Cyan Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BadgeCarpenterCyan),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "My Carpenter",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "My Carpenter",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ActionTileCard(
    title: String,
    badgeColor: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF6EBE88), // Frosted soft green surface matching screenshot
        shadowElevation = 2.dp,
        modifier = modifier
            .height(72.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Circular Badge with Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
        }
    }
}
