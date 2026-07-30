package com.example.gdutcanteenapp.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.gdutcanteenapp.R

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observerDate()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun initViews()
    abstract fun observerDate()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}