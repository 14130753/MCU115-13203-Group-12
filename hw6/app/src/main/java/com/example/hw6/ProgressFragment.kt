package com.example.hw6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hw6.databinding.FragProgressBinding

class ProgressFragment : Fragment() {

    private var _binding: FragProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                WorkStatus.IDLE -> {
                    binding.circularProgress.isIndeterminate = false
                    binding.circularProgress.progress = 0
                    binding.tvProgress.text = "Idle"
                }
                WorkStatus.PREPARING -> {
                    binding.circularProgress.isIndeterminate = true
                    binding.tvProgress.text = "Preparing..."
                }
                WorkStatus.WORKING -> {
                    binding.circularProgress.isIndeterminate = false
                    val p = viewModel.progress.value ?: 0
                    binding.circularProgress.progress = p
                    binding.tvProgress.text = "Working... $p%"
                }
                WorkStatus.FINISHED -> {
                    binding.circularProgress.isIndeterminate = false
                    binding.circularProgress.progress = 100
                    binding.tvProgress.text = "背景工作結束!"
                }
                WorkStatus.CANCELED -> {
                    binding.circularProgress.isIndeterminate = false
                    binding.tvProgress.text = "Canceled"
                }
                null -> {}
            }
        }

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            if (viewModel.status.value == WorkStatus.WORKING) {
                binding.circularProgress.isIndeterminate = false
                binding.circularProgress.progress = progress
                binding.tvProgress.text = "Working... $progress%"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}