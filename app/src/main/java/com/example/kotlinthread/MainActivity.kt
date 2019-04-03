package com.example.kotlinthread

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock.sleep
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.random

const val KotlinLog = "kotlinTest"

class MainActivity : AppCompatActivity() {
    var rabbitProgress = 0
    var turtleProgress = 0
    /*
    private val mHandler = object: Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what) {
                1 -> {
                    btn1.text = "Start"
                    txv1.text = "Start"
                }
                2 -> {
                    btn1.text = "End"
                    txv1.text = "End"
                }
                else -> Log.d(KotlinLog, "otherwise")
            }
        }
    }
*/
    private val mHandle = Handler(Handler.Callback {
        when (it.what) {
            1 -> {
                seekBar1.progress = rabbitProgress
            }
            else -> Log.d(KotlinLog, "otherwise")
        }
        if(rabbitProgress >= 100 && turtleProgress < 100) {
            Toast.makeText(this, "Rabbit is the winner.", Toast.LENGTH_LONG).show()
            btn1.isEnabled = true
        }
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            btn1.isEnabled = false
            rabbitProgress = 0
            turtleProgress = 0
            seekBar1.progress = rabbitProgress
            seekBar2.progress = turtleProgress
            runThread()
            runAsyncTask()
        }
    }

    fun runThread() {
        Thread(Runnable {
            while (rabbitProgress <= 100 && turtleProgress < 100) {
                sleep(100)
                rabbitProgress += (random() * 3).toInt()
                val msg = Message()
                msg.what = 1
                mHandle.sendMessage(msg)
            }
        }).start()
    }

    fun runAsyncTask() {
        object: AsyncTask<Void, Int, Boolean>() {
            override fun doInBackground(vararg params: Void?): Boolean {
                while(turtleProgress <= 100 && rabbitProgress < 100) {
                    sleep(100)
                    turtleProgress += (random() * 3).toInt()
                    publishProgress(turtleProgress)
                }
                return true
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                seekBar2.progress = turtleProgress
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                Toast.makeText(this@MainActivity, "Turtle is the winner.", Toast.LENGTH_LONG).show()
                btn1.isEnabled = true
            }
        }.execute()
    }
}
