package com.example.mviguide

sealed class MainViewState {
    //idle
    object idle:MainViewState()
    //number
    data class Number(val num:Int):MainViewState()
    //Error
    data class Error(val error:String):MainViewState()

    //Ex >> Loading
}