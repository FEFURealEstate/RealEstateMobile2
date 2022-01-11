package ru.fefu.nedviga.ui.screens.login

import androidx.compose.foundation.Image
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.nedviga.MainActivity
import ru.fefu.nedviga.R
import ru.fefu.nedviga.data.models.User
import ru.fefu.nedviga.data.models.UserToken
import ru.fefu.nedviga.data.network.App
import ru.fefu.nedviga.data.viewmodels.LoginViewModel
import ru.fefu.nedviga.data.viewmodels.SharedViewModel
import ru.fefu.nedviga.ui.theme.LoginTheme
import ru.fefu.nedviga.ui.theme.welcomeTextColor
import ru.fefu.nedviga.ui.theme.imageBackgroundColor
import ru.fefu.nedviga.util.Result

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setContent {
            LoginTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginScreen(this@LoginActivity, viewModel)
                }
            }
        }

        if (App.INSTANCE.sharedPreferences.getString("token", null) !== null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        viewModel.dataFlow
            .onEach {
                if (it is Result.Success<User>) {
                    App.INSTANCE.sharedPreferences.edit().putString("token", it.result.token)
                        .apply()
                    App.INSTANCE.sharedPreferences.edit().putInt("agentId", it.result.agentId)
                    sharedViewModel.setAllTasks(it.result.agentId)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else if (it is Result.Error<User>) {
                    Toast.makeText(this, it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)

    }
}

@Composable
fun LoginScreen(mContext: Context, viewModel: LoginViewModel) {

    Column(modifier = Modifier.padding(32.dp)) {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        Text(text = "Welcome,", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = MaterialTheme.colors.welcomeTextColor)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Sign in to continue", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .size(375.dp).clip(shape = RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.imageBackgroundColor),
                painter = painterResource(R.drawable.ic_login_people),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text(text = "Email") },
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation =
                if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                val eye =
                    if (passwordVisibility) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = eye, "")
                }
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
                viewModel.login(login, password)
            },
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        ) {
            Text("Login", fontWeight = FontWeight.Bold)
        }
    }
}
