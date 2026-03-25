package org.homesharing.cashbackhome.presentation.editcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.promotions_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditCategoryRoot() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.promotions_placeholder),
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1B1B1B)
        )
    }
}