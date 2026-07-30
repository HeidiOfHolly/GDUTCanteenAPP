package com.example.gdutcanteenapp

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.gdutcanteenapp.adapter.ViewPagerAdapter
import com.example.gdutcanteenapp.data.local.database.AppDatabase
import com.example.gdutcanteenapp.data.local.database.CanteenDao
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.databinding.ActivityMainBinding
import com.example.gdutcanteenapp.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.jvm.java

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var canteenDao: CanteenDao

    override fun getViewBinding(): ActivityMainBinding? {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setupViewPager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 直接在这里初始化数据库
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "gdut_canteen_database"
        ).build()

        canteenDao = database.canteenDao()

        // 直接测试
        testDatabase()
    }

    private fun testDatabase() {
        lifecycleScope.launch {
            // 插入
            val canteen = Canteen(
                canteenId = 1,
                canteenName = "测试食堂",)
            canteenDao.insertCanteen(canteen)
            Log.d("MainActivity", "✅ 插入成功")

            // 查询
            val list = canteenDao.getAllCanteens()
            println("✅ 查询结果: $list")

            // 删除
            canteenDao.deleteCanteenById(1)
            println("✅ 删除成功")
        }
    }

    override fun observeData() {
        // TODO: 添加观察数据的逻辑
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this, emptyList())//emptyList()占位，具体的fragment列表待定
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            binding.tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.black)
                //具体颜色待定，根据页面设置
            )
            // TODO: 设置 tab 文字
        }.attach()
    }
}