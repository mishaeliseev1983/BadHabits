package com.melyseev.badhabits

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val textView = findViewById<TextView>(R.id.mainText)
        val buttonReset = findViewById<Button>(R.id.resetButton)

        val viewModel = (application as ProvideViewModel).provideViewModel()

        viewModel.observe(this){
            it.apply( textView, buttonReset)
        }

        buttonReset.setOnClickListener {
            viewModel.reset()
        }
        viewModel.init(savedInstanceState == null)


//        val time = sharedPref.getLong("time", -1)
//
//        val resetBtn =  findViewById<Button>(R.id.resetButton)
//        resetBtn.setOnClickListener {
//            sharedPref.edit().putLong("time",  System.currentTimeMillis()).apply()
//            textView.text = "0"
//            resetBtn.visibility = View.GONE
//        }
//
//        var days = 0
//        if(time == -1L){
//            sharedPref.edit().putLong("time", System.currentTimeMillis()).apply()
//            textView.text = "0"
//            resetBtn.visibility = View.GONE
//        }else{
//            val diffSeconds = (System.currentTimeMillis() - time) / 1_000
//            days = (diffSeconds / (24 * 3_600)).toInt()
//            if(days == 0)
//                resetBtn.visibility = View.VISIBLE
//            textView.text = days.toString()
//        }





    }
}