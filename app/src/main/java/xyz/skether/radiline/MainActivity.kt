package xyz.skether.radiline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import xyz.skether.radiline.dependency.Dependencies
import xyz.skether.radiline.ui.data.previewMainScreenDataHolder
import xyz.skether.radiline.ui.theme.RadilineTheme
import xyz.skether.radiline.ui.view.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadilineTheme {
                MainScreen(Dependencies.mainScreenDataHolder)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    RadilineTheme {
        MainScreen(previewMainScreenDataHolder())
    }
}