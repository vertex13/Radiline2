package com.github.vertex13.radiline.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.vertex13.radiline.R
import com.github.vertex13.radiline.domain.*
import com.github.vertex13.radiline.ui.PlayerInfoDataValue
import com.github.vertex13.radiline.ui.asState

data class PlayerInfoData(
    val stationName: StationName,
    val currentTrack: String?,
    val playerStatus: PlayerStatus,
    val inFavorites: Boolean,
)

enum class PlayerStatus { LOADING, PLAYING, PAUSED }

class PlayerInfoDataHolder(
    val playerInfoDataValue: PlayerInfoDataValue,
    val playCurrent: PlayCurrent,
    val stop: Stop,
    val addToFavorites: AddToFavorites,
    val removeFromFavorites: RemoveFromFavorites,
)

@Composable
fun PlayerInfo(dataHolder: PlayerInfoDataHolder, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val data = dataHolder.playerInfoDataValue.asState().value ?: return
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer,
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.currentTrack ?: "",
                    style = typography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val favSize = 24.dp
                    if (data.inFavorites) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = stringResource(R.string.remove_from_favorites),
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(favSize)
                                .clickable { dataHolder.removeFromFavorites(data.stationName) }
                        )
                    } else {
                        Icon(
                            Icons.TwoTone.Star,
                            contentDescription = stringResource(R.string.add_to_favorites),
                            tint = colorScheme.inversePrimary,
                            modifier = Modifier
                                .size(favSize)
                                .clickable { dataHolder.addToFavorites(data.stationName) }
                        )
                    }

                    Text(
                        text = data.stationName,
                        style = typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            val controlsSize = 80.dp
            when (data.playerStatus) {
                PlayerStatus.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(controlsSize)
                            .padding(8.dp)
                    )
                }
                PlayerStatus.PAUSED -> {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.play),
                        modifier = Modifier
                            .size(controlsSize)
                            .clickable { dataHolder.playCurrent() }
                    )
                }
                PlayerStatus.PLAYING -> {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = stringResource(R.string.pause),
                        modifier = Modifier
                            .size(controlsSize)
                            .clickable { dataHolder.stop() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PlayerInfo(previewPlayerDataHolder())
}
