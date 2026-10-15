package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    isCheckedIn: Boolean,
    lastCheckInTime: String,
    onBackClick: () -> Unit,
    onCheckInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showExportToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Showroom Profile & Settings", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AravalliGreenPrimary)
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("profile_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Business Header Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AravalliGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Weekend,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Aravalli Furniture & Interiors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B241E)
                    )
                    Text(
                        text = "Sagwan Teakwood • WPC Plywood • Modular Kitchens",
                        fontSize = 12.sp,
                        color = Color(0xFF616161)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "GSTIN: 08AABCA1234F1Z8",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Staff Check-In Status Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isCheckedIn) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCheckedIn) Icons.Default.CheckCircle else Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = if (isCheckedIn) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )
                        }
                        Column {
                            Text(
                                text = if (isCheckedIn) "Duty: Active & Checked In" else "Duty: Not Checked In",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                            Text(
                                text = "Last record: $lastCheckInTime",
                                fontSize = 12.sp,
                                color = Color(0xFF757575)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onCheckInClick,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isCheckedIn) "Change" else "Check In", fontSize = 12.sp)
                    }
                }
            }

            // Showroom Contact & Address
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Showroom & Factory Location", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))

                    ProfileRow(icon = Icons.Default.LocationOn, title = "Showroom Address", value = "Plot No. 12, NH-8 Main Highway, Sukher Industrial Zone, Udaipur, Rajasthan 313001")
                    ProfileRow(icon = Icons.Default.Phone, title = "Sales & Customer Support", value = "+91 98290 41234 / +91 94140 88921")
                    ProfileRow(icon = Icons.Default.Email, title = "Official Email", value = "contact@aravallifurniture.com")
                    ProfileRow(icon = Icons.Default.Schedule, title = "Working Hours", value = "Mon - Sat: 9:30 AM to 8:30 PM (Sunday Open)")
                }
            }

            // Export Reports
            Button(
                onClick = { showExportToast = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AravalliGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Monthly Sales & Estimate Report (PDF)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    if (showExportToast) {
        AlertDialog(
            onDismissRequest = { showExportToast = false },
            title = { Text("Report Generated") },
            text = { Text("October 2026 Monthly Statement with ₹12,45,000 Estimates and ₹8,72,500 Confirmed Orders ready for download.") },
            confirmButton = {
                Button(onClick = { showExportToast = false }) { Text("OK") }
            }
        )
    }
}

@Composable
fun ProfileRow(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF616161), modifier = Modifier.size(18.dp))
        Column {
            Text(text = title, fontSize = 11.sp, color = Color(0xFF757575))
            Text(text = value, fontSize = 13.sp, color = Color(0xFF212121), fontWeight = FontWeight.Medium)
        }
    }
}
