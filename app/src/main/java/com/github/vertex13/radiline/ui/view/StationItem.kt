package com.github.vertex13.radiline.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.vertex13.radiline.R

@Composable
fun StationItem(
    station: StationItemData,
    cardColors: CardColors = CardDefaults.cardColors(),
    onClick: () -> Unit
) {
    Card(
        colors = cardColors,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val (name, genre, bitrate) = createRefs()

            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                },
            )
            Text(
                text = station.genre,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Right,
                modifier = Modifier.constrainAs(genre) {
                    top.linkTo(name.bottom)
                    start.linkTo(parent.start)
                },
            )
            Text(
                text = stringResource(R.string.bitrate, station.bitrate),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.constrainAs(bitrate) {
                    top.linkTo(name.bottom)
                    linkTo(
                        start = genre.end,
                        startMargin = 8.dp,
                        end = parent.end,
                        bias = 1f
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    StationItem(station = previewStationItemData(8392), onClick = {})
}
