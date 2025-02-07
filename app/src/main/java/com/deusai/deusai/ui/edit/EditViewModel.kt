package com.deusai.deusai.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Edit Fragment"
    }
    val text: LiveData<String> = _text
}