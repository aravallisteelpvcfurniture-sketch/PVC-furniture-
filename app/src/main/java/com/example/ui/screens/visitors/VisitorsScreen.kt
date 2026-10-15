package com.example.ui.screens.visitors

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
import com.example.data.model.Visitor
import com.example.data.model.VisitorStatus
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.BadgeVisitorsNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorsScreen(
    visitors: List<Visitor>,
    onBackClick: () -> Unit,
    onAddVisitor: (name: String, phone: String, req: String, budget: String, notes: String) -> Unit,
    onUpdateStatus: (String, VisitorStatus) -> Unit,
    onCreateEstimateForVisitor: (Visitor) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(visitors, selectedFilter, searchQuery) {
        visitors.filter {
            val matchesFilter = when (selectedFilter) {
                "New Lead" -> it.status == VisitorStatus.NEW_LEAD
                "Follow-up" -> it.status == VisitorStatus.FOLLOW_UP
                "Showroom Visited" -> it.status == VisitorStatus.VISITED
                "Converted" -> it.status == VisitorStatus.CONVERTED
                else -> true
            }
            val matchesQuery = searchQuery.isBlank() ||
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.phone.contains(searchQuery, ignoreCase = true) ||
                    it.requirement.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Showroom Visitors & Leads",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${visitors.size} Total Walk-in Leads",
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
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Visitor", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BadgeVisitorsNavy)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BadgeVisitorsNavy,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Log New Visitor", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .testTag("fab_add_visitor")
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("visitors_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search visitor name, phone or furniture...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCFD8DC),
                    focusedBorderColor = BadgeVisitorsNavy
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            val filters = listOf("All", "New Lead", "Follow-up", "Showroom Visited", "Converted")
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter).coerceAtLeast(0),
                edgePadding = 0.dp,
                divider = {},
                indicator = {}
            ) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Surface(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) BadgeVisitorsNavy else Color.White,
                        border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder(),
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF37474F),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Visitors List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(filteredList) { visitor ->
                    VisitorCard(
                        visitor = visitor,
                        onUpdateStatus = { onUpdateStatus(visitor.id, it) },
                        onCreateEstimate = { onCreateEstimateForVisitor(visitor) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddVisitorDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, phone, req, budget, notes ->
                onAddVisitor(name, phone, req, budget, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun VisitorCard(
    visitor: Visitor,
    onUpdateStatus: (VisitorStatus) -> Unit,
    onCreateEstimate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = visitor.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Surface(
                    color = when (visitor.status) {
                        VisitorStatus.NEW_LEAD -> Color(0xFFE3F2FD)
                        VisitorStatus.FOLLOW_UP -> Color(0xFFFFF3E0)
                        VisitorStatus.VISITED -> Color(0xFFE8F5E9)
                        VisitorStatus.CONVERTED -> Color(0xFFE0F2F1)
                        else -> Color(0xFFECEFF1)
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = visitor.status.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (visitor.status) {
                            VisitorStatus.NEW_LEAD -> Color(0xFF1565C0)
                            VisitorStatus.FOLLOW_UP -> Color(0xFFE65100)
                            VisitorStatus.VISITED -> Color(0xFF2E7D32)
                            VisitorStatus.CONVERTED -> Color(0xFF00796B)
                            else -> Color(0xFF546E7A)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = visitor.phone, fontSize = 12.sp, color = Color(0xFF616161))

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Follow-up: ${visitor.followUpDate}", fontSize = 12.sp, color = Color(0xFF616161))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFF5F7F6),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Interested in: ${visitor.requirement}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF37474F)
                    )
                    Text(
                        text = "Budget: ${visitor.budgetRange}",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                    if (visitor.notes.isNotBlank()) {
                        Text(
                            text = "Note: ${visitor.notes}",
                            fontSize = 11.sp,
                            color = Color(0xFF78909C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        val nextStatus = when (visitor.status) {
                            VisitorStatus.NEW_LEAD -> VisitorStatus.VISITED
                            VisitorStatus.VISITED -> VisitorStatus.FOLLOW_UP
                            VisitorStatus.FOLLOW_UP -> VisitorStatus.CONVERTED
                            else -> VisitorStatus.NEW_LEAD
                        }
                        onUpdateStatus(nextStatus)
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Text("Change Status", fontSize = 11.sp)
                }

                Button(
                    onClick = onCreateEstimate,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BadgeVisitorsNavy),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Make Quote", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AddVisitorDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, phone: String, req: String, budget: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var req by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("₹1,00,000 - ₹2,00,000") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Showroom Visitor", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Visitor Name *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number *") },
                    placeholder = { Text("+91 98290 00000") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = req,
                    onValueChange = { req = it },
                    label = { Text("Looking for (Requirement) *") },
                    placeholder = { Text("e.g. 7-Seater Sofa & Center Table") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Estimated Budget") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Preferences / Finishing Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && req.isNotBlank()) {
                        onAdd(name, phone, req, budget, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BadgeVisitorsNavy)
            ) {
                Text("Save Visitor")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
