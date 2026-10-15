package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShowroomNotification(
    val title: String,
    val description: String,
    val timeAgo: String,
    val icon: ImageVector,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val notifications = listOf(
        ShowroomNotification(
            title = "Follow-up Reminder: Prakash Joshi",
            description = "Customer requested quote for 3BHK Sagwan Furniture. Follow up scheduled for today.",
            timeAgo = "10 mins ago",
            icon = Icons.Default.PhoneCallback,
            iconTint = Color(0xFF1565C0)
        ),
        ShowroomNotification(
            title = "Order Ready for Polish: #AF-2026-089",
            description = "8-Seater Onyx Marble Dining Table has completed cutting. Ready for Sanding & Polish.",
            timeAgo = "45 mins ago",
            icon = Icons.Default.Handyman,
            iconTint = Color(0xFF00796B)
        ),
        ShowroomNotification(
            title = "Carpenter Bonus Claimed",
            description = "Master Ramesh Suthar claimed ₹2,500 cashback on 25 Real Plast WPC sheets.",
            timeAgo = "2 hours ago",
            icon = Icons.Default.MonetizationOn,
            iconTint = Color(0xFFC62828)
        ),
        ShowroomNotification(
            title = "New Estimate Approved: EST-2026-104",
            description = "Rajeshwari Sharma approved ₹2,13,000 quotation for Rajasthan Teakwood Sofa Set.",
            timeAgo = "Yesterday",
            icon = Icons.Default.CheckCircle,
            iconTint = Color(0xFF2E7D32)
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.testTag("notifications_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications & Activity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications) { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAF9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(item.iconTint.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = item.iconTint,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1B241E)
                                    )
                                    Text(
                                        text = item.timeAgo,
                                        fontSize = 11.sp,
                                        color = Color(0xFF90A4AE)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    color = Color(0xFF546E7A),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
