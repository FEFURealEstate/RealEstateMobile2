package ru.fefu.nedviga.ui.screens.login

import androidx.compose.foundation.Image
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import ru.fefu.nedviga.R
import ru.fefu.nedviga.ui.theme.LoginTheme
import ru.fefu.nedviga.ui.theme.welcomeTextColor
import ru.fefu.nedviga.ui.theme.imageBackgroundColor

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginScreen(this@LoginActivity)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(mContext: Context) {
    Column(modifier = Modifier.padding(32.dp)) {
        HeaderText()
        Spacer(modifier = Modifier.height(10.dp))
        Icon()
        Spacer(modifier = Modifier.height(10.dp))
        EmailTextField()
        Spacer(modifier = Modifier.height(4.dp))
        PasswordTextField()
        Spacer(modifier = Modifier.height(32.dp))
        ButtonLogin()
    }
}

@Composable
private fun HeaderText() {
    Text(text = "Welcome,", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = MaterialTheme.colors.welcomeTextColor)
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Sign in to continue", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.LightGray)
}

@Composable
private fun Icon() {
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
}

@Composable
private fun EmailTextField() {
    var email by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text(text = "Email") },
        textStyle = MaterialTheme.typography.body1,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
private fun PasswordTextField() {
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Password") },
        textStyle = MaterialTheme.typography.body1,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
            if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation()
        ,
        trailingIcon = {
            val eye =
                if (passwordVisibility) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = eye, "")
            }
        }
    )
}

@Composable
private fun ButtonLogin() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
    ) {
        Text("Login", fontWeight = FontWeight.Bold)
    }
}
