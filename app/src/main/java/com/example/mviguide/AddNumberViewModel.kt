package com.example.mviguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.Exception

//if I want to communicate to Two coroutine
//you can use Flow Or Channel

class AddNumberViewModel : ViewModel() {

    //Channel > to send intents from activity to viewModel
    val intentChannel=Channel<MainIntent>(Channel.UNLIMITED)

    //State Flow > to send data from ViewModel to MainActivity
    //State Flow to insure that data not repeated and not render again
    //Encapsulation
    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.idle)
    val state:StateFlow<MainViewState> get() = _viewState

    private var number = 0

    init {
        processIntent()
    }

    //process
    private fun processIntent(){
        viewModelScope.launch {
            //get data as flow and collect this data
            intentChannel.consumeAsFlow().collect {
                when(it){
                    is MainIntent.AddNumber -> addNumber()
                }
            }
        }
    }

    //reduceResult
    private fun addNumber(){
        viewModelScope.launch {
            _viewState.value=
                try {
                    MainViewState.Number(++number)
                }catch (ex:Exception){
                    MainViewState.Error(ex.message!!)
                }
        }
    }

}