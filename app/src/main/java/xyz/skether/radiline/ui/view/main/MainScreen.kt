package xyz.skether.radiline.ui.view.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xyz.skether.radiline.ui.view.main.page.FavoritesPage
import xyz.skether.radiline.ui.view.main.page.GenresPage
import xyz.skether.radiline.ui.view.main.page.TopPage

private sealed class Page(
    val route: String,
    val content: @Composable (NavBackStackEntry) -> Unit,
    //@StringRes val titleId: Int
) {
    object Favorites : Page("favorites", { FavoritesPage() })
    object Top : Page("top", { TopPage() })
    object Genres : Page("genres", { GenresPage() })
}

private val allPages = listOf(Page.Favorites, Page.Top, Page.Genres)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainScreen() {
    val navc = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navc.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                allPages.forEach { page ->
                    NavigationBarItem(
                        label = { Text(text = page.route) },
                        icon = { Icon(Icons.Default.Add, null) },
                        selected = currentDestination?.route == page.route,
                        onClick = {
                            navc.navigate(page.route) {
                                popUpTo(navc.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navc,
            startDestination = Page.Top.route,
            Modifier.padding(innerPadding)
        ) {
            allPages.forEach { page ->
                composable(page.route, content = page.content)
            }
        }
    }
}