package com.example.ui.screens.carpenters

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
import com.example.data.model.Carpenter
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.BadgeCarpenterCyan
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarpentersScreen(
    carpenters: List<Carpenter>,
    onBackClick: () -> Unit,
    onAddCarpenter: (name: String, phone: String, area: String, spec: String) -> Unit,
    onAwardPoints: (String, Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCarpenterForPoints by remember { mutableStateOf<Carpenter?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(carpenters, searchQuery) {
        if (searchQuery.isBlank()) carpenters
        else carpenters.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.workshopArea.contains(searchQuery, ignoreCase = true) ||
            it.specialty.contains(searchQuery, ignoreCase = true)
        }
    }

    val totalPointsDistributed = carpenters.sumOf { it.totalPoints }
    val totalCashbackPaid = carpenters.sumOf { it.pendingCashback }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "My Carpenter Partner Club",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Aravalli Shilpkar Loyalty Program",
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
                        Icon(Icons.Default.PersonAdd, contentDescription = "Enroll Carpenter", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BadgeCarpenterCyan)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BadgeCarpenterCyan,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Enroll Carpenter", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .testTag("fab_add_carpenter")
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("carpenters_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Loyalty Stats Card
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
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Active Carpenters", fontSize = 11.sp, color = Color(0xFF616161))
                        Text("${carpenters.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BadgeCarpenterCyan)
                    }
                    Divider(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Points Given", fontSize = 11.sp, color = Color(0xFF616161))
                        Text("$totalPointsDistributed pts", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                    Divider(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Rewards Claimed", fontSize = 11.sp, color = Color(0xFF616161))
                        Text("₹${formatCurrency(totalCashbackPaid)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by carpenter name, area or craft...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCFD8DC),
                    focusedBorderColor = BadgeCarpenterCyan
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(filteredList) { carpenter ->
                    CarpenterCard(
                        carpenter = carpenter,
                        onAddPointsClick = { selectedCarpenterForPoints = carpenter }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddCarpenterDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, phone, area, spec ->
                onAddCarpenter(name, phone, area, spec)
                showAddDialog = false
            }
        )
    }

    selectedCarpenterForPoints?.let { carpenter ->
        AwardPointsDialog(
            carpenter = carpenter,
            onDismiss = { selectedCarpenterForPoints = null },
            onConfirm = { points, cashback ->
                onAwardPoints(carpenter.id, points, cashback)
                selectedCarpenterForPoints = null
            }
        )
    }
}

@Composable
fun CarpenterCard(
    carpenter: Carpenter,
    onAddPointsClick: () -> Unit,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(BadgeCarpenterCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Engineering,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = carpenter.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        Text(
                            text = "${carpenter.specialty} • ${carpenter.workshopArea}",
                            fontSize = 11.sp,
                            color = Color(0xFF616161)
                        )
                    }
                }

                Surface(
                    color = when (carpenter.tier) {
                        "Platinum Partner" -> Color(0xFFEDE7F6)
                        "Gold Partner" -> Color(0xFFFFF8E1)
                        else -> Color(0xFFECEFF1)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = carpenter.tier,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (carpenter.tier) {
                            "Platinum Partner" -> Color(0xFF5E35B1)
                            "Gold Partner" -> Color(0xFFFF8F00)
                            else -> Color(0xFF546E7A)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = carpenter.phone, fontSize = 12.sp, color = Color(0xFF616161))
                }

                Text(
                    text = "${carpenter.completedProjectsCount} Orders Handled",
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium
                )
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFECEFF1))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Loyalty Balance", fontSize = 11.sp, color = Color(0xFF78909C))
                    Text(
                        "${carpenter.totalPoints} Points (₹${formatCurrency(carpenter.pendingCashback)})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B)
                    )
                }

                Button(
                    onClick = onAddPointsClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BadgeCarpenterCyan),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Coupon Pts", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AddCarpenterDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, phone: String, area: String, spec: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var spec by remember { mutableStateOf("Teak Carving & Modular Furniture") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enroll New Carpenter / Mistri", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Carpenter Name *") },
                    placeholder = { Text("e.g. Master Suresh Suthar") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    placeholder = { Text("+91 98290 00000") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Workshop Area / City") },
                    placeholder = { Text("e.g. Bhuwana, Udaipur") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = spec,
                    onValueChange = { spec = it },
                    label = { Text("Craft Specialty") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onAdd(name, phone, area, spec)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BadgeCarpenterCyan)
            ) {
                Text("Enroll Partner")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AwardPointsDialog(
    carpenter: Carpenter,
    onDismiss: () -> Unit,
    onConfirm: (Int, Double) -> Unit
) {
    var pointsText by remember { mutableStateOf("150") }
    var cashbackText by remember { mutableStateOf("500") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Scan / Add Coupon Points", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Awarding points to ${carpenter.name}")
                Text(text = "Real Plast / Plywood Sheet QR code scan verification.", fontSize = 12.sp, color = Color(0xFF616161))
                OutlinedTextField(
                    value = pointsText,
                    onValueChange = { pointsText = it },
                    label = { Text("Points to Credit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cashbackText,
                    onValueChange = { cashbackText = it },
                    label = { Text("Cashback Bonus (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pts = pointsText.toIntOrNull() ?: 0
                    val cb = cashbackText.toDoubleOrNull() ?: 0.0
                    onConfirm(pts, cb)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BadgeCarpenterCyan)
            ) {
                Text("Credit Points")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun formatCurrency(amount: Double): String {
    return try {
        val format = NumberFormat.getIntegerInstance(Locale("en", "IN"))
        format.format(amount.toLong())
    } catch (e: Exception) {
        amount.toLong().toString()
    }
}
