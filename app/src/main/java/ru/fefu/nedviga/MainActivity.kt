package ru.fefu.nedviga

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.nedviga.data.network.App
import ru.fefu.nedviga.data.viewmodels.ProfileViewModel
import ru.fefu.nedviga.navigation.SetupNavigation
import ru.fefu.nedviga.ui.theme.NedvigaTheme
import ru.fefu.nedviga.data.viewmodels.SharedViewModel
import ru.fefu.nedviga.ui.screens.login.LoginActivity
import ru.fefu.nedviga.util.channelID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        setContent {
            NedvigaTheme {
                navController = rememberNavController()
                SetupNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
                Flag(this@MainActivity, sharedViewModel, viewModel)
            }
        }

        createNotificationChannel(this@MainActivity)

        viewModel.logoutUser
            .onEach {
                    App.INSTANCE.sharedPreferences.edit().remove("token").apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
            }
            .launchIn(lifecycleScope)
    }
}

@Composable
fun Flag(mContext: Context, sharedViewModel: SharedViewModel, viewModel: ProfileViewModel) {
    val flag by sharedViewModel.flag.observeAsState()
    if (flag!!) {
        sharedViewModel._flag.postValue(false)
        viewModel.logout()
    }
}

private fun createNotificationChannel(context: Context) {
    val name = "Notification Channel"
    val desc = "A Description of the Channel"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelID, name, importance)
    channel.description = desc
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

