package com.example.gdutcanteenapp.ui.canteen.canteenlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.gdutcanteenapp.databinding.FragmentCanteenBinding
import com.example.gdutcanteenapp.ui.base.BaseFragment

//作为navigation的容器
class CanteenFragment : BaseFragment<FragmentCanteenBinding>() {

        override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCanteenBinding {
            return FragmentCanteenBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun observeData() {
    }
}