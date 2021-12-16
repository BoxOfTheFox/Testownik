package com.example.testownik.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BaseDatabaseDao {
    @Insert
    suspend fun insertBase(base: Base): Long

    @Query("SELECT * FROM base_table")
    fun getBases(): LiveData<List<Base>>

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions_table WHERE id = :key")
    suspend fun getQuestionFromId(key: Long): Question

    @Query("DELETE FROM questions_table")
    suspend fun clearQuestions()

    @Query("SELECT COUNT() FROM base_table WHERE id = :key")
    fun getNumberOfQuestions(key: Long): LiveData<Long>

    @Transaction
    @Query("SELECT * FROM base_table WHERE id = :baseId")
    suspend fun getBaseWithQuestions(baseId: Long): BaseWithQuestions

//    todo rozbić
    @Transaction
    @Query("SELECT * FROM base_table")
    fun getAllBaseWithQuestions(): LiveData<List<BaseWithQuestions>>

    @Query("SELECT * FROM questions_table ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuestion(): Question?

    @Query("SELECT * FROM questions_table WHERE baseId = :baseId")
    suspend fun getQuestionsFromBase(baseId: Long): List<Question>

    @Insert
    suspend fun insertAnswer(answer: Answer): Long

    @Transaction
    @Query("SELECT * FROM questions_table WHERE id = :questionId")
    suspend fun getQuestionWithAnswers(questionId: Long): QuestionWithAnswers

    @Query("SELECT * FROM base_table")
    fun getAllBases(): LiveData<List<Base>>
}