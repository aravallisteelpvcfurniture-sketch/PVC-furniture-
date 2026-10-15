package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SalesDataPoint
import com.example.ui.theme.ChartEstimatePurple
import com.example.ui.theme.ChartOrderBlue
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateVsOrderSection(
    salesData: List<SalesDataPoint>,
    selectedMonth: String,
    selectedYear: String,
    onMonthYearChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMonthDropdownOpen by remember { mutableStateOf(false) }
    var yearInput by remember(selectedYear) { mutableStateOf(selectedYear) }
    var selectedDayPoint by remember { mutableStateOf<SalesDataPoint?>(null) }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Month and Year Row matching screenshot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Select Month Dropdown
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Select Month",
                    fontSize = 13.sp,
                    color = Color(0xFF424242),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isMonthDropdownOpen,
                    onExpandedChange = { isMonthDropdownOpen = it }
                ) {
                    OutlinedTextField(
                        value = selectedMonth,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Month")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFCFD8DC),
                            focusedBorderColor = Color(0xFF2E7D32)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("select_month_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = isMonthDropdownOpen,
                        onDismissRequest = { isMonthDropdownOpen = false }
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    onMonthYearChange(month, yearInput)
                                    isMonthDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // Enter Year
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Enter Year",
                    fontSize = 13.sp,
                    color = Color(0xFF424242),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = yearInput,
                    onValueChange = {
                        yearInput = it.take(4)
                        if (it.length == 4) {
                            onMonthYearChange(selectedMonth, it)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color(0xFFCFD8DC),
                        focusedBorderColor = Color(0xFF2E7D32)
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("enter_year_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // "Estimate vs Order" header
        Text(
            text = "Estimate vs Order",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B241E),
            modifier = Modifier.testTag("estimate_vs_order_title")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Legend Row matching screenshot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Order Amount Legend
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(ChartOrderBlue)
                )
                Text(
                    text = "Order Amount",
                    fontSize = 13.sp,
                    color = Color(0xFF37474F),
                    fontWeight = FontWeight.Medium
                )
            }

            // Estimate Amount Legend
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(ChartEstimatePurple)
                )
                Text(
                    text = "Estimate Amount",
                    fontSize = 13.sp,
                    color = Color(0xFF37474F),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // The Dual Bar Chart Canvas Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("sales_chart_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                // If a day is tapped, show tooltip details
                selectedDayPoint?.let { point ->
                    Surface(
                        color = Color(0xFFF1F8F4),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Day ${point.day} $selectedMonth:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "Est: ₹${formatCurrency(point.estimateAmount)}",
                                fontSize = 12.sp,
                                color = ChartEstimatePurple,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Ord: ₹${formatCurrency(point.orderAmount)}",
                                fontSize = 12.sp,
                                color = ChartOrderBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Chart with fixed Y-axis labels and horizontally scrollable bar pairs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    // Y-Axis Labels matching screenshot (662092, 600000, 400000, 0)
                    Column(
                        modifier = Modifier
                            .width(60.dp)
                            .fillMaxHeight()
                            .padding(start = 10.dp, bottom = 22.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("662092", fontSize = 10.sp, color = Color(0xFF546E7A), fontWeight = FontWeight.Medium)
                        Text("600000", fontSize = 10.sp, color = Color(0xFF78909C))
                        Text("400000", fontSize = 10.sp, color = Color(0xFF78909C))
                        Text("0", fontSize = 10.sp, color = Color(0xFF90A4AE))
                    }

                    // Horizontally scrollable bar chart for all days
                    val scrollState = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .horizontalScroll(scrollState)
                    ) {
                        val barGroupWidth = 20.dp
                        val totalChartWidth = (barGroupWidth * (salesData.size.coerceAtLeast(1))) + 30.dp

                        Canvas(
                            modifier = Modifier
                                .width(totalChartWidth)
                                .fillMaxHeight()
                                .pointerInput(salesData) {
                                    detectTapGestures { offset ->
                                        val index = (offset.x / 20.dp.toPx()).toInt()
                                        if (index in salesData.indices) {
                                            selectedDayPoint = salesData[index]
                                        }
                                    }
                                }
                        ) {
                            val maxVal = 700000f
                            val chartBottom = size.height - 24.dp.toPx()
                            val chartHeight = chartBottom - 10.dp.toPx()

                            // Horizontal subtle gridlines
                            val gridLevels = listOf(0f, 400000f, 600000f, 662092f)
                            gridLevels.forEach { lvl ->
                                val y = chartBottom - (lvl / maxVal) * chartHeight
                                drawLine(
                                    color = Color(0xFFECEFF1),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                )
                            }

                            // Draw bars for each day
                            salesData.forEachIndexed { i, point ->
                                val groupStartX = i * 20.dp.toPx() + 6.dp.toPx()
                                val barWidth = 6.dp.toPx()

                                // Estimate Bar (Purple)
                                val estHeight = ((point.estimateAmount.toFloat() / maxVal) * chartHeight).coerceAtLeast(4f)
                                val estTop = chartBottom - estHeight
                                drawRoundRect(
                                    color = ChartEstimatePurple,
                                    topLeft = Offset(groupStartX, estTop),
                                    size = Size(barWidth, estHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                )

                                // Order Bar (Blue)
                                val ordHeight = ((point.orderAmount.toFloat() / maxVal) * chartHeight).coerceAtLeast(4f)
                                val ordTop = chartBottom - ordHeight
                                drawRoundRect(
                                    color = ChartOrderBlue,
                                    topLeft = Offset(groupStartX + barWidth + 2.dp.toPx(), ordTop),
                                    size = Size(barWidth, ordHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                )
                            }
                        }
                    }
                }

                // Days label row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Day 1", fontSize = 10.sp, color = Color(0xFF90A4AE))
                    Text("Day 10", fontSize = 10.sp, color = Color(0xFF90A4AE))
                    Text("Day 20", fontSize = 10.sp, color = Color(0xFF90A4AE))
                    Text("Day 30", fontSize = 10.sp, color = Color(0xFF90A4AE))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Monthly Summary metrics row
        val totalEstimates = salesData.sumOf { it.estimateAmount }
        val totalOrders = salesData.sumOf { it.orderAmount }
        val conversionRatio = if (totalEstimates > 0) (totalOrders / totalEstimates) * 100 else 0.0

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryStatCard(
                label = "Total Estimates",
                amount = "₹${formatCurrency(totalEstimates)}",
                color = ChartEstimatePurple,
                modifier = Modifier.weight(1f)
            )
            SummaryStatCard(
                label = "Confirmed Orders",
                amount = "₹${formatCurrency(totalOrders)}",
                color = ChartOrderBlue,
                modifier = Modifier.weight(1f)
            )
            SummaryStatCard(
                label = "Conversion",
                amount = "${conversionRatio.toInt()}%",
                color = Color(0xFF2E7D32),
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}

@Composable
private fun SummaryStatCard(
    label: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF455A64)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
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
