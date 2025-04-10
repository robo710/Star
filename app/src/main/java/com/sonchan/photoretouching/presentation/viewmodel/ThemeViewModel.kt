package com.sonchan.photoretouching.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonchan.photoretouching.data.datastore.preference.ThemePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreference: ThemePreference
) : ViewModel(){
    private val _isDarkTheme = MutableStateFlow<Boolean>(false)

    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            themePreference.isDarkTheme.collect{
                _isDarkTheme.value = it
            }
        }
    }

    fun toggleTheme(){
        viewModelScope.launch {
            themePreference.setDarkTheme(!_isDarkTheme.value)
        }
    }
}