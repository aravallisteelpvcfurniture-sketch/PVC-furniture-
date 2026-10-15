package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AravalliRepository private constructor() {

    private val _estimates = MutableStateFlow<List<Estimate>>(emptyList())
    val estimates: StateFlow<List<Estimate>> = _estimates.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _visitors = MutableStateFlow<List<Visitor>>(emptyList())
    val visitors: StateFlow<List<Visitor>> = _visitors.asStateFlow()

    private val _carpenters = MutableStateFlow<List<Carpenter>>(emptyList())
    val carpenters: StateFlow<List<Carpenter>> = _carpenters.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _checkIns = MutableStateFlow<List<CheckInRecord>>(emptyList())
    val checkIns: StateFlow<List<CheckInRecord>> = _checkIns.asStateFlow()

    private val _isCheckedIn = MutableStateFlow(true)
    val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

    private val _lastCheckInTime = MutableStateFlow("08:00 AM Today")
    val lastCheckInTime: StateFlow<String> = _lastCheckInTime.asStateFlow()

    private val _selectedMonth = MutableStateFlow("October")
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow("2026")
    val selectedYear: StateFlow<String> = _selectedYear.asStateFlow()

    private val _salesData = MutableStateFlow<List<SalesDataPoint>>(emptyList())
    val salesData: StateFlow<List<SalesDataPoint>> = _salesData.asStateFlow()

    init {
        seedInitialData()
        generateSalesDataForMonth("October", "2026")
    }

    private fun seedInitialData() {
        _estimates.value = listOf(
            Estimate(
                estimateNumber = "EST-2026-104",
                clientName = "Rajeshwari Sharma",
                clientPhone = "+91 98290 41234",
                clientAddress = "Villa 42, Aravalli Heights, Udaipur",
                items = listOf(
                    EstimateItem(
                        title = "Royal Rajasthan Teakwood Sofa Set (3+2+1)",
                        category = "Living Room",
                        woodType = "Grade-1 Sagwan (Burma Teak)",
                        dimensions = "Sofa: 84x36in, Chairs: 36x36in",
                        quantity = 1,
                        unitPrice = 185000.0,
                        laborCost = 15000.0
                    ),
                    EstimateItem(
                        title = "Solid Teak Center Coffee Table with Carved Legs",
                        category = "Living Room",
                        woodType = "Solid Sagwan Teak",
                        dimensions = "48x28x18in",
                        quantity = 1,
                        unitPrice = 28000.0,
                        laborCost = 3000.0
                    )
                ),
                extraDiscountPercent = 5.0,
                status = EstimateStatus.APPROVED,
                createdAt = "12 Oct 2026",
                notes = "Client requested Honey Gloss PU polish and high density 40D foam."
            ),
            Estimate(
                estimateNumber = "EST-2026-103",
                clientName = "Vikram Singh Rathore",
                clientPhone = "+91 94140 88921",
                clientAddress = "Plot 18, Maharana Pratap Enclave, Rajsamand",
                items = listOf(
                    EstimateItem(
                        title = "Master Bedroom Hydraulic King Storage Bed",
                        category = "Bedroom",
                        woodType = "Century Teak Ply & Solid Teak Headboard",
                        dimensions = "72x78in (King Size)",
                        quantity = 1,
                        unitPrice = 110000.0,
                        laborCost = 8000.0
                    ),
                    EstimateItem(
                        title = "Matching Bedside Tables with Soft-close Drawers",
                        category = "Bedroom",
                        woodType = "Teak Veneer & Marine Ply",
                        dimensions = "20x16x20in",
                        quantity = 2,
                        unitPrice = 14500.0,
                        laborCost = 2000.0
                    )
                ),
                extraDiscountPercent = 3.0,
                status = EstimateStatus.SENT,
                createdAt = "10 Oct 2026",
                notes = "Quotation shared on WhatsApp. Follow up on Saturday."
            ),
            Estimate(
                estimateNumber = "EST-2026-102",
                clientName = "Manish Agrawal",
                clientPhone = "+91 97840 55123",
                clientAddress = "Agrawal Marble Trading, Sukher, Udaipur",
                items = listOf(
                    EstimateItem(
                        title = "Luxury 8-Seater Onyx Marble Dining Table & Chairs",
                        category = "Dining",
                        woodType = "Seasoned Sheesham Wood + Imported Marble",
                        dimensions = "8ft x 4ft",
                        quantity = 1,
                        unitPrice = 195000.0,
                        laborCost = 12000.0
                    )
                ),
                extraDiscountPercent = 0.0,
                status = EstimateStatus.CONVERTED,
                createdAt = "06 Oct 2026",
                notes = "Converted to confirmed Order #AF-2026-089."
            )
        )

        _orders.value = listOf(
            Order(
                orderNumber = "AF-2026-089",
                clientName = "Manish Agrawal",
                clientPhone = "+91 97840 55123",
                deliveryAddress = "Agrawal Marble Trading, Sukher, Udaipur",
                itemsSummary = "8-Seater Onyx Marble Dining Table + 8 Handcrafted Chairs",
                totalAmount = 244260.0,
                advancePaid = 150000.0,
                status = OrderStatus.SANDING_POLISH,
                orderDate = "06 Oct 2026",
                expectedDeliveryDate = "22 Oct 2026",
                carpenterAssigned = "Master Dinesh Suthar"
            ),
            Order(
                orderNumber = "AF-2026-088",
                clientName = "Dr. Sunita Choudhary",
                clientPhone = "+91 98281 99014",
                deliveryAddress = "Clinic Complex, Chetak Circle, Udaipur",
                itemsSummary = "Executive Doctor Desk + Credenza & Patient Waiting Benches",
                totalAmount = 185000.0,
                advancePaid = 100000.0,
                status = OrderStatus.UPHOLSTERY,
                orderDate = "02 Oct 2026",
                expectedDeliveryDate = "18 Oct 2026",
                carpenterAssigned = "Master Ramesh Suthar"
            ),
            Order(
                orderNumber = "AF-2026-087",
                clientName = "Kishanlal Mehta",
                clientPhone = "+91 94141 12389",
                deliveryAddress = "Sector 14, Hiran Magri, Udaipur",
                itemsSummary = "Modular L-Shape Wardrobe 9x8ft with Real Plast PVC carcass",
                totalAmount = 215000.0,
                advancePaid = 215000.0,
                status = OrderStatus.DELIVERED,
                orderDate = "25 Sep 2026",
                expectedDeliveryDate = "10 Oct 2026",
                carpenterAssigned = "Babu Mistri"
            )
        )

        _visitors.value = listOf(
            Visitor(
                name = "Prakash Joshi",
                phone = "+91 98292 77410",
                requirement = "Complete 3BHK Living & Dining Furniture",
                budgetRange = "₹3,50,000 - ₹5,00,000",
                visitDate = "13 Oct 2026",
                followUpDate = "16 Oct 2026",
                status = VisitorStatus.CATALOG_SHARED,
                notes = "Interested in natural Sagwan finish. House possession by Diwali."
            ),
            Visitor(
                name = "Neelam Bhati",
                phone = "+91 97821 34902",
                requirement = "6-Seater Wooden Dining Set & Crockery Unit",
                budgetRange = "₹1,20,000 - ₹1,80,000",
                visitDate = "12 Oct 2026",
                followUpDate = "15 Oct 2026",
                status = VisitorStatus.FOLLOW_UP,
                notes = "Showed interest in modern sleek designs with fabric seating."
            ),
            Visitor(
                name = "Gopal Soni",
                phone = "+91 94132 87654",
                requirement = "Solid Wood Temple (Mandir) with Hand Carving",
                budgetRange = "₹75,000 - ₹1,10,000",
                visitDate = "10 Oct 2026",
                followUpDate = "14 Oct 2026",
                status = VisitorStatus.VISITED,
                notes = "Needs custom dimensions 5ft wide with brass bell accessories."
            )
        )

        _carpenters.value = listOf(
            Carpenter(
                name = "Ramesh Suthar (Senior Mistri)",
                phone = "+91 98291 11200",
                workshopArea = "Bhuwana, Udaipur",
                specialty = "Hand Carving & Antique Teak Wood Work",
                totalPoints = 4850,
                pendingCashback = 12500.0,
                completedProjectsCount = 64,
                tier = "Platinum Partner"
            ),
            Carpenter(
                name = "Dinesh Suthar",
                phone = "+91 94142 33456",
                workshopArea = "Pratap Nagar, Udaipur",
                specialty = "Modular Kitchens & Hydraulic Beds",
                totalPoints = 3200,
                pendingCashback = 8000.0,
                completedProjectsCount = 42,
                tier = "Gold Partner"
            ),
            Carpenter(
                name = "Shravan Jangid",
                phone = "+91 96024 88711",
                workshopArea = "Savina, Udaipur",
                specialty = "Real Plast WPC / PVC Carcass & Wardrobes",
                totalPoints = 2150,
                pendingCashback = 4500.0,
                completedProjectsCount = 28,
                tier = "Silver Partner"
            ),
            Carpenter(
                name = "Kishore Mistri",
                phone = "+91 99280 44521",
                workshopArea = "Goverdhan Vilas, Udaipur",
                specialty = "PU High Gloss & Italian Polish Specialist",
                totalPoints = 1750,
                pendingCashback = 3500.0,
                completedProjectsCount = 19,
                tier = "Silver Partner"
            )
        )

        _products.value = listOf(
            Product(
                name = "Aravalli Maharaja Sagwan Sofa (3+1+1)",
                category = "Living Room",
                price = 145000.0,
                mrp = 185000.0,
                dimensions = "82\" x 34\" x 38\"",
                woodType = "100% Solid Burma Teak",
                description = "Handcrafted royal curved arms with deep button tufting and high resilience comfort foam.",
                inStock = true
            ),
            Product(
                name = "Royal Udaipur Hydraulic King Bed",
                category = "Bedroom",
                price = 98000.0,
                mrp = 125000.0,
                dimensions = "72\" x 78\" (King)",
                woodType = "Grade-1 Teak & Marine Plywood",
                description = "Heavy duty hydraulic gas lifter with velvet acoustic headboard and internal dual storage boxes.",
                inStock = true
            ),
            Product(
                name = "Imperial Onyx 6-Seater Dining Set",
                category = "Dining",
                price = 135000.0,
                mrp = 168000.0,
                dimensions = "72\" x 40\" x 30\"",
                woodType = "Solid Sheesham & Natural Onyx Marble",
                description = "Beveled edge natural marble top supported on hand-turned solid timber pedestals with cushioned chairs.",
                inStock = true
            ),
            Product(
                name = "Real Plast Modular Wardrobe Carcass System",
                category = "Modular",
                price = 78000.0,
                mrp = 95000.0,
                dimensions = "8ft x 7ft x 2ft",
                woodType = "Real Plast Waterproof WPC & Marine Ply",
                description = "100% Termite proof, waterproof PVC carcass with soft-close German hinges and sensor LED lighting.",
                inStock = true
            ),
            Product(
                name = "Executive Teakwood Boss Table & Credenza",
                category = "Office",
                price = 85000.0,
                mrp = 110000.0,
                dimensions = "72\" x 36\" x 30\"",
                woodType = "Solid Teak & Natural Wood Veneer",
                description = "Leatherette writing pad inset, wire management channel, and multi-drawer locking storage.",
                inStock = true
            )
        )

        _checkIns.value = listOf(
            CheckInRecord(
                timestamp = "08:00 AM Today",
                location = "Aravalli Furniture Showroom, NH-8 Main Road",
                type = "Morning Opening Check-In",
                note = "Showroom opened, factory delivery schedules reviewed."
            ),
            CheckInRecord(
                timestamp = "08:15 AM Yesterday",
                location = "Aravalli Furniture Showroom, NH-8 Main Road",
                type = "Morning Opening Check-In",
                note = "Full inventory audit and visitor follow-ups."
            )
        )
    }

    fun setMonthAndYear(month: String, year: String) {
        _selectedMonth.value = month
        _selectedYear.value = year
        generateSalesDataForMonth(month, year)
    }

    private fun generateSalesDataForMonth(month: String, year: String) {
        // Daily points for the month (1 to 31) showing real metrics matching the screenshot graph
        val daysInMonth = if (month in listOf("April", "June", "September", "November")) 30 else if (month == "February") 28 else 31
        val points = mutableListOf<SalesDataPoint>()

        // Seed with realistic realistic curve matching screenshot's dual bars
        for (day in 1..daysInMonth) {
            val baseEstimate = when {
                day in 1..5 -> 580000.0 + (day * 16000)
                day in 6..12 -> 620000.0 + ((day % 4) * 22000)
                day in 13..20 -> 662092.0 - ((day % 5) * 18000)
                day in 21..27 -> 610000.0 + ((day % 3) * 25000)
                else -> 590000.0 + ((day % 2) * 15000)
            }
            val baseOrder = when {
                day in 1..5 -> 410000.0 + (day * 12000)
                day in 6..12 -> 450000.0 + ((day % 5) * 28000)
                day in 13..20 -> 520000.0 - ((day % 4) * 20000)
                day in 21..27 -> 480000.0 + ((day % 3) * 31000)
                else -> 430000.0 + (day * 5000)
            }
            points.add(SalesDataPoint(day, baseOrder, baseEstimate))
        }
        _salesData.value = points
    }

    fun addEstimate(estimate: Estimate) {
        _estimates.update { listOf(estimate) + it }
    }

    fun updateEstimateStatus(estimateId: String, status: EstimateStatus) {
        _estimates.update { list ->
            list.map { if (it.id == estimateId) it.copy(status = status) else it }
        }
    }

    fun convertEstimateToOrder(estimate: Estimate): Order {
        val newOrder = Order(
            orderNumber = "AF-2026-${(100..999).random()}",
            clientName = estimate.clientName,
            clientPhone = estimate.clientPhone,
            deliveryAddress = estimate.clientAddress,
            itemsSummary = estimate.items.joinToString(", ") { "${it.title} (x${it.quantity})" },
            totalAmount = estimate.grandTotal,
            advancePaid = estimate.grandTotal * 0.4, // Standard 40% booking advance
            status = OrderStatus.CONFIRMED,
            orderDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            expectedDeliveryDate = "Within 15 Days"
        )
        _orders.update { listOf(newOrder) + it }
        updateEstimateStatus(estimate.id, EstimateStatus.CONVERTED)
        return newOrder
    }

    fun addOrder(order: Order) {
        _orders.update { listOf(order) + it }
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        _orders.update { list ->
            list.map { if (it.id == orderId) it.copy(status = status) else it }
        }
    }

    fun recordOrderPayment(orderId: String, amount: Double) {
        _orders.update { list ->
            list.map {
                if (it.id == orderId) {
                    it.copy(advancePaid = (it.advancePaid + amount).coerceAtMost(it.totalAmount))
                } else it
            }
        }
    }

    fun addVisitor(visitor: Visitor) {
        _visitors.update { listOf(visitor) + it }
    }

    fun updateVisitorStatus(visitorId: String, status: VisitorStatus) {
        _visitors.update { list ->
            list.map { if (it.id == visitorId) it.copy(status = status) else it }
        }
    }

    fun addCarpenter(carpenter: Carpenter) {
        _carpenters.update { listOf(carpenter) + it }
    }

    fun awardCarpenterPoints(carpenterId: String, points: Int, cashback: Double) {
        _carpenters.update { list ->
            list.map {
                if (it.id == carpenterId) {
                    it.copy(
                        totalPoints = it.totalPoints + points,
                        pendingCashback = it.pendingCashback + cashback
                    )
                } else it
            }
        }
    }

    fun toggleCheckIn(staffName: String, location: String, note: String) {
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val isNowCheckedIn = !_isCheckedIn.value
        _isCheckedIn.value = isNowCheckedIn
        val record = CheckInRecord(
            staffName = staffName,
            timestamp = "$now Today",
            location = location,
            type = if (isNowCheckedIn) "Staff Check-In" else "Staff Check-Out",
            note = note
        )
        _checkIns.update { listOf(record) + it }
        _lastCheckInTime.value = "$now Today"
    }

    companion object {
        @Volatile
        private var INSTANCE: AravalliRepository? = null

        fun getInstance(): AravalliRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AravalliRepository().also { INSTANCE = it }
            }
        }
    }
}
