package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AravalliBottomNavBar
import com.example.ui.components.CheckInBottomSheet
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.carpenters.CarpentersScreen
import com.example.ui.screens.catalog.CatalogScreen
import com.example.ui.screens.estimates.CreateEstimateScreen
import com.example.ui.screens.estimates.EstimatesScreen
import com.example.ui.screens.greetings.GreetingsScreen
import com.example.ui.screens.messages.MessagesScreen
import com.example.ui.screens.orders.OrdersScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.scanner.QrScannerScreen
import com.example.ui.screens.visitors.VisitorsScreen
import com.example.ui.theme.AravalliBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AravalliFurnitureApp()
            }
        }
    }
}

@Composable
fun AravalliFurnitureApp(
    viewModel: MainViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isCheckedIn by viewModel.isCheckedIn.collectAsStateWithLifecycle()
    val lastCheckInTime by viewModel.lastCheckInTime.collectAsStateWithLifecycle()
    val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val salesData by viewModel.salesData.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()

    val estimates by viewModel.estimates.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val visitors by viewModel.visitors.collectAsStateWithLifecycle()
    val carpenters by viewModel.carpenters.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    val showCheckInDialog by viewModel.showCheckInDialog.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()

    // Handle system back navigation
    BackHandler(enabled = currentScreen != Screen.Home) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AravalliBackground),
        bottomBar = {
            // Show bottom navigation on all primary tabs
            AravalliBottomNavBar(
                currentScreen = currentScreen,
                onTabSelected = { screen ->
                    viewModel.navigateTo(screen)
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> {
                    HomeScreen(
                        isCheckedIn = isCheckedIn,
                        unreadNotifications = unreadNotifications,
                        salesData = salesData,
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear,
                        onCheckInClick = { viewModel.openCheckInDialog() },
                        onNotificationsClick = { viewModel.openNotificationsSheet() },
                        onEstimateClick = { viewModel.navigateTo(Screen.Estimates) },
                        onGreetingsClick = { viewModel.navigateTo(Screen.Greetings) },
                        onVisitorsClick = { viewModel.navigateTo(Screen.Visitors) },
                        onConfirmOrderClick = { viewModel.navigateTo(Screen.ConfirmOrders) },
                        onMyCarpenterClick = { viewModel.navigateTo(Screen.MyCarpenter) },
                        onMonthYearChange = { month, year -> viewModel.setMonthAndYear(month, year) }
                    )
                }

                Screen.Estimates -> {
                    EstimatesScreen(
                        estimates = estimates,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onCreateNewClick = { viewModel.navigateTo(Screen.CreateEstimate) },
                        onConvertToOrderClick = { estimate ->
                            viewModel.convertEstimateToOrder(estimate)
                        },
                        onStatusChange = { id, status ->
                            viewModel.updateEstimateStatus(id, status)
                        }
                    )
                }

                Screen.CreateEstimate -> {
                    CreateEstimateScreen(
                        onBackClick = { viewModel.navigateTo(Screen.Estimates) },
                        onSaveEstimate = { name, phone, address, items, discount, notes ->
                            viewModel.createEstimate(name, phone, address, items, discount, notes)
                        }
                    )
                }

                Screen.Greetings -> {
                    GreetingsScreen(
                        onBackClick = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                Screen.Visitors -> {
                    VisitorsScreen(
                        visitors = visitors,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onAddVisitor = { name, phone, req, budget, notes ->
                            viewModel.addVisitor(name, phone, req, budget, notes)
                        },
                        onUpdateStatus = { id, status ->
                            viewModel.updateVisitorStatus(id, status)
                        },
                        onCreateEstimateForVisitor = {
                            viewModel.navigateTo(Screen.CreateEstimate)
                        }
                    )
                }

                Screen.ConfirmOrders -> {
                    OrdersScreen(
                        orders = orders,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onUpdateStatus = { id, status ->
                            viewModel.updateOrderStatus(id, status)
                        },
                        onRecordPayment = { id, amount ->
                            viewModel.recordOrderPayment(id, amount)
                        }
                    )
                }

                Screen.MyCarpenter -> {
                    CarpentersScreen(
                        carpenters = carpenters,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onAddCarpenter = { name, phone, area, spec ->
                            viewModel.addCarpenter(name, phone, area, spec)
                        },
                        onAwardPoints = { id, pts, cb ->
                            viewModel.awardCarpenterPoints(id, pts, cb)
                        }
                    )
                }

                Screen.Catalog -> {
                    CatalogScreen(
                        products = products,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onProductSelectForQuote = {
                            viewModel.navigateTo(Screen.CreateEstimate)
                        }
                    )
                }

                Screen.QrScanner -> {
                    QrScannerScreen(
                        onBackClick = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                Screen.Profile -> {
                    ProfileScreen(
                        isCheckedIn = isCheckedIn,
                        lastCheckInTime = lastCheckInTime,
                        onBackClick = { viewModel.navigateTo(Screen.Home) },
                        onCheckInClick = { viewModel.openCheckInDialog() }
                    )
                }

                Screen.Messages -> {
                    MessagesScreen(
                        onBackClick = { viewModel.navigateTo(Screen.Home) }
                    )
                }
            }
        }
    }

    // Attendance Punch In / Out Modal Sheet
    if (showCheckInDialog) {
        CheckInBottomSheet(
            isCheckedIn = isCheckedIn,
            lastCheckInTime = lastCheckInTime,
            checkInHistory = checkIns,
            onToggleCheckIn = { staff, loc, note ->
                viewModel.toggleCheckIn(staff, loc, note)
            },
            onDismiss = { viewModel.closeCheckInDialog() }
        )
    }

    // Activity & Notifications Modal Sheet
    if (showNotificationsSheet) {
        NotificationsBottomSheet(
            onDismiss = { viewModel.closeNotificationsSheet() }
        )
    }
}
