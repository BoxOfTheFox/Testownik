package com.example.testownik.database

import androidx.room.*
import com.example.testownik.ui.title.BaseState

@Entity(tableName = "base_table", indices = [Index(value = ["title"], unique = true)])
data class Base(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val state: BaseState
)

@Entity(
    tableName = "questions_table",
    foreignKeys = [ForeignKey(
        entity = Base::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("baseId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val baseId: Long
)

@Entity(tableName = "answers_table",
    foreignKeys = [ForeignKey(
        entity = Question::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("questionId"),
        onDelete = ForeignKey.CASCADE
    )])
data class Answer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val isCorrect: Boolean,

    val questionId: Long
)

data class BaseWithQuestions(
    @Embedded
    val base: Base,

    @Relation(
        parentColumn = "id",
        entityColumn = "baseId"
    )
    val questions: List<Question>
)

data class QuestionWithAnswers(
    @Embedded
    val question: Question,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val answers: List<Answer>
)