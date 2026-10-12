package com.example.myapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val titles = arrayOf("主餐", "副餐", "飲料", "確認")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        viewModel.submittedMeal.observe(this) { meal ->
            if (meal != null) {
                binding.tvSubmittedMeal.text = "已提交所選餐點：\n$meal"
            } else {
                binding.tvSubmittedMeal.text = "尚未提交餐點"
            }
        }
    }

    fun navigateToMainScreen() {
        binding.viewPager.currentItem = 0
    }
}