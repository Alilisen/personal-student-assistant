package com.example.frisenbattaultisensmartcompanion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questionResponse: QuestionResponse)

    @Query("SELECT * FROM question_response_table ORDER BY timestamp DESC")
    suspend fun getAllQuestionsAndResponses(): List<QuestionResponse>

    @Query("DELETE FROM question_response_table")
    suspend fun deleteAll()
}