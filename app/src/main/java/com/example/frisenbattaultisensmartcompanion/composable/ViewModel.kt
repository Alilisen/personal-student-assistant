package com.example.frisenbattaultisensmartcompanion.composable

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frisenbattaultisensmartcompanion.database.AppDatabase
import com.example.frisenbattaultisensmartcompanion.database.QuestionResponse
import com.example.frisenbattaultisensmartcompanion.database.QuestionResponseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val questionResponseDao: QuestionResponseDao =
        AppDatabase.getDatabase(application).questionResponseDao()

    private var _chatMessages = MutableStateFlow<List<QuestionResponse>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    init {
        viewModelScope.launch {
            _chatMessages.value = questionResponseDao.getAllQuestionsAndResponses()
        }
    }

    fun addMessage(question: String, response: String) {
        val newEntry = QuestionResponse(question = question, response = response)
        viewModelScope.launch {
            questionResponseDao.insert(newEntry)
        }
    }
    fun deleteMessage(id: Int) {
        viewModelScope.launch {
            // Supprimer le message de la base de données
            questionResponseDao.deleteById(id)

            // Mettre à jour la liste des messages
            _chatMessages.value = questionResponseDao.getAllQuestionsAndResponses()
        }
    }
    var text = mutableStateOf("")
        private set
}



