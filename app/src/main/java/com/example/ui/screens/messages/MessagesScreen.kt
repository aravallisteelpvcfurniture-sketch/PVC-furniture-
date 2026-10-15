package com.example.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenPrimary

data class ChatMessage(
    val senderName: String,
    val phone: String,
    val lastMessage: String,
    val time: String,
    val unread: Boolean,
    val tag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messageInput by remember { mutableStateOf("") }
    var showSentNotice by remember { mutableStateOf(false) }

    val conversations = remember {
        mutableStateListOf(
            ChatMessage("Rajeshwari Sharma", "+91 98290 41234", "Can you send the teak wood sample finishes on WhatsApp?", "10:14 AM", true, "Estimate Client"),
            ChatMessage("Master Ramesh Suthar", "+91 98291 11200", "Bhaiya, cutting for Order #AF-2026-089 is finished.", "Yesterday", false, "Senior Carpenter"),
            ChatMessage("Prakash Joshi", "+91 98292 77410", "Visiting showroom this Friday with family for dining table trial.", "Yesterday", false, "Visitor Lead"),
            ChatMessage("Dr. Sunita Choudhary", "+91 98281 99014", "Please dispatch the clinic desk by Monday morning.", "11 Oct", false, "Confirmed Order")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer & Carpenter Desk", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AravalliGreenPrimary)
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("messages_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // WhatsApp Quick Banner
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF25D366)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("WhatsApp Business Connected", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1B5E20))
                        Text("Direct quotation and delivery alerts synced with customers", fontSize = 11.sp, color = Color(0xFF616161))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Broadcast Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = { Text("Quick WhatsApp broadcast message...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (messageInput.isNotBlank()) {
                            showSentNotice = true
                            messageInput = ""
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(conversations) { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.senderName.take(1),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.senderName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        text = item.time,
                                        fontSize = 11.sp,
                                        color = Color(0xFF90A4AE)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.lastMessage,
                                    fontSize = 12.sp,
                                    color = Color(0xFF546E7A),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = Color(0xFFF1F8F4),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = item.tag,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSentNotice) {
        AlertDialog(
            onDismissRequest = { showSentNotice = false },
            title = { Text("Message Broadcast Sent") },
            text = { Text("Message successfully dispatched to active client WhatsApp threads.") },
            confirmButton = {
                Button(onClick = { showSentNotice = false }) { Text("OK") }
            }
        )
    }
}
