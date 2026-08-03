package com.example.gdutcanteenapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gdutcanteenapp.data.model.Canteen
import com.example.gdutcanteenapp.data.model.Dish
import com.example.gdutcanteenapp.data.model.FavoriteDish
import com.example.gdutcanteenapp.data.model.User
import com.example.gdutcanteenapp.data.model.Window

@Database(
    entities = [Canteen::class, Window::class, Dish::class, User::class, FavoriteDish::class],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun canteenDao(): CanteenDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gdut_canteen_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}