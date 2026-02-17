package com.homesharing.cashbackhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.homesharing.cashbackhome.presentation.App
import com.homesharing.cashbackhome.ui.theme.CashbackHomeTheme

class CashbackHomeAcitivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            CashbackHomeTheme {
                App()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview() {
    CashbackHomeTheme {
        App()
    }
}