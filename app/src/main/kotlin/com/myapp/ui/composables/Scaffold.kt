package com.example.myapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.myapp.Route.Route
import com.myapp.ui.composables.OurNavigationBar


@Composable
fun OurScaffold(
    onNavigate: (route: Route) -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            OurNavigationBar(onNavigate)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}