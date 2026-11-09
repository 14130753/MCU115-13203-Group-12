package com.example.hw6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hw6.databinding.FragStartBinding

class StartFragment : Fragment() {

    private var _binding: FragStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            Toast.makeText(requireContext(), "開始執行背景工作...", Toast.LENGTH_SHORT).show()
            viewModel.startWork()
            (activity as? MainActivity)?.selectTab(1)
        }

        binding.btnCancel.setOnClickListener {
            Toast.makeText(requireContext(), "中斷背景工作", Toast.LENGTH_SHORT).show()
            viewModel.cancelWork()
            (activity as? MainActivity)?.selectTab(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}