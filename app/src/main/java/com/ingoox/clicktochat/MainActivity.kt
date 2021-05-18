package com.ingoox.clicktochat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingoox.clicktochat.ui.theme.OpenChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var phone by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    // Event handlers

    Content(
        phone = phone,
        message = message,
        onPhoneChange = { phone = it },
        onMessageChange = { message = it })
}

@Composable
fun Content(
    phone: String,
    message: String,
    onPhoneChange: (String) -> Unit,
    onMessageChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp
        ) {
            Text(
                "wa.me/$phone",
                Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { onPhoneChange(it) },
            label = { Text("Phone") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = message,
            onValueChange = { onMessageChange(it) },
            label = { Text("Message") }
        )
        Spacer(Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("GENERATE LINK")
            }
            Spacer(Modifier.size(8.dp))
            Button(onClick = { /*onLinkHandle?*/ }) {
                Text("OPEN CHAT")
            }
        }
    }
}

fun createLink(phoneNumber: String, message: String): String {
    return ""
}

fun openLink(link: String) {

}

fun shareLink(link: String) {

}
