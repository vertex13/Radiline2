package xyz.skether.radiline.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Play
import xyz.skether.radiline.ui.FavoriteStationItemData
import xyz.skether.radiline.ui.TopStationItemDataWithoutFavorites
import xyz.skether.radiline.ui.asState
import xyz.skether.radiline.ui.theme.FavoriteStationColor

class MainScreenDataHolder(
    val favoriteStationItemData: FavoriteStationItemData,
    val topStationItemDataWithoutFavorites: TopStationItemDataWithoutFavorites,
    val play: Play,
    val playerInfoDataHolder: PlayerInfoDataHolder,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(dataHolder: MainScreenDataHolder) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val favCardColors = CardDefaults.cardColors(containerColor = FavoriteStationColor)
            val favorites by dataHolder.favoriteStationItemData().asState()
            val top by dataHolder.topStationItemDataWithoutFavorites().asState()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 150.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (favorites.isNotEmpty()) {
                    item("favorites_title", "title") {
                        Title(text = stringResource(id = R.string.favorites))
                    }
                }
                items(favorites, key = { it.id }, contentType = { "favorites_item" }) {
                    StationItem(it, favCardColors) { dataHolder.play(it.name) }
                }
                if (top.isNotEmpty()) {
                    item("top_title", "title") {
                        Title(text = stringResource(id = R.string.top))
                    }
                }
                items(top, key = { it.id }, contentType = { "top_item" }) {
                    StationItem(it) { dataHolder.play(it.name) }
                }
            }

            PlayerInfo(
                dataHolder = dataHolder.playerInfoDataHolder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(8.dp)
    )
}


@Preview
@Composable
private fun Preview() {
    MainScreen(previewMainScreenDataHolder())
}
