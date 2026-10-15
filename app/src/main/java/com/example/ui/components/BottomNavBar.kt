package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VioletContainer
import com.example.ui.theme.VioletOnContainer
import com.example.ui.theme.VioletPrimary
import com.example.ui.viewmodel.Screen

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screen,
    val tag: String
)

@Composable
fun ClassMateBottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        NavItem(
            title = "Week",
            selectedIcon = Icons.Filled.CalendarMonth,
            unselectedIcon = Icons.Outlined.CalendarMonth,
            screen = Screen.WeeklyGrid,
            tag = "nav_week"
        ),
        NavItem(
            title = "Daily",
            selectedIcon = Icons.Filled.Today,
            unselectedIcon = Icons.Outlined.Today,
            screen = Screen.DailyView(com.example.data.model.AppDayOfWeek.MON),
            tag = "nav_daily"
        ),
        NavItem(
            title = "Lecturers",
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.People,
            screen = Screen.LecturerList,
            tag = "nav_lecturers"
        ),
        NavItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            screen = Screen.Settings,
            tag = "nav_settings"
        )
    )

    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = when (item.screen) {
                is Screen.WeeklyGrid -> currentScreen is Screen.WeeklyGrid
                is Screen.DailyView -> currentScreen is Screen.DailyView
                is Screen.LecturerList -> currentScreen is Screen.LecturerList
                is Screen.Settings -> currentScreen is Screen.Settings
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VioletOnContainer,
                    selectedTextColor = VioletPrimary,
                    indicatorColor = VioletContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
