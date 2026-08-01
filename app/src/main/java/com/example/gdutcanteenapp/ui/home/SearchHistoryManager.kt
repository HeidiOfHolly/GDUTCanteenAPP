package com.example.gdutcanteenapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


interface HistoryChangeListener {
    fun onHistoryChanged(history: List<String>)
}

class SearchHistoryManager (context: Context) {
    private val history :SharedPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    private val KEY_HISTORY = "history"
    private val MAX_SIZE = 5
    private val listeners = mutableListOf<HistoryChangeListener>()

    fun getHistory(): List<String> {
        val jason = history.getString(KEY_HISTORY, "[]")
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
        history.edit().putString(KEY_HISTORY, Gson().toJson(currentHistory)).apply()
    }

    fun clearHistory() {
        history.edit().remove(KEY_HISTORY).apply()
        notifyListeners(emptyList())
    }

    private fun notifyListeners(history: List<String>) {
        listeners.forEach{
            it.onHistoryChanged(history)
        }
    }



}