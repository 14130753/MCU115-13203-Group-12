package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainCourseActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MAIN_COURSE = "extra_main_course"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialSelection = intent.getStringExtra(EXTRA_MAIN_COURSE) ?: ""

        setContent {
            MyApplicationTheme {
                MainCourseScreen(
                    initialSelection = initialSelection,
                    onConfirm = { selectedCourse ->
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_MAIN_COURSE, selectedCourse)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCourseScreen(
    initialSelection: String,
    onConfirm: (String) -> Unit,
    onBack: () -> Unit
) {
    val options = listOf(
        "經典雙層牛肉堡 ($130)",
        "勁辣咔啦雞腿堡 ($120)",
        "日式厚切豬排堡 ($110)",
        "黃金鱈魚堡 ($105)",
        "健康植物肉蔬食堡 ($100)"
    )

    var selectedOption by remember { mutableStateOf(initialSelection) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("選擇主餐", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "請選擇一項主餐：",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(options) { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOption = option },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedOption == option)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedOption == option),
                                    onClick = { selectedOption = option }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("返回")
                }

                Button(
                    onClick = { onConfirm(selectedOption) },
                    enabled = selectedOption.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("確認選擇")
                }
            }
        }
    }
}
