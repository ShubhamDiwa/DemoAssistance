package com.vasyerp.demoassistance

import android.app.assist.AssistContent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.vasyerp.demoassistance.databinding.ActivityMainBinding
import org.json.JSONObject




class MainActivity : AppCompatActivity() {
    /**
     * Before
     */
    enum class Type(val nameId: Int) {
        START(R.string.activity_timer);

        companion object {
            const val PARAM_TYPE = "type"

            /**
             * @return a FitActivity.Type that matches the given name
             */
            fun find(type: String): Type {
                return values().find { it.name.equals(other = type, ignoreCase = true) } ?: START
            }
        }
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var isTimerRunning = false
    private var startTime: Long = 0
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.e("Shubham++", "onCreate: ", )
        /**
         * Oncreate
         */

        intent?.handleIntent()  // here the Intent will start from BIIS
       binding.startButton.setOnClickListener {
            if (!isTimerRunning) {
                startTtimer()
            } else {
                stopTimer()
            }
     }

    }

    private fun startTtimer() {
        isTimerRunning = true
        binding.startButton.text = "Stop Timer"
        startTime = SystemClock.elapsedRealtime()

        handler.postDelayed(timerRunnable, 0)
    }

    private fun stopTimer() {
        isTimerRunning = false
        binding.startButton.text = "Start Timer"
        handler.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
            val minutes = (elapsedTime / 60000).toInt()
            val seconds = ((elapsedTime / 1000) % 60).toInt()
            val timeString = String.format("%02d:%02d", minutes, seconds)
            binding.timerTextView.text = timeString
            handler.postDelayed(this, 1000) // Update every 1 second
        }
    }

    /**
     * Google Assistance provided
     */

    override fun onProvideAssistContent(outContent: AssistContent) {
        super.onProvideAssistContent(outContent)
        Log.e("Shubham", "onProvideAssistContent:==== ", )
        // JSON-LD object based on Schema.org structured data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // This is just an example, more accurate information should be provided
            outContent.structuredData = JSONObject()
                .put("@type", "exerciseType")
                .put("name", "exercise.name")
                .toString()
        }
    }


    // HAmdle the intent on Type of Receiver
    private fun Intent.handleIntent() {
        when (action) {

            // When the BII is matched, Intent.Action_VIEW will be used
            Intent.ACTION_VIEW -> handleIntent(data)
            // Otherwise start the app as you would normally do.

            //showDefaultView()
        }
    }

    /**
     *
     */
    private fun handleIntent(data: Uri?) {
        Log.e("Shubham+++", "handleIntent: yaha se ")
        // path is normally used to indicate which view should be displayed
        // i.e https://fit-actions.firebaseapp.com/start?exerciseType="Running" -> path = "start"
        Log.e("TAG", "handleIntent:${handleIntent(data)} ")
        val startTtimer = intent?.extras?.getString(BiiIntents.START_TIMER)
        val stopExercise = intent?.extras?.getString(BiiIntents.STOP_TIMER)

        if (startTtimer != null) {
            val type = MainActivity.Type.find(startTtimer)


            startTtimer()
        }

    }



}
