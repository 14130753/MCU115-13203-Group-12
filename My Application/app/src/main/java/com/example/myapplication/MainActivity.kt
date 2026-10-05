package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private var selectedMainCourse = mutableStateOf("")
    private var selectedSideDish = mutableStateOf("")
    private var selectedDrink = mutableStateOf("")
    private var isOrderSubmitted = mutableStateOf(false)

    // Launchers for Sub-Activities
    private val mainCourseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val mainCourse = result.data?.getStringExtra(MainCourseActivity.EXTRA_MAIN_COURSE) ?: ""
            selectedMainCourse.value = mainCourse
        }
    }

    private val sideDishLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val sideDish = result.data?.getStringExtra(SideDishActivity.EXTRA_SIDE_DISH) ?: ""
            selectedSideDish.value = sideDish
        }
    }

    private val drinkLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val drink = result.data?.getStringExtra(DrinkActivity.EXTRA_DRINK) ?: ""
            selectedDrink.value = drink
        }
    }

    private val confirmLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val submitted = data?.getBooleanExtra(ConfirmActivity.EXTRA_SUBMITTED, false) ?: false
            if (submitted) {
                selectedMainCourse.value = data.getStringExtra(ConfirmActivity.EXTRA_MAIN_COURSE) ?: ""
                selectedSideDish.value = data.getStringExtra(ConfirmActivity.EXTRA_SIDE_DISH) ?: ""
                selectedDrink.value = data.getStringExtra(ConfirmActivity.EXTRA_DRINK) ?: ""
                isOrderSubmitted.value = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainScreen(
                    mainCourse = selectedMainCourse.value,
                    sideDish = selectedSideDish.value,
                    drink = selectedDrink.value,
                    isSubmitted = isOrderSubmitted.value,
                    onSelectMainCourse = {
                        val intent = Intent(this, MainCourseActivity::class.java).apply {
                            putExtra(MainCourseActivity.EXTRA_MAIN_COURSE, selectedMainCourse.value)
                        }
                        mainCourseLauncher.launch(intent)
                    },
                    onSelectSideDish = {
                        val intent = Intent(this, SideDishActivity::class.java).apply {
                            putExtra(SideDishActivity.EXTRA_SIDE_DISH, selectedSideDish.value)
                        }
                        sideDishLauncher.launch(intent)
                    },
                    onSelectDrink = {
                        val intent = Intent(this, DrinkActivity::class.java).apply {
                            putExtra(DrinkActivity.EXTRA_DRINK, selectedDrink.value)
                        }
                        drinkLauncher.launch(intent)
                    },
                    onGoToConfirm = {
                        val intent = Intent(this, ConfirmActivity::class.java).apply {
                            putExtra(ConfirmActivity.EXTRA_MAIN_COURSE, selectedMainCourse.value)
                            putExtra(ConfirmActivity.EXTRA_SIDE_DISH, selectedSideDish.value)
                            putExtra(ConfirmActivity.EXTRA_DRINK, selectedDrink.value)
                        }
                        confirmLauncher.launch(intent)
                    },
                    onResetOrder = {
                        selectedMainCourse.value = ""
                        selectedSideDish.value = ""
                        selectedDrink.value = ""
                        isOrderSubmitted.value = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainCourse: String,
    sideDish: String,
    drink: String,
    isSubmitted: Boolean,
    onSelectMainCourse: () -> Unit,
    onSelectSideDish: () -> Unit,
    onSelectDrink: () -> Unit,
    onGoToConfirm: () -> Unit,
    onResetOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("線上點餐系統", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Submitted Order Banner
            if (isSubmitted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🎉 訂單已成功提交！",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        HorizontalDivider()
                        Text(
                            text = "【所選餐點】",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("• 主餐：$mainCourse", style = MaterialTheme.typography.bodyLarge)
                        Text("• 副餐：$sideDish", style = MaterialTheme.typography.bodyLarge)
                        Text("• 飲料：$drink", style = MaterialTheme.typography.bodyLarge)

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onResetOrder,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("重新點餐")
                        }
                    }
                }
            } else {
                // Current Order Progress Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "目前已點餐點狀態：",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider()
                        StatusRow(label = "主餐", value = if (mainCourse.isNotBlank()) mainCourse else "未點")
                        StatusRow(label = "副餐", value = if (sideDish.isNotBlank()) sideDish else "未點")
                        StatusRow(label = "飲料", value = if (drink.isNotBlank()) drink else "未點")
                    }
                }
            }

            Text(
                text = "請依序選擇餐點：",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // 4 Sub-Screen Action Buttons
            Button(
                onClick = onSelectMainCourse,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("1. 主餐 (Main Course)", fontSize = 16.sp)
                    if (mainCourse.isNotBlank()) {
                        Text("✓ 已選擇", fontSize = 14.sp)
                    }
                }
            }

            Button(
                onClick = onSelectSideDish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("2. 副餐 (Side Dish)", fontSize = 16.sp)
                    if (sideDish.isNotBlank()) {
                        Text("✓ 已選擇", fontSize = 14.sp)
                    }
                }
            }

            Button(
                onClick = onSelectDrink,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("3. 飲料 (Drink)", fontSize = 16.sp)
                    if (drink.isNotBlank()) {
                        Text("✓ 已選擇", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGoToConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("4. 前往確認 (Confirm)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatusRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            color = if (value == "未點") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontWeight = if (value == "未點") FontWeight.Normal else FontWeight.Bold
        )
    }
}
