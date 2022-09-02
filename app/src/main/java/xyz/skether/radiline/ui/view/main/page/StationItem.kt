package xyz.skether.radiline.ui.view.main.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.data.StationItemData
import xyz.skether.radiline.ui.data.previewStationItemData
import xyz.skether.radiline.ui.theme.StarColor

@Composable
fun StationItem(station: StationItemData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val (star, name, genre, track, play, bitrate) = createRefs()

            if (station.inFavorites) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = StarColor,
                    modifier = Modifier
                        .rotate(12f)
                        .scale(4f)
                        .constrainAs(star) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                )
            }
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(parent.top)
                },
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.constrainAs(play) {
                    visibility = if (station.isPlaying) Visibility.Visible else Visibility.Gone
                    top.linkTo(name.bottom)
                }
            )
            Text(
                text = station.currentTrack,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.constrainAs(track) {
                    top.linkTo(name.bottom)
                    start.linkTo(play.end)
                }
            )
            Text(
                text = station.genre,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Right,
                modifier = Modifier.constrainAs(genre) {
                    top.linkTo(track.bottom)
                    linkTo(
                        start = parent.start,
                        end = bitrate.start,
                        endMargin = 8.dp,
                        bias = 1f,
                    )
                },
            )
            Text(
                text = stringResource(R.string.station_item_bitrate, station.bitrate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                modifier = Modifier.constrainAs(bitrate) {
                    top.linkTo(track.bottom)
                    end.linkTo(parent.end)
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
