package xyz.skether.radiline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import xyz.skether.radiline.ui.theme.RadilineTheme
import xyz.skether.radiline.ui.view.MainScreen
import xyz.skether.radiline.ui.view.previewMainScreenDataHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependencies = application.app().dependencies
        setContent {
            RadilineTheme {
                MainScreen(dependencies.mainScreenDataHolder)
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
