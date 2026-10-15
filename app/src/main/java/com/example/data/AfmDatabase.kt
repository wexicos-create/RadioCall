package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChannelEntity::class, MessageEntity::class, CallLogEntity::class, AiLogEntity::class], version = 1, exportSchema = false)
abstract class AfmDatabase : RoomDatabase() {
    abstract fun afmDao(): AfmDao

    companion object {
        @Volatile
        private var INSTANCE: AfmDatabase? = null

        fun getDatabase(context: Context): AfmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AfmDatabase::class.java,
                    "afm_secure_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
