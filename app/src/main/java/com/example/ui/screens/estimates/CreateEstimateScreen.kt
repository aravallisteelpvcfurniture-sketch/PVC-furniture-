package com.example.ui.screens.estimates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EstimateItem
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenPrimary
import com.example.ui.theme.BadgeEstimateRed
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEstimateScreen(
    onBackClick: () -> Unit,
    onSaveEstimate: (
        clientName: String,
        clientPhone: String,
        clientAddress: String,
        items: List<EstimateItem>,
        discount: Double,
        notes: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientAddress by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("0") }

    // Items list
    val items = remember {
        mutableStateListOf(
            EstimateItem(
                title = "Sagwan Teakwood Royal Sofa Set (3+1+1)",
                category = "Living Room",
                woodType = "100% Solid Burma Teak",
                dimensions = "84\" x 36\" x 38\"",
                quantity = 1,
                unitPrice = 145000.0,
                laborCost = 10000.0
            )
        )
    }

    // New item inputs
    var newItemTitle by remember { mutableStateOf("") }
    var newItemWoodType by remember { mutableStateOf("Grade-1 Sagwan Teak") }
    var newItemDimensions by remember { mutableStateOf("6ft x 3ft") }
    var newItemPrice by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("1") }
    var newItemLabor by remember { mutableStateOf("0") }

    val scrollState = rememberScrollState()

    val subtotal = items.sumOf { it.totalAmount }
    val discRate = discountPercent.toDoubleOrNull() ?: 0.0
    val discountVal = subtotal * (discRate / 100.0)
    val gstVal = (subtotal - discountVal) * 0.18
    val grandTotal = (subtotal - discountVal) + gstVal

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate New Estimate", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AravalliGreenPrimary)
            )
        },
        containerColor = AravalliBackground,
        modifier = modifier.testTag("create_estimate_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Client Details Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Client Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Client Full Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("Mobile Number (WhatsApp) *") },
                        placeholder = { Text("+91 98765 43210") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = clientAddress,
                        onValueChange = { clientAddress = it },
                        label = { Text("Delivery City / Site Address") },
                        placeholder = { Text("e.g. Fatehpura, Udaipur") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Items List Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Estimate Items (${items.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )

                    items.forEachIndexed { index, item ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF9FBF9),
                            border = ButtonDefaults.outlinedButtonBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.title} (x${item.quantity})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${item.woodType} • ${item.dimensions}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF616161)
                                    )
                                    Text(
                                        text = "Item Total: ₹${formatCurrency(item.totalAmount)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                                IconButton(onClick = { items.removeAt(index) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFD32F2F))
                                }
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = "Add Another Item to Quotation",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = newItemTitle,
                        onValueChange = { newItemTitle = it },
                        label = { Text("Item Title") },
                        placeholder = { Text("e.g. Hydraulic Bed 6x6.5 with Teak Headboard") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newItemWoodType,
                            onValueChange = { newItemWoodType = it },
                            label = { Text("Wood / Material") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newItemDimensions,
                            onValueChange = { newItemDimensions = it },
                            label = { Text("Dimensions") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newItemPrice,
                            onValueChange = { newItemPrice = it },
                            label = { Text("Unit Price (₹)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newItemQuantity,
                            onValueChange = { newItemQuantity = it },
                            label = { Text("Qty") },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newItemLabor,
                            onValueChange = { newItemLabor = it },
                            label = { Text("Labor (₹)") },
                            modifier = Modifier.weight(0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val pr = newItemPrice.toDoubleOrNull() ?: 0.0
                            val qty = newItemQuantity.toIntOrNull() ?: 1
                            val lab = newItemLabor.toDoubleOrNull() ?: 0.0
                            if (newItemTitle.isNotBlank() && pr > 0) {
                                items.add(
                                    EstimateItem(
                                        title = newItemTitle,
                                        category = "Furniture",
                                        woodType = newItemWoodType,
                                        dimensions = newItemDimensions,
                                        quantity = qty,
                                        unitPrice = pr,
                                        laborCost = lab
                                    )
                                )
                                newItemTitle = ""
                                newItemPrice = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Item to List")
                    }
                }
            }

            // Calculations & Discounts Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Price Summary & Taxes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )

                    OutlinedTextField(
                        value = discountPercent,
                        onValueChange = { discountPercent = it },
                        label = { Text("Special Festive Discount (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Remarks / Finishing Notes") },
                        placeholder = { Text("e.g. PU Matte finish, 10-year warranty, delivery within 12 days") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal:", color = Color(0xFF616161))
                        Text("₹${formatCurrency(subtotal)}", fontWeight = FontWeight.SemiBold)
                    }

                    if (discountVal > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount ($discountPercent%):", color = Color(0xFFD32F2F))
                            Text("- ₹${formatCurrency(discountVal)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("GST (18%):", color = Color(0xFF616161))
                        Text("₹${formatCurrency(gstVal)}", fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Grand Total:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            "₹${formatCurrency(grandTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // Save / Submit Button
            Button(
                onClick = {
                    if (clientName.isNotBlank() && items.isNotEmpty()) {
                        onSaveEstimate(
                            clientName,
                            clientPhone.ifBlank { "+91 98290 00000" },
                            clientAddress.ifBlank { "Udaipur, Rajasthan" },
                            items.toList(),
                            discRate,
                            notes
                        )
                        onBackClick()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BadgeEstimateRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_estimate_button")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save & Generate Quotation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun formatCurrency(amount: Double): String {
    return try {
        val format = NumberFormat.getIntegerInstance(Locale("en", "IN"))
        format.format(amount.toLong())
    } catch (e: Exception) {
        amount.toLong().toString()
    }
}
