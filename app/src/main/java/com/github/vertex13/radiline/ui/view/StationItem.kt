package com.github.vertex13.radiline.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.vertex13.radiline.R
import com.github.vertex13.radiline.glsurfacecompose.GLSurface
import com.github.vertex13.radiline.ui.gl.GLVector
import com.github.vertex13.radiline.ui.gl.WavesRenderer

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
        val padding = 8.dp
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            val (name, genre, waves) = createRefs()

            if (station.isPlaying) {
                val bgColor = MaterialTheme.colorScheme.secondaryContainer
                GLSurface(
                    renderer = WavesRenderer(
                        appContext = station.getAppContext(),
                        params = WavesRenderer.Params(GLVector(bgColor.asFloatArray()))
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(waves) {
                            linkTo(
                                top = parent.top,
                                bottom = parent.bottom,
                                start = parent.start,
                                end = parent.end
                            )
                        }
                )
            }

            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = padding, start = padding, end = padding)
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                    },
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = padding, start = padding, end = padding)
                    .constrainAs(genre) {
                        top.linkTo(name.bottom)
                        linkTo(start = parent.start, end = parent.end)
                    }
            ) {
                Text(
                    text = station.genre,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.bitrate, station.bitrate),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    modifier = Modifier.padding(start = padding)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    StationItem(station = previewStationItemData(8392), onClick = {})
}

private fun Color.asFloatArray(): FloatArray {
    return floatArrayOf(red, green, blue, alpha)
}
