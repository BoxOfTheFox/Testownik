package com.example.testownik.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testownik.ui.title.BaseState

@Dao
interface BaseDatabaseDao {
    @Insert
    suspend fun insertBase(base: Base): Long

    @Query("SELECT * FROM base_table")
    fun getBases(): LiveData<List<Base>>

    @Update
    suspend fun updateBase(base: Base)

    @Delete
    suspend fun deleteBase(base: Base)

    @Query("DELETE FROM base_table WHERE state=:state")
    suspend fun deleteBasesWithState(state: BaseState = BaseState.DELETED)

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions_table WHERE id = :key")
    suspend fun getQuestionFromId(key: Long): Question

    @Query("UPDATE base_table SET state=:state WHERE id=:id")
    suspend fun updateBaseStateWithId(id: Long, state: BaseState)

    @Query("UPDATE base_table SET state=:targetState WHERE state=:currentState")
    suspend fun updateBasesWithStateToState(currentState: BaseState, targetState: BaseState)

    @Query("UPDATE base_table SET state=:state WHERE id in (:id)")
    suspend fun updateBasesStateWithIds(id: List<Long>, state: BaseState)

    @Query("DELETE FROM base_table WHERE state=:state")
    suspend fun clearBasesWithState(state: BaseState)

    @Query("DELETE FROM questions_table")
    suspend fun clearQuestions()

    @Query("SELECT COUNT() FROM base_table WHERE id = :key")
    fun getNumberOfQuestions(key: Long): LiveData<Long>

    @Transaction
    @Query("SELECT * FROM base_table WHERE id = :baseId")
    suspend fun getBaseWithQuestions(baseId: Long): BaseWithQuestions

    @Transaction
    @Query("SELECT * FROM base_table WHERE state=:state")
    fun getAllBaseWithQuestions(state: BaseState = BaseState.ACTIVE): LiveData<List<BaseWithQuestions>>

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