package com.example.hw5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ResultFragment : Fragment() {
    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_result, container, false)
        val tvResultStatus = view.findViewById<TextView>(R.id.tvResultStatus)

        viewModel.resultStatus.observe(viewLifecycleOwner) { status ->
            tvResultStatus.text = status
        }

        return view
    }
}
