package com.example.ui.screens.estimates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.model.Estimate
import com.example.data.model.EstimateItem
import com.example.data.model.EstimateStatus
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenPrimary
import com.example.ui.theme.BadgeEstimateRed
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimatesScreen(
    estimates: List<Estimate>,
    onBackClick: () -> Unit,
    onCreateNewClick: () -> Unit,
    onConvertToOrderClick: (Estimate) -> Unit,
    onStatusChange: (String, EstimateStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedEstimateForDetail by remember { mutableStateOf<Estimate?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredEstimates = remember(estimates, searchQuery) {
        if (searchQuery.isBlank()) estimates
        else estimates.filter {
            it.clientName.contains(searchQuery, ignoreCase = true) ||
            it.estimateNumber.contains(searchQuery, ignoreCase = true) ||
            it.items.any { item -> item.title.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Estimates & Quotations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${estimates.size} Active Quotations",
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
                    IconButton(onClick = onCreateNewClick) {
                        Icon(Icons.Default.AddCircle, contentDescription = "New Estimate", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AravalliGreenPrimary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNewClick,
                containerColor = BadgeEstimateRed,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Estimate", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .testTag("fab_new_estimate")
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("estimates_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search client name, quote ID or items...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCFD8DC),
                    focusedBorderColor = AravalliGreenPrimary
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredEstimates.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = Color(0xFFB0BEC5),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No estimates found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF546E7A)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(filteredEstimates) { estimate ->
                        EstimateCard(
                            estimate = estimate,
                            onClick = { selectedEstimateForDetail = estimate },
                            onConvertToOrder = { onConvertToOrderClick(estimate) },
                            onStatusChange = { onStatusChange(estimate.id, it) }
                        )
                    }
                }
            }
        }
    }

    // Estimate Details Modal Sheet
    selectedEstimateForDetail?.let { estimate ->
        EstimateDetailDialog(
            estimate = estimate,
            onDismiss = { selectedEstimateForDetail = null },
            onConvertToOrder = {
                onConvertToOrderClick(estimate)
                selectedEstimateForDetail = null
            }
        )
    }
}

@Composable
fun EstimateCard(
    estimate: Estimate,
    onClick: () -> Unit,
    onConvertToOrder: () -> Unit,
    onStatusChange: (EstimateStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
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
                Surface(
                    color = Color(0xFFECEFF1),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = estimate.estimateNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                StatusChip(status = estimate.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = estimate.clientName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = estimate.clientPhone,
                    fontSize = 12.sp,
                    color = Color(0xFF616161)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Items summary
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                estimate.items.forEach { item ->
                    Text(
                        text = "• ${item.title} (${item.quantity}x) - ${item.woodType}",
                        fontSize = 12.sp,
                        color = Color(0xFF455A64),
                        maxLines = 1
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFECEFF1))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Grand Total (incl. GST)", fontSize = 11.sp, color = Color(0xFF78909C))
                    Text(
                        text = "₹${formatCurrency(estimate.grandTotal)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }

                if (estimate.status != EstimateStatus.CONVERTED) {
                    Button(
                        onClick = onConvertToOrder,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Convert to Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "✓ Order Active",
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: EstimateStatus) {
    val (bgColor, textColor) = when (status) {
        EstimateStatus.DRAFT -> Color(0xFFECEFF1) to Color(0xFF455A64)
        EstimateStatus.SENT -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        EstimateStatus.APPROVED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        EstimateStatus.CONVERTED -> Color(0xFFE0F2F1) to Color(0xFF00695C)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun EstimateDetailDialog(
    estimate: Estimate,
    onDismiss: () -> Unit,
    onConvertToOrder: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(text = estimate.estimateNumber, fontSize = 14.sp, color = Color(0xFF757575))
                Text(text = "Quotation Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Client: ${estimate.clientName}", fontWeight = FontWeight.SemiBold)
                Text(text = "Phone: ${estimate.clientPhone}", fontSize = 13.sp)
                Text(text = "Address: ${estimate.clientAddress}", fontSize = 13.sp)

                Divider(modifier = Modifier.padding(vertical = 4.dp))

                Text(text = "Items & Specifications:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                estimate.items.forEach { item ->
                    Column(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(text = "${item.title} (x${item.quantity})", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        Text(text = "Wood: ${item.woodType} | Dimensions: ${item.dimensions}", fontSize = 11.sp, color = Color(0xFF616161))
                        Text(text = "Price: ₹${formatCurrency(item.totalAmount)}", fontSize = 12.sp, color = Color(0xFF2E7D32))
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal:", fontSize = 13.sp)
                    Text("₹${formatCurrency(estimate.subtotal)}", fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("GST (${estimate.gstPercent}%):", fontSize = 13.sp)
                    Text("₹${formatCurrency(estimate.gstAmount)}", fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Grand Total:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("₹${formatCurrency(estimate.grandTotal)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF2E7D32))
                }
            }
        },
        confirmButton = {
            if (estimate.status != EstimateStatus.CONVERTED) {
                Button(
                    onClick = onConvertToOrder,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                ) {
                    Text("Convert to Confirmed Order")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
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
