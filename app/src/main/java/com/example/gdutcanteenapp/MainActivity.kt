package com.example.gdutcanteenapp

import androidx.core.content.ContextCompat
import com.example.gdutcanteenapp.adapter.ViewPagerAdapter
import com.example.gdutcanteenapp.databinding.ActivityMainBinding
import com.example.gdutcanteenapp.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding? {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setupViewPager()
    }

    override fun observeData() {
        // TODO: 添加观察数据的逻辑
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            binding.tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.black)
                //具体颜色待定，根据页面设置
            )
            //设置tab文字
            when (position) {
                0 -> tab.text = "食堂"//暂时只有一个界面
            }
        }.attach()
    }
}
