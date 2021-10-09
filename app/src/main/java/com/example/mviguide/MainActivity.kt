package com.example.mviguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var numberTv:TextView
    lateinit var addNumBtn:Button

    private val viewModel:AddNumberViewModel by lazy {
        ViewModelProviders.of(this).get(AddNumberViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberTv=findViewById(R.id.tv_number)
        addNumBtn=findViewById(R.id.addNumBtn)

        //send
        addNumBtn.setOnClickListener {
            lifecycleScope.launch {
            viewModel.intentChannel.send(MainIntent.AddNumber)
            }
        }

        //render
        render()
    }

    private fun render(){
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when(it){
                    is MainViewState.idle -> numberTv.text="Idle"
                    is MainViewState.Number -> numberTv.text = it.num.toString()
                    is MainViewState.Error -> numberTv.text=it.error
                }

            }

        }
    }

}