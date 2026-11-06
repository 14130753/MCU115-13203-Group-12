package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedMainCourse.observe(viewLifecycleOwner) { main ->
            binding.tvSelectedMainCourse.text = "主餐：" + (main ?: "未選擇")
        }

        viewModel.selectedSideDish.observe(viewLifecycleOwner) { side ->
            binding.tvSelectedSideDish.text = "副餐：" + (side ?: "未選擇")
        }

        viewModel.selectedBeverage.observe(viewLifecycleOwner) { beverage ->
            binding.tvSelectedBeverage.text = "飲料：" + (beverage ?: "未選擇")
        }

        binding.btnConfirm.setOnClickListener {
            val main = viewModel.selectedMainCourse.value
            val side = viewModel.selectedSideDish.value
            val beverage = viewModel.selectedBeverage.value

            if (main != null && side != null && beverage != null) {
                // 如果主餐,副餐,飲料都有選 -> 跳出 AlertDialog 裏面有所選項目和 Submit (提交) Button
                val message = "主餐：$main\n副餐：$side\n飲料：$beverage"
                AlertDialog.Builder(requireContext())
                    .setTitle("確認點餐項目")
                    .setMessage(message)
                    .setPositiveButton("Submit (提交)") { _, _ ->
                        viewModel.submitOrder()
                        (activity as? MainActivity)?.navigateToMainScreen()
                    }
                    .setNegativeButton("取消", null)
                    .show()
            } else {
                // 如果有任何一項沒點 -> 跳出 Toast 並顯示要選擇主餐,副餐,飲料的文字
                val unselected = mutableListOf<String>()
                if (main == null) unselected.add("主餐")
                if (side == null) unselected.add("副餐")
                if (beverage == null) unselected.add("飲料")

                val toastText = "請選擇" + unselected.joinToString("、") + "！"
                Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}