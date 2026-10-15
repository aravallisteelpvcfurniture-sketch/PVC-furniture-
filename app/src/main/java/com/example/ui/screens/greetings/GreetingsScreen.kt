package com.example.ui.screens.greetings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GreetingTemplate
import com.example.ui.MainViewModel
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenDark
import com.example.ui.theme.AravalliGreenPrimary
import com.example.ui.theme.BadgeGreetingsPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val templates = MainViewModel.greetingTemplates
    var selectedTemplate by remember { mutableStateOf(templates.first()) }
    var customerName by remember { mutableStateOf("Rajeshwari Sharma") }
    var customMessage by remember { mutableStateOf(selectedTemplate.message) }
    var showShareSuccessDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Festive Greetings & Cards",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Branded Customer WhatsApp Wishes",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BadgeGreetingsPurple)
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("greetings_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Select Template
            Text(
                text = "1. Choose Occasion Template",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B241E)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(templates) { template ->
                    val isSelected = template.id == selectedTemplate.id
                    Surface(
                        onClick = {
                            selectedTemplate = template
                            customMessage = template.message
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(template.accentColorHex) else Color.White,
                        shadowElevation = if (isSelected) 3.dp else 1.dp,
                        border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = template.occasion,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF37474F)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = template.bannerTag,
                                fontSize = 11.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF78909C)
                            )
                        }
                    }
                }
            }

            // Customer Name & Message Editor
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "2. Customize Greeting Card",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer / Architect / Dealer Name") },
                        placeholder = { Text("e.g. Shri Rajeshwari Sharma & Family") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        label = { Text("Personalized Greetings Note") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3
                    )
                }
            }

            // 3. Live Greeting Card Preview
            Text(
                text = "3. Live Branded Card Preview",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B241E)
            )

            // The Rendered Greeting Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("greeting_card_preview")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(selectedTemplate.accentColorHex),
                                    Color(0xFF2E7D32),
                                    AravalliGreenDark
                                )
                            )
                        )
                        .border(
                            2.dp,
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFFD54F), Color(0xFFFFB300), Color(0xFFFFD54F))
                            ),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top decorative badge
                        Surface(
                            color = Color(0xFFFFD54F),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "★ ARAVALLI FURNITURE & INTERIORS ★",
                                color = Color(0xFF3E2723),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = selectedTemplate.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFECB3),
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Customer Name Banner
                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = ButtonDefaults.outlinedButtonBorder()
                        ) {
                            Text(
                                text = "Dear $customerName",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = customMessage,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.95f),
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Footer with Showroom contact
                        Divider(color = Color(0xFFFFD54F).copy(alpha = 0.4f))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Showroom & Factory",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFFD54F)
                                )
                                Text(
                                    text = "NH-8 Main Road, Udaipur",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Direct Inquiry",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFFD54F)
                                )
                                Text(
                                    text = "+91 98290 00000",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Share & Download Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showShareSuccessDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp Green
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("share_whatsapp_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share on WhatsApp", fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = { showShareSuccessDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(0.7f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Card")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showShareSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showShareSuccessDialog = false },
            title = { Text("Greeting Card Ready!") },
            text = {
                Text(
                    "Branded greeting for '$customerName' formatted and ready to share with Aravalli Furniture's contact info & festive discount offers."
                )
            },
            confirmButton = {
                Button(
                    onClick = { showShareSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AravalliGreenPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }
}
