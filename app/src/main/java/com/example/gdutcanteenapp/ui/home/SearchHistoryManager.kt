package com.example.gdutcanteenapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import com.example.gdutcanteenapp.data.remote.TokenManager
import com.google.gson.Gson


interface HistoryChangeListener {
    fun onHistoryChanged(history: List<String>)
}

class SearchHistoryManager (context: Context) {
    private val appContext = context.applicationContext
    private val KEY_HISTORY = "history"
    private val MAX_SIZE = 10
    private val listeners = mutableListOf<HistoryChangeListener>()

    // 按当前登录用户隔离搜索历史，切换账号后各自持有独立的搜索历史
    private fun prefs(): SharedPreferences =
        appContext.getSharedPreferences("search_history_" + TokenManager.getUserId(), Context.MODE_PRIVATE)

    fun getHistory(): List<String> {
        val jason = prefs().getString(KEY_HISTORY, "[]")
        return Gson().fromJson(jason, Array<String>::class.java).toList()
    }

    fun addHistory(keyword: String) {
        if(keyword.isBlank()) return
        val currentHistory = getHistory().toMutableList()
        if (currentHistory.contains(keyword)) {
            currentHistory.remove(keyword)
        }
        currentHistory.add(0, keyword)
        if (currentHistory.size > MAX_SIZE) {
            currentHistory.removeAt(currentHistory.size - 1)
        }
        prefs().edit().putString(KEY_HISTORY, Gson().toJson(currentHistory)).apply()
    }

    fun removeHistory(keyword: String) {
        val currentHistory = getHistory().toMutableList()
        if (currentHistory.remove(keyword)) {
            prefs().edit().putString(KEY_HISTORY, Gson().toJson(currentHistory)).apply()
        }
    }



    private fun notifyListeners(history: List<String>) {
        listeners.forEach{
            it.onHistoryChanged(history)
        }
    }



}