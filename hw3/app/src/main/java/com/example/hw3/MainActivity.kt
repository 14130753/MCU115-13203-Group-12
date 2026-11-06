package com.example.hw3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hw3.ui.theme.Hw3Theme

class MainActivity : ComponentActivity() {
    private var receivedReply by mutableStateOf("尚無回傳訊息")

    private val startSecActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val reply = result.data?.getStringExtra("reply")
            if (reply != null) {
                receivedReply = reply
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hw3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        receivedReply = receivedReply,
                        modifier = Modifier.padding(innerPadding),
                        onSwitchPageClick = {
                            val intent = Intent(this, SecActivity::class.java).apply {
                                putExtra("message", "This is the text string from the page one")
                            }
                            startSecActivityLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    receivedReply: String,
    modifier: Modifier = Modifier,
    onSwitchPageClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "第一頁",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "This is the text string from the page one",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSwitchPageClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("切換頁面")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "第二頁回傳的字串: $receivedReply",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Hw3Theme {
        MainScreen(receivedReply = "Hello I have received your message") {}
    }
}
