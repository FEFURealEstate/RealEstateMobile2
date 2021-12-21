package ru.fefu.nedviga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.fefu.nedviga.navigation.SetupNavigation
import ru.fefu.nedviga.ui.theme.NedvigaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NedvigaTheme {
                navController = rememberNavController()
                SetupNavigation(navController = navController)
            }
        }
    }
}