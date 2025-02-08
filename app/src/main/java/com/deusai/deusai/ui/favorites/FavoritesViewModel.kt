package com.deusai.deusai.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {

        value = "You don't have any favorites yet."
    }
    val text: LiveData<String> = _text
}