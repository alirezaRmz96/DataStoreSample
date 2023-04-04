package com.example.datastoresample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.datastoresample.ui.UserDataStore
import com.example.datastoresample.ui.theme.DataStoreSampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStoreSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Main()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Main() {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val tokenValue = remember {
        mutableStateOf(TextFieldValue())
    }
    var checkToken = false
    val tokenValueCheck = remember {
        mutableStateOf(TextFieldValue())
    }
    val store = UserDataStore(context = context)
    val tokenText = store.getAccessToken.collectAsState(initial = "")

    Column(
        modifier = Modifier.clickable { keyboardController?.hide() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "DataStorage Example", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = tokenText.value)

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = tokenValue.value, onValueChange = { tokenValue.value = it })

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                store.saveToken(tokenValue.value.text)
            }
        }) {
            Text(text = "Update Token")
        }

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = tokenValueCheck.value, onValueChange = { tokenValueCheck.value = it })

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                store.getAccessToken.collectLatest {

                    checkToken = it == tokenValueCheck.value.text
                    checkLogin(checkToken)
                }
            }

        }) {
            Text(text = "Login")
        }
    }
}

fun checkLogin(value: Boolean) {
    if (value)
        Log.d("messi", "Login: happened")
    else
        Log.d("messi", "Login: Don't happened")
}
