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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.circularProgressView.progress = state.progress
            binding.circularProgressView.centerText = state.progressText
            binding.tvProgressStatus.text = state.progressText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
