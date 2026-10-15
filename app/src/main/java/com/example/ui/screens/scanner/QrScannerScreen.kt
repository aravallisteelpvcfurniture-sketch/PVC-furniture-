package com.example.ui.screens.scanner

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AravalliGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scannedResult by remember { mutableStateOf<String?>(null) }
    var scanMode by remember { mutableStateOf("Carpenter Coupon QR") }

    val infiniteTransition = rememberInfiniteTransition()
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Showroom QR & Barcode Scanner", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AravalliGreenPrimary)
            )
        },
        containerColor = Color(0xFF101814),
        modifier = modifier.testTag("qr_scanner_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("Carpenter Coupon QR", "Product Showroom Tag").forEach { mode ->
                    val isSelected = mode == scanMode
                    Surface(
                        onClick = { scanMode = mode },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFF4CAF50) else Color(0xFF1E2822),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = mode,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Scanner Viewfinder with animated laser line
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF19251E))
                    .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val yPos = size.height * laserY
                    drawLine(
                        color = Color(0xFF00E676),
                        start = Offset(20f, yPos),
                        end = Offset(size.width - 20f, yPos),
                        strokeWidth = 3.dp.toPx()
                    )
                }

                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(160.dp)
                )
            }

            // Simulated Scan Trigger & Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Align barcode or QR code inside the frame",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        scannedResult = if (scanMode == "Carpenter Coupon QR") {
                            "Verified: Real Plast WPC Sheet Coupon #RP-88492! +150 Loyalty Points & ₹500 Cashback approved."
                        } else {
                            "Verified: Aravalli Sagwan Royal Sofa Set 3+1+1 (SKU: ARV-LIV-092) - In Showroom Stock, 10-Yr Warranty."
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simulate Scan Verification", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    scannedResult?.let { result ->
        AlertDialog(
            onDismissRequest = { scannedResult = null },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(36.dp)) },
            title = { Text("Code Scanned Successfully!", fontWeight = FontWeight.Bold) },
            text = { Text(result) },
            confirmButton = {
                Button(
                    onClick = { scannedResult = null },
                    colors = ButtonDefaults.buttonColors(containerColor = AravalliGreenPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}
