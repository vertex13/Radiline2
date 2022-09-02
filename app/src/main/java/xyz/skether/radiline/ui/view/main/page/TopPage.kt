package xyz.skether.radiline.ui.view.main.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.skether.radiline.domain.Play
import xyz.skether.radiline.ui.ObserveTopStationItemData
import xyz.skether.radiline.ui.asState
import xyz.skether.radiline.ui.data.previewTopPageDataHolder

class TopPageDataHolder(
    val observeTopStationItemData: ObserveTopStationItemData,
    val play: Play,
)

@Composable
fun TopPage(
    dataHolder: TopPageDataHolder,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val stations by dataHolder.observeTopStationItemData().asState()
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    ) {
        items(stations, key = { it.id }) {
            StationItem(it) { dataHolder.play(it.id) }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TopPage(previewTopPageDataHolder())
}