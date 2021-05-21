package com.ingoox.clicktochat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.ingoox.clicktochat.ui.theme.OpenChatTheme
import java.net.URLEncoder

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
    val context = LocalContext.current
    var phone by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var link by rememberSaveable { mutableStateOf("") }

    fun onPhoneChange(value: String) {
        phone = value
        link = createLink(phone, message)
    }

    fun onMessageChange(value: String) {
        message = value
        link = createLink(phone, message)
    }

    fun clearPhoneHandler() {
        phone = ""
        link = createLink(phone, message)
    }

    fun clearMessageHandler() {
        message = ""
        link = createLink(phone, message)
    }

    Content(
        phone = phone,
        message = message,
        link = link,
        onPhoneChange = { onPhoneChange(it) },
        onMessageChange = { onMessageChange(it) },
        openChatHandler = { openLink(link, context) },
        shareLinkHandler = { shareLink(link, context) },
        clearMessageHandler = { clearMessageHandler() },
        clearPhoneHandler = { clearPhoneHandler() }
    )
}

@Composable
fun Content(
    phone: String,
    message: String,
    link: String,
    onPhoneChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    openChatHandler: () -> Unit,
    shareLinkHandler: () -> Unit,
    clearMessageHandler: () -> Unit,
    clearPhoneHandler: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val focusRequester = FocusRequester()

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 1.dp
        ) {
            Column {
                Text(
                    text = stringResource(R.string.card_title),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
                )
                Text(
                    text = link,
                    modifier = Modifier
                        .padding(16.dp, 8.dp, 16.dp, 8.dp)
                        .animateContentSize(),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.size(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { onPhoneChange(it) },
            label = { Text(stringResource(R.string.phone_label)) },
            trailingIcon = {
                if (phone.isNotEmpty()) {
                    ClearTextIcon(
                        onClick = { clearPhoneHandler() },
                        contentDescription = stringResource(R.string.clear_phone_description)
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = message,
            onValueChange = { onMessageChange(it) },
            label = { Text(stringResource(R.string.message_label)) },
            trailingIcon = {
                if (message.isNotEmpty()) {
                    ClearTextIcon(
                        onClick = { clearMessageHandler() },
                        contentDescription = stringResource(R.string.clear_message_description)
                    )
                }
            },
            maxLines = 6
        )
        Spacer(Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = { shareLinkHandler() }) {
                Text(stringResource(R.string.share_link_button))
            }
            Spacer(Modifier.size(8.dp))

            Button(onClick = { openChatHandler() }) {
                Text(stringResource(R.string.open_chat_button))
            }
        }
    }
}

@Composable
fun ClearTextIcon(
    onClick: () -> Unit,
    enabled: Boolean = true,
    contentDescription: String = "Clear text"
) {
    IconButton(
        onClick = { onClick() },
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.Rounded.Clear,
            contentDescription = contentDescription
        )
    }
}

fun createLink(phone: String, message: String): String {
    val phoneDigits = phone.filter { it.isDigit() }
    if (phoneDigits.isBlank() and message.isBlank()) return ""
    var link = "https://wa.me/"
    if (phoneDigits.isNotBlank()) {
        link += phoneDigits
    }
    if (message.isNotBlank()) {
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        link += "?text=$encodedMessage"
    }
    return link
}

fun openLink(link: String, context: Context) {
    if (link.isNotBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(context, intent, null)
    }
}

fun shareLink(link: String, context: Context) {
    if (link.isNotBlank()) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(intent, "")
        startActivity(context, chooserIntent, null)
    }
}
