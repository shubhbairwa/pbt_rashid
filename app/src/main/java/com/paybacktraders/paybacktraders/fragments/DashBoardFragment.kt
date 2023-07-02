package com.paybacktraders.paybacktraders.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.activity.AddClientActivity
import com.paybacktraders.paybacktraders.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {
    private lateinit var _binding:FragmentDashBoardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDashBoardBinding.bind(view)

        _binding.addFloatingBtn.setOnClickListener {
            var intent = Intent(requireContext(), AddClientActivity::class.java)
            startActivity(intent)
        }

    }
}