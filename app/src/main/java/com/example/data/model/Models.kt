package com.example.data.model

import java.util.UUID

enum class EstimateStatus(val displayName: String) {
    DRAFT("Draft"),
    SENT("Quotation Sent"),
    APPROVED("Approved"),
    CONVERTED("Converted to Order")
}

data class EstimateItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val woodType: String = "Burma Teak",
    val dimensions: String = "6ft x 3ft",
    val quantity: Int = 1,
    val unitPrice: Double,
    val laborCost: Double = 0.0,
    val discountPercent: Double = 0.0
) {
    val totalAmount: Double
        get() = (quantity * unitPrice + laborCost) * (1 - discountPercent / 100.0)
}

data class Estimate(
    val id: String = UUID.randomUUID().toString(),
    val estimateNumber: String,
    val clientName: String,
    val clientPhone: String,
    val clientAddress: String,
    val items: List<EstimateItem>,
    val gstPercent: Double = 18.0,
    val extraDiscountPercent: Double = 0.0,
    val status: EstimateStatus = EstimateStatus.DRAFT,
    val createdAt: String,
    val notes: String = ""
) {
    val subtotal: Double
        get() = items.sumOf { it.totalAmount }

    val discountAmount: Double
        get() = subtotal * (extraDiscountPercent / 100.0)

    val gstAmount: Double
        get() = (subtotal - discountAmount) * (gstPercent / 100.0)

    val grandTotal: Double
        get() = (subtotal - discountAmount) + gstAmount
}

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    CONFIRMED("Order Confirmed", 0),
    CUTTING("Wood Cutting & Carpentry", 1),
    SANDING_POLISH("Sanding & Polish", 2),
    UPHOLSTERY("Cushion & Upholstery", 3),
    READY_DISPATCH("Ready for Dispatch", 4),
    DELIVERED("Delivered & Installed", 5)
}

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val orderNumber: String,
    val clientName: String,
    val clientPhone: String,
    val deliveryAddress: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val advancePaid: Double,
    val status: OrderStatus = OrderStatus.CONFIRMED,
    val orderDate: String,
    val expectedDeliveryDate: String,
    val carpenterAssigned: String = "Master Ramesh Suthar"
) {
    val balanceDue: Double
        get() = (totalAmount - advancePaid).coerceAtLeast(0.0)

    val isFullyPaid: Boolean
        get() = balanceDue <= 0.0
}

enum class VisitorStatus(val displayName: String) {
    NEW_LEAD("New Lead"),
    CATALOG_SHARED("Catalog Shared"),
    VISITED("Showroom Trial Done"),
    FOLLOW_UP("Follow-up Scheduled"),
    CONVERTED("Converted"),
    CLOSED("Closed")
}

data class Visitor(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val phone: String,
    val requirement: String, // e.g. "7-Seater Teak Sofa + Center Table"
    val budgetRange: String, // e.g. "₹80,000 - ₹1,20,000"
    val visitDate: String,
    val followUpDate: String,
    val status: VisitorStatus = VisitorStatus.NEW_LEAD,
    val notes: String = ""
)

data class Carpenter(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val phone: String,
    val workshopArea: String,
    val specialty: String, // e.g. "Carving & Modular Kitchen"
    val totalPoints: Int = 0,
    val pendingCashback: Double = 0.0,
    val completedProjectsCount: Int = 0,
    val tier: String = "Gold Partner"
)

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String,
    val price: Double,
    val mrp: Double,
    val dimensions: String,
    val woodType: String,
    val warrantyYears: Int = 10,
    val description: String,
    val inStock: Boolean = true
)

data class CheckInRecord(
    val id: String = UUID.randomUUID().toString(),
    val staffName: String = "Harish Patel (Sales & Production)",
    val timestamp: String,
    val location: String = "Aravalli Furniture Showroom, NH-8 Main Road",
    val type: String = "Morning Opening Check-In",
    val note: String = "Showroom opened, daily carpenter orders verified"
)

data class GreetingTemplate(
    val id: String,
    val occasion: String,
    val title: String,
    val message: String,
    val bannerTag: String,
    val accentColorHex: Long
)

data class SalesDataPoint(
    val day: Int,
    val orderAmount: Double,
    val estimateAmount: Double
)

data class ShowroomBanner(
    val id: String,
    val headline: String,
    val subline: String,
    val badgeText: String,
    val promoCode: String? = null
)
