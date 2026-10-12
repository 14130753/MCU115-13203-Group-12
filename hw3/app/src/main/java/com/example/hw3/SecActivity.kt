package com.example.hw3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hw3.ui.theme.Hw3Theme

class SecActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val receivedText = intent.getStringExtra("message") ?: "This is the text string from the page one"

        setContent {
            Hw3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SecScreen(
                        receivedText = receivedText,
                        modifier = Modifier.padding(innerPadding),
                        onReturnClick = { replyText ->
                            val resultIntent = Intent().apply {
                                putExtra("reply", replyText)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SecScreen(
    receivedText: String,
    modifier: Modifier = Modifier,
    onReturnClick: (String) -> Unit,
) {
    var replyInput by remember { mutableStateOf("Hello I have received your message") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "第二頁",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "接收一頁傳來的文字:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = receivedText,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = replyInput,
            onValueChange = { replyInput = it },
            label = { Text("請輸入回傳到第一頁的文字") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onReturnClick(replyInput) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("回傳並返回")
        }
    }
}
