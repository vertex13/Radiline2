package xyz.skether.radiline

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import xyz.skether.radiline.system.AppContext
import xyz.skether.radiline.ui.theme.RadilineTheme
import xyz.skether.radiline.ui.view.MainScreen
import xyz.skether.radiline.ui.view.previewMainScreenDataHolder

class MainActivity : ComponentActivity() {
    companion object {
        fun newPendingIntent(appContext: AppContext): PendingIntent {
            val intent = Intent(appContext.value, MainActivity::class.java)
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE else 0
            return PendingIntent.getActivity(
                appContext.value, 885883, intent, flags
            )
        }
    }

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
