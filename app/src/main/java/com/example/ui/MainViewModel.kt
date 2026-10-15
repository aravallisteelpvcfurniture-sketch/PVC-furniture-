package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AravalliRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object QrScanner : Screen("qr_scanner")
    object Profile : Screen("profile")
    object Messages : Screen("messages")
    object Estimates : Screen("estimates")
    object CreateEstimate : Screen("create_estimate")
    object Greetings : Screen("greetings")
    object Visitors : Screen("visitors")
    object ConfirmOrders : Screen("confirm_orders")
    object MyCarpenter : Screen("my_carpenter")
}

class MainViewModel(
    private val repository: AravalliRepository = AravalliRepository.getInstance()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))

    val estimates = repository.estimates
    val orders = repository.orders
    val visitors = repository.visitors
    val carpenters = repository.carpenters
    val products = repository.products
    val checkIns = repository.checkIns
    val isCheckedIn = repository.isCheckedIn
    val lastCheckInTime = repository.lastCheckInTime
    val selectedMonth = repository.selectedMonth
    val selectedYear = repository.selectedYear
    val salesData = repository.salesData

    // UI Dialog & BottomSheet toggles
    private val _showCheckInDialog = MutableStateFlow(false)
    val showCheckInDialog: StateFlow<Boolean> = _showCheckInDialog.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _unreadNotificationsCount = MutableStateFlow(3)
    val unreadNotificationsCount: StateFlow<Int> = _unreadNotificationsCount.asStateFlow()

    // Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedProductCategory = MutableStateFlow("All")
    val selectedProductCategory: StateFlow<String> = _selectedProductCategory.asStateFlow()

    // Greeting card state
    private val _selectedGreetingTemplate = MutableStateFlow(greetingTemplates.first())
    val selectedGreetingTemplate: StateFlow<GreetingTemplate> = _selectedGreetingTemplate.asStateFlow()

    private val _greetingCustomerName = MutableStateFlow("Valued Customer")
    val greetingCustomerName: StateFlow<String> = _greetingCustomerName.asStateFlow()

    private val _greetingCustomNote = MutableStateFlow("May this season bring prosperity, joy, and elegance to your home with handcrafted furniture from Aravalli Furniture.")
    val greetingCustomNote: StateFlow<String> = _greetingCustomNote.asStateFlow()

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _currentScreen.value = screen
            _screenStack.update { it + screen }
        }
    }

    fun navigateBack(): Boolean {
        val stack = _screenStack.value
        return if (stack.size > 1) {
            val updated = stack.dropLast(1)
            _screenStack.value = updated
            _currentScreen.value = updated.last()
            true
        } else {
            false
        }
    }

    fun openCheckInDialog() {
        _showCheckInDialog.value = true
    }

    fun closeCheckInDialog() {
        _showCheckInDialog.value = false
    }

    fun openNotificationsSheet() {
        _showNotificationsSheet.value = true
        _unreadNotificationsCount.value = 0
    }

    fun closeNotificationsSheet() {
        _showNotificationsSheet.value = false
    }

    fun setMonthAndYear(month: String, year: String) {
        repository.setMonthAndYear(month, year)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedProductCategory(cat: String) {
        _selectedProductCategory.value = cat
    }

    fun setGreetingTemplate(template: GreetingTemplate) {
        _selectedGreetingTemplate.value = template
    }

    fun setGreetingCustomerName(name: String) {
        _greetingCustomerName.value = name
    }

    fun setGreetingCustomNote(note: String) {
        _greetingCustomNote.value = note
    }

    fun toggleCheckIn(staffName: String, location: String, note: String) {
        repository.toggleCheckIn(staffName, location, note)
        closeCheckInDialog()
    }

    fun createEstimate(
        clientName: String,
        clientPhone: String,
        clientAddress: String,
        items: List<EstimateItem>,
        extraDiscount: Double,
        notes: String
    ) {
        val estimateNumber = "EST-2026-${(100..999).random()}"
        val estimate = Estimate(
            estimateNumber = estimateNumber,
            clientName = clientName,
            clientPhone = clientPhone,
            clientAddress = clientAddress,
            items = items,
            extraDiscountPercent = extraDiscount,
            status = EstimateStatus.DRAFT,
            createdAt = "Today",
            notes = notes
        )
        repository.addEstimate(estimate)
    }

    fun updateEstimateStatus(estimateId: String, status: EstimateStatus) {
        repository.updateEstimateStatus(estimateId, status)
    }

    fun convertEstimateToOrder(estimate: Estimate) {
        repository.convertEstimateToOrder(estimate)
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        repository.updateOrderStatus(orderId, status)
    }

    fun recordOrderPayment(orderId: String, amount: Double) {
        repository.recordOrderPayment(orderId, amount)
    }

    fun addVisitor(
        name: String,
        phone: String,
        requirement: String,
        budgetRange: String,
        notes: String
    ) {
        val visitor = Visitor(
            name = name,
            phone = phone,
            requirement = requirement,
            budgetRange = budgetRange,
            visitDate = "Today",
            followUpDate = "Next 3 Days",
            status = VisitorStatus.NEW_LEAD,
            notes = notes
        )
        repository.addVisitor(visitor)
    }

    fun updateVisitorStatus(visitorId: String, status: VisitorStatus) {
        repository.updateVisitorStatus(visitorId, status)
    }

    fun addCarpenter(
        name: String,
        phone: String,
        workshopArea: String,
        specialty: String
    ) {
        val carpenter = Carpenter(
            name = name,
            phone = phone,
            workshopArea = workshopArea,
            specialty = specialty,
            totalPoints = 500, // Welcome bonus points!
            pendingCashback = 1000.0,
            completedProjectsCount = 0,
            tier = "Silver Partner"
        )
        repository.addCarpenter(carpenter)
    }

    fun awardCarpenterPoints(carpenterId: String, points: Int, cashback: Double) {
        repository.awardCarpenterPoints(carpenterId, points, cashback)
    }

    companion object {
        val greetingTemplates = listOf(
            GreetingTemplate(
                id = "diwali_dhamaka",
                occasion = "Diwali Festival Dhamaka",
                title = "शुभ दीपावली & Exclusive Festive Offer",
                message = "Wishing you and your family abundant happiness, prosperity, and light. Celebrate Diwali with Aravalli Furniture's festive collection at flat 25% off!",
                bannerTag = "Festive Season 2026",
                accentColorHex = 0xFFFF6F00
            ),
            GreetingTemplate(
                id = "griha_pravesh",
                occasion = "Griha Pravesh (New Home)",
                title = "गृह प्रवेश की हार्दिक बधाई",
                message = "Heartiest congratulations on your beautiful new home! May every corner be filled with warmth, love, and memories crafted with Aravalli Furniture.",
                bannerTag = "New Beginnings",
                accentColorHex = 0xFF8E24AA
            ),
            GreetingTemplate(
                id = "new_year",
                occasion = "New Year 2027",
                title = "Happy New Year & Fresh Spaces",
                message = "May this New Year bring new perspectives, peace, and timeless elegance to your living and workspaces. Warm wishes from Aravalli Furniture.",
                bannerTag = "New Year Special",
                accentColorHex = 0xFF00796B
            ),
            GreetingTemplate(
                id = "carpenter_appreciation",
                occasion = "Vishwakarma Day / Carpenter Honor",
                title = "विश्वकर्मा पूजा & शिल्पी सम्मान",
                message = "Honoring our skilled carpenters and master craftsmen who carve dreams into timber. Thank you for building perfection with Aravalli Furniture.",
                bannerTag = "Shilpkar Partner",
                accentColorHex = 0xFF0288D1
            )
        )
    }
}
