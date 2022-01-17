package com.example.testownik.ui.title

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testownik.database.Answer
import com.example.testownik.database.Base
import com.example.testownik.database.BaseDatabaseDao
import com.example.testownik.database.Question
import kotlinx.coroutines.*
import timber.log.Timber
import java.nio.charset.Charset

class TitleViewModel(val database: BaseDatabaseDao,application: Application) :
    AndroidViewModel(application) {

    val bases = database.getAllBaseWithQuestions()

    fun insertBase(context: Context, uri: Uri, base: Base) {
        viewModelScope.launch {
            try {
                val baseId = async { insertBase(base) }
                DocumentFile.fromTreeUri(context, uri)
                    ?.listFiles()
                    ?.forEach {
                        context
                            .contentResolver
                            .openInputStream(it.uri)
                            ?.readBytes()
                            ?.toString(Charset.defaultCharset())
                            ?.lines()
                            ?.let { lst ->
                                val deferredQuestionId = async {
                                    insertQuestion(
                                        Question(
                                            text = lst[1],
                                            baseId = baseId.await()
                                        )
                                    )
                                }
                                insertAnswers(lst.subList(2, lst.lastIndex), lst[0], deferredQuestionId)
                            }
                    }
            } catch (e: SQLiteConstraintException) {
                Timber.e(e)
                Toast.makeText(context, "Ta baza już istnieje!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun insertBase(base: Base): Long{
        return withContext(Dispatchers.IO) {
            database.insertBase(base)
        }
    }

    private suspend fun updateBase(id: Long, state: BaseState){
        return withContext(Dispatchers.IO){
            database.updateBaseStateWithId(id,state)
        }
    }

    fun setBaseDeleted(id: Long){
        viewModelScope.launch {
            updateBase(id, BaseState.DELETED)
        }
    }

    private suspend fun updateBases(ids: List<Long>, state: BaseState){
        return withContext(Dispatchers.IO){
            database.updateBasesStateWithIds(ids, state)
        }
    }

    fun setBasesDeleted(ids: List<Long>){
        viewModelScope.launch {
            updateBases(ids, BaseState.DELETED)
        }
    }

    private suspend fun clearBasesWithState(state: BaseState){
        return withContext(Dispatchers.IO){
            database.clearBasesWithState(state)
        }
    }

    fun clearBasesWithDeleteState(){
        viewModelScope.launch {
            clearBasesWithState(BaseState.DELETED)
        }
    }

    private suspend fun setBasesWithStateToState(currentState: BaseState, targetState: BaseState){
        return withContext(Dispatchers.IO){
            database.updateBasesWithStateToState(currentState, targetState)
        }
    }

    fun setBasesDeletedToActive(){
        viewModelScope.launch {
            setBasesWithStateToState(BaseState.DELETED, BaseState.ACTIVE)
        }
    }

//    private suspend fun

    private suspend fun insertQuestion(question: Question): Long{
        return withContext(Dispatchers.IO){
            database.insertQuestion(question)
        }
    }

    private suspend fun insertAnswers(answerStrings: List<String>, isCorrectString: String, deferredQuestionId: Deferred<Long>){
        withContext(Dispatchers.IO){
            answerStrings.forEachIndexed { index, str ->
                database.insertAnswer(
                    Answer(
                        text = str,
                        isCorrect = isCorrectString[1 + index] == '1',
                        questionId = deferredQuestionId.await()
                    )
                )
            }
        }
    }
}