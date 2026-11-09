package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _selectedMainCourse = MutableLiveData<String?>(null)
    val selectedMainCourse: LiveData<String?> = _selectedMainCourse

    private val _selectedSideDish = MutableLiveData<String?>(null)
    val selectedSideDish: LiveData<String?> = _selectedSideDish

    private val _selectedBeverage = MutableLiveData<String?>(null)
    val selectedBeverage: LiveData<String?> = _selectedBeverage

    private val _submittedMeal = MutableLiveData<String?>(null)
    val submittedMeal: LiveData<String?> = _submittedMeal

    fun selectMainCourse(item: String?) {
        _selectedMainCourse.value = item
    }

    fun selectSideDish(item: String?) {
        _selectedSideDish.value = item
    }

    fun selectBeverage(item: String?) {
        _selectedBeverage.value = item
    }

    fun submitOrder() {
        val main = _selectedMainCourse.value
        val side = _selectedSideDish.value
        val beverage = _selectedBeverage.value
        if (main != null && side != null && beverage != null) {
            _submittedMeal.value = "主餐：$main\n副餐：$side\n飲料：$beverage"
        }
    }
}