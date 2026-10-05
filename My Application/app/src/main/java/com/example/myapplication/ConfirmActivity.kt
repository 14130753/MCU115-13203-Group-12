package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class ConfirmActivity : ComponentActivity() {

    companion object {
        const val EXTRA_MAIN_COURSE = "extra_main_course"
        const val EXTRA_SIDE_DISH = "extra_side_dish"
        const val EXTRA_DRINK = "extra_drink"
        const val EXTRA_SUBMITTED = "extra_submitted"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainCourse = intent.getStringExtra(EXTRA_MAIN_COURSE) ?: ""
        val sideDish = intent.getStringExtra(EXTRA_SIDE_DISH) ?: ""
        val drink = intent.getStringExtra(EXTRA_DRINK) ?: ""

        setContent {
            MyApplicationTheme {
                ConfirmScreen(
                    mainCourse = mainCourse,
                    sideDish = sideDish,
                    drink = drink,
                    onSubmitSuccess = { finalMain, finalSide, finalDrink ->
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_MAIN_COURSE, finalMain)
                            putExtra(EXTRA_SIDE_DISH, finalSide)
                            putExtra(EXTRA_DRINK, finalDrink)
                            putExtra(EXTRA_SUBMITTED, true)
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
fun ConfirmScreen(
    mainCourse: String,
    sideDish: String,
    drink: String,
    onSubmitSuccess: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val isAllSelected = mainCourse.isNotBlank() && sideDish.isNotBlank() && drink.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("確認餐點", fontSize = 20.sp) },
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
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "目前選擇的餐點內容：",
                    style = MaterialTheme.typography.titleMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SelectionItemRow(
                            label = "主餐",
                            value = if (mainCourse.isNotBlank()) mainCourse else "未選擇"
                        )
                        HorizontalDivider()
                        SelectionItemRow(
                            label = "副餐",
                            value = if (sideDish.isNotBlank()) sideDish else "未選擇"
                        )
                        HorizontalDivider()
                        SelectionItemRow(
                            label = "飲料",
                            value = if (drink.isNotBlank()) drink else "未選擇"
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (isAllSelected) {
                            showDialog = true
                        } else {
                            // Find unselected items for better Toast message context
                            val unselected = mutableListOf<String>()
                            if (mainCourse.isBlank()) unselected.add("主餐")
                            if (sideDish.isBlank()) unselected.add("副餐")
                            if (drink.isBlank()) unselected.add("飲料")

                            val missingText = unselected.joinToString("、")
                            Toast.makeText(
                                context,
                                "請選擇 $missingText！",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirm (確認)", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                }

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("返回修改", fontSize = 16.sp, modifier = Modifier.padding(4.dp))
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("餐點確認 (Confirm)", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("您點選的餐點如下：")
                    Text("• 主餐：$mainCourse")
                    Text("• 副餐：$sideDish")
                    Text("• 飲料：$drink")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("是否確定提交訂單？", fontWeight = FontWeight.Medium)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onSubmitSuccess(mainCourse, sideDish, drink)
                    }
                ) {
                    Text("Submit (提交)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun SelectionItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            color = if (value == "未選擇") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontWeight = if (value == "未選擇") FontWeight.Normal else FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
