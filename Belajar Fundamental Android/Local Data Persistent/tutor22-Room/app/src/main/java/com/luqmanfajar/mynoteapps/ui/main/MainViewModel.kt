package com.luqmanfajar.mynoteapps.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.luqmanfajar.mynoteapps.database.Note
import com.luqmanfajar.mynoteapps.repository.NoteRepository

class MainViewModel(application: Application): ViewModel() {

    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes(): LiveData<List<Note>> = mNoteRepository.getAllNotes()
}