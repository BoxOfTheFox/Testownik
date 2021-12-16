package com.example.testownik.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Base::class, Question::class, Answer::class],
    version = 2,
    exportSchema = false
)
abstract class BaseDatabase : RoomDatabase() {
    abstract val BaseDatabaseDao: BaseDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: BaseDatabase? = null

        fun getInstance(context: Context): BaseDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BaseDatabase::class.java,
                        "base_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}