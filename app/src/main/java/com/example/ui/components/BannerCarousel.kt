package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class BannerItem(
    val title: String,
    val subtitle: String,
    val badgeHindi: String,
    val brandLogoText: String,
    val backgroundBrush: Brush,
    val accentColor: Color
)

@Composable
fun BannerCarousel(
    onBannerClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val banners = listOf(
        BannerItem(
            title = "Aravalli Premium Furniture",
            subtitle = "Sagwan Teakwood & Waterproof WPC Plywood",
            badgeHindi = "भारत का\nNo. 1",
            brandLogoText = "REAL PLAST",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF88A28E), Color(0xFF6F8B75))
            ),
            accentColor = Color(0xFFFFD54F)
        ),
        BannerItem(
            title = "Grand Showroom Festive Collection",
            subtitle = "Solid Wood Handcrafted Living & Dining Sets",
            badgeHindi = "राजस्थानी\nसागवान",
            brandLogoText = "ARAVALLI",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF5D4037), Color(0xFF8D6E63))
            ),
            accentColor = Color(0xFFFFB300)
        ),
        BannerItem(
            title = "Modular Kitchens & Carcass",
            subtitle = "German Hardware with 10 Years Replacement Guarantee",
            badgeHindi = "10 वर्ष\nवारंटी",
            brandLogoText = "MODULAR",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF263238), Color(0xFF37474F))
            ),
            accentColor = Color(0xFF00E676)
        ),
        BannerItem(
            title = "Carpenter Rewards & Loyalty Club",
            subtitle = "Earn ₹500 Instant Cashback on every 10 Sheets",
            badgeHindi = "शिल्पी\nसम्मान",
            brandLogoText = "REWARDS",
            backgroundBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF004D40), Color(0xFF00897B))
            ),
            accentColor = Color(0xFFFFD54F)
        )
    )

    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-advance banner carousel every 4 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("banner_pager")
        ) { page ->
            val banner = banners[page]
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(page) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(banner.backgroundBrush)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Column: The yellow "भारत का No.1" badge + "REAL PLAST" style logo
                        Column(
                            modifier = Modifier.width(130.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Yellow Badge
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFFC107),
                                shadowElevation = 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        color = Color(0xFFD32F2F),
                                        shape = RoundedCornerShape(2.dp)
                                    ) {
                                        Text(
                                            text = banner.badgeHindi.lines().firstOrNull() ?: "",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                    Text(
                                        text = banner.badgeHindi.lines().getOrNull(1) ?: "",
                                        color = Color(0xFF212121),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Brand Logo Plaque
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = Color(0xFFFFEB3B),
                                shadowElevation = 1.dp
                            ) {
                                Text(
                                    text = banner.brandLogoText,
                                    color = Color(0xFFD32F2F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Right Column: Title and Subtitle
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = banner.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = banner.subtitle,
                                color = Color.White.copy(alpha = 0.88f),
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = banner.accentColor,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ISO Certified Quality",
                                    color = banner.accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dot indicators matching screenshot
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.indices.forEach { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color(0xFF2E7D32) else Color(0xFFB0BEC5)
                        )
                )
            }
        }
    }
}
