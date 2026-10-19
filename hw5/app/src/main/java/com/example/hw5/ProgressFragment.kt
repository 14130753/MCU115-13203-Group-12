package com.example.hw5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment() {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_progress, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.circularProgressBar)
        val tvPercentage = view.findViewById<TextView>(R.id.tvPercentage)
        val tvStatus = view.findViewById<TextView>(R.id.tvProgressStatus)

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            tvPercentage.text = "$progress%"
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            tvStatus.text = status
        }

        return view
    }
}
