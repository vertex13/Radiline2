package xyz.skether.radiline.ui.view.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.data.previewMainScreenDataHolder
import xyz.skether.radiline.ui.view.main.page.FavoritesPage
import xyz.skether.radiline.ui.view.main.page.GenresPage
import xyz.skether.radiline.ui.view.main.page.TopPage
import xyz.skether.radiline.ui.view.main.page.TopPageDataHolder

private enum class Page(
    val route: String,
    val icon: ImageVector,
    @StringRes val titleId: Int,
) {
    Favorites(
        "favorites",
        Icons.Default.Star,
        R.string.page_favorites,
    ),
    Top(
        "top",
        Icons.Default.KeyboardArrowUp,
        R.string.page_top,
    ),
    Genres(
        "genres",
        Icons.Default.List,
        R.string.page_genres,
    ),
}

class MainScreenDataHolder(
    val topPageDataHolder: TopPageDataHolder,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(dataHolder: MainScreenDataHolder) {
    var currentPage by rememberSaveable { mutableStateOf(Page.Top.route) }
    val topPageListState = rememberLazyListState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                Page.values().forEach { page ->
                    NavigationBarItem(
                        label = { Text(text = stringResource(page.titleId)) },
                        icon = { Icon(page.icon, null) },
                        selected = currentPage == page.route,
                        onClick = {
                            currentPage = page.route
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentPage) {
                Page.Favorites.route -> FavoritesPage()
                Page.Top.route -> TopPage(
                    dataHolder = dataHolder.topPageDataHolder,
                    lazyListState = topPageListState,
                )
                Page.Genres.route -> GenresPage()
                else -> error("Unsupported page [$currentPage].")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreen(previewMainScreenDataHolder())
}
