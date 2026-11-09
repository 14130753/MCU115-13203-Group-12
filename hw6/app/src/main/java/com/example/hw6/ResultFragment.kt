package com.example.hw6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hw6.databinding.FragResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvResult.text = when (status) {
                WorkStatus.IDLE -> "Idle"
                WorkStatus.PREPARING -> "Preparing..."
                WorkStatus.WORKING -> "Working..."
                WorkStatus.FINISHED -> "背景工作結束!"
                WorkStatus.CANCELED -> "Canceled"
                null -> "Idle"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}