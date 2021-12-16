package com.example.testownik

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.testownik.database.BaseDatabase
import com.example.testownik.database.BaseDatabaseDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest2 {
    private lateinit var baseDao: BaseDatabaseDao
    private lateinit var db: BaseDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, BaseDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        baseDao = db.BaseDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGetNight() {
//        val question = Question(answers = listOf("true","false"),text = "question",correctAnswers = listOf("1","0"))
//        GlobalScope.launch {
//            baseDao.insert(question)
//            val tonight = baseDao.getAllQuestions().value?.get(0)
//            Assert.assertEquals(tonight, question)
//        }
//    }
}