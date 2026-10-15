package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.SalesDataPoint
import com.example.ui.components.*
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.AravalliGreenHeader
import com.example.ui.theme.AravalliGreenPrimary

@Composable
fun HomeScreen(
    isCheckedIn: Boolean,
    unreadNotifications: Int,
    salesData: List<SalesDataPoint>,
    selectedMonth: String,
    selectedYear: String,
    onCheckInClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onEstimateClick: () -> Unit,
    onGreetingsClick: () -> Unit,
    onVisitorsClick: () -> Unit,
    onConfirmOrderClick: () -> Unit,
    onMyCarpenterClick: () -> Unit,
    onMonthYearChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AravalliBackground)
            .verticalScroll(scrollState)
            .testTag("home_screen")
    ) {
        // TOP GREEN SECTION: Matches screenshot's upper green backdrop
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AravalliGreenHeader)
                .padding(bottom = 20.dp)
        ) {
            // Welcome Header with Check In & Notification bell
            AravalliTopBar(
                isCheckedIn = isCheckedIn,
                unreadNotifications = unreadNotifications,
                onCheckInClick = onCheckInClick,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Tiles: Estimate, Greetings, Visitors, Confirm Order, My Carpenter
            ActionTilesSection(
                onEstimateClick = onEstimateClick,
                onGreetingsClick = onGreetingsClick,
                onVisitorsClick = onVisitorsClick,
                onConfirmOrderClick = onConfirmOrderClick,
                onMyCarpenterClick = onMyCarpenterClick
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // LOWER SECTION: Matches screenshot's banner, month selector, and Estimate vs Order chart
        BannerCarousel(
            onBannerClick = { /* Can open details */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EstimateVsOrderSection(
            salesData = salesData,
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            onMonthYearChange = onMonthYearChange
        )

        Spacer(modifier = Modifier.height(88.dp)) // Extra space for floating bottom nav
    }
}
