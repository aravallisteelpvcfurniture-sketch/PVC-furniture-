package com.example.ui.screens.orders

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
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.BadgeConfirmOrderTeal
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    orders: List<Order>,
    onBackClick: () -> Unit,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onRecordPayment: (String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOrderForPayment by remember { mutableStateOf<Order?>(null) }
    var selectedFilter by remember { mutableStateOf("All Orders") }

    val filteredOrders = remember(orders, selectedFilter) {
        when (selectedFilter) {
            "In Production" -> orders.filter { it.status != OrderStatus.DELIVERED }
            "Delivered" -> orders.filter { it.status == OrderStatus.DELIVERED }
            "Pending Balance" -> orders.filter { it.balanceDue > 0 }
            else -> orders
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Confirmed Furniture Orders",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${orders.size} Total Orders Tracking",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BadgeConfirmOrderTeal)
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("orders_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Filter Tabs
            val filters = listOf("All Orders", "In Production", "Pending Balance", "Delivered")
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
                        color = if (isSelected) BadgeConfirmOrderTeal else Color.White,
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

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderTrackingCard(
                        order = order,
                        onAdvanceStatus = {
                            val next = when (order.status) {
                                OrderStatus.CONFIRMED -> OrderStatus.CUTTING
                                OrderStatus.CUTTING -> OrderStatus.SANDING_POLISH
                                OrderStatus.SANDING_POLISH -> OrderStatus.UPHOLSTERY
                                OrderStatus.UPHOLSTERY -> OrderStatus.READY_DISPATCH
                                OrderStatus.READY_DISPATCH -> OrderStatus.DELIVERED
                                OrderStatus.DELIVERED -> OrderStatus.DELIVERED
                            }
                            onUpdateStatus(order.id, next)
                        },
                        onRecordPaymentClick = { selectedOrderForPayment = order }
                    )
                }
            }
        }
    }

    selectedOrderForPayment?.let { order ->
        RecordPaymentDialog(
            order = order,
            onDismiss = { selectedOrderForPayment = null },
            onConfirm = { amount ->
                onRecordPayment(order.id, amount)
                selectedOrderForPayment = null
            }
        )
    }
}

@Composable
fun OrderTrackingCard(
    order: Order,
    onAdvanceStatus: () -> Unit,
    onRecordPaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Order ID & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFE0F2F1),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.orderNumber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(
                    text = "Expected: ${order.expectedDeliveryDate}",
                    fontSize = 11.sp,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = order.clientName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Text(
                text = "Items: ${order.itemsSummary}",
                fontSize = 13.sp,
                color = Color(0xFF455A64),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Engineering,
                    contentDescription = null,
                    tint = Color(0xFF0288D1),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Crafted by: ${order.carpenterAssigned}",
                    fontSize = 12.sp,
                    color = Color(0xFF0288D1),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Progress Bar
            OrderStatusPipeline(currentStatus = order.status)

            Spacer(modifier = Modifier.height(12.dp))

            // Financials Strip
            Surface(
                color = Color(0xFFF5F7F6),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Order", fontSize = 11.sp, color = Color(0xFF616161))
                        Text(
                            "₹${formatCurrency(order.totalAmount)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text("Advance Paid", fontSize = 11.sp, color = Color(0xFF2E7D32))
                        Text(
                            "₹${formatCurrency(order.advancePaid)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Balance Due", fontSize = 11.sp, color = Color(0xFFD32F2F))
                        Text(
                            "₹${formatCurrency(order.balanceDue)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.balanceDue > 0) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (order.balanceDue > 0) {
                    OutlinedButton(
                        onClick = onRecordPaymentClick,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Receive Payment", fontSize = 11.sp)
                    }
                }

                if (order.status != OrderStatus.DELIVERED) {
                    Button(
                        onClick = onAdvanceStatus,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BadgeConfirmOrderTeal),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Next Stage", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delivered & Closed", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusPipeline(currentStatus: OrderStatus) {
    val stages = listOf(
        "Confirmed",
        "Cutting",
        "Polish",
        "Upholstery",
        "Ready",
        "Delivered"
    )
    val currentIndex = currentStatus.stepIndex

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            stages.forEachIndexed { index, stage ->
                val isCompleted = index <= currentIndex
                val isCurrent = index == currentIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 18.dp else 14.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> Color(0xFF00796B)
                                    isCompleted -> Color(0xFF80CBC4)
                                    else -> Color(0xFFCFD8DC)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stage,
                        fontSize = 8.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) Color(0xFF00796B) else Color(0xFF78909C)
                    )
                }
            }
        }
    }
}

@Composable
fun RecordPaymentDialog(
    order: Order,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var amountText by remember { mutableStateOf(order.balanceDue.toInt().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Receive Order Payment", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Order: ${order.orderNumber} (${order.clientName})")
                Text(text = "Pending Balance: ₹${formatCurrency(order.balanceDue)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount Received (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    if (amt > 0) onConfirm(amt)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BadgeConfirmOrderTeal)
            ) {
                Text("Confirm Payment")
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
