package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CashflowScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PosScreen
import com.example.ui.screens.StockMenuScreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.ui.viewmodel.PosViewModel

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainApp(viewModel: PosViewModel = viewModel()) {
    val currentTab by viewModel.currentNavTab.collectAsState()

    val navItems = listOf(
        NavItem(
            title = "Kasir POS",
            selectedIcon = Icons.Filled.PointOfSale,
            unselectedIcon = Icons.Outlined.PointOfSale
        ),
        NavItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart
        ),
        NavItem(
            title = "Stock Menu",
            selectedIcon = Icons.Filled.Inventory2,
            unselectedIcon = Icons.Outlined.Inventory2
        ),
        NavItem(
            title = "Cashflow",
            selectedIcon = Icons.Filled.AccountBalanceWallet,
            unselectedIcon = Icons.Outlined.AccountBalanceWallet
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Slate50,
        bottomBar = {
            Surface(
                color = White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                shadowElevation = 4.dp
            ) {
                NavigationBar(
                    containerColor = White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.background(White)
                ) {
                    navItems.forEachIndexed { index, item ->
                        val isSelected = currentTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setNavTab(index) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlueLight,
                                unselectedIconColor = Slate400,
                                unselectedTextColor = Slate500
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> PosScreen(viewModel = viewModel)
                1 -> DashboardScreen(viewModel = viewModel)
                2 -> StockMenuScreen(viewModel = viewModel)
                3 -> CashflowScreen(viewModel = viewModel)
            }
        }
    }
}
