package com.example.emmaleegomez_simpleboggle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoggleViewModel : ViewModel() {
    private val mutableChangeScore = MutableLiveData<Int>()
    val changeScore: LiveData<Int> get() = mutableChangeScore

    private val mutableClearGame = MutableLiveData<Boolean>()
    val clearGame: LiveData<Boolean> get() = mutableClearGame

    fun changeScore(score: Int) {
        mutableChangeScore.value = score
    }

    fun clearGame(game: Boolean) {
        mutableClearGame.value = game
    }
}