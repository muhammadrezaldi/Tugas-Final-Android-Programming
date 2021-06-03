package com.example.tugas_final_reza.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MahasiswaEntity::class], exportSchema = false, version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun mahasiswaDao(): MahasiswaDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempinstance = INSTANCE
            if(tempinstance != null) {
                return tempinstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}