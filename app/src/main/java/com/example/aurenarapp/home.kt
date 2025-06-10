package com.example.aurenarapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class home : Fragment() {

    private lateinit var timeText: TextView
    private lateinit var startPause: Button
    private lateinit var reset: Button
    private lateinit var pair: Button
    private lateinit var timer: View
    private var t = 0
    private var flag = true
    private var isPaired = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runtime: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeText = view.findViewById(R.id.timeText)
        startPause = view.findViewById(R.id.startPause)
        reset = view.findViewById(R.id.reset)
        pair = view.findViewById(R.id.pair)
        timer = view.findViewById(R.id.timer)
        hideTimer()


        runtime = Runnable {
            if (!flag) {
                t++
                updateTimerDisplay()
                handler.postDelayed(runtime, 1000)
            }
        }

        startPause.setOnClickListener {
            if (!flag) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        reset.setOnClickListener {
            resetTimer()
        }

        pair.setOnClickListener {
            if (!isPaired) {
                pairMode()
            } else {
                Toast.makeText(context, "Already Paired", Toast.LENGTH_SHORT).show()
            }
        }
    }

        private fun hideTimer(){
            if(isPaired){
                timer.visibility = View.VISIBLE
                pair.visibility = View.GONE
            }

            else {
                timer.visibility = View.GONE
                pair.visibility = View.VISIBLE
            }

        updateTimerDisplay() // Initialize with 00:00:00
    }

    private fun startTimer() {
        flag = false
        startPause.text = "Pause"
        handler.post(runtime)
    }

    private fun pairMode(){
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle("Pair Mode")
                .setMessage("This is a pair mode message.")
                .setPositiveButton("Confirm") { _, _ ->
                    Toast.makeText(context, "Starting Pairing..." , Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(context, "Pairing Cancelled" , Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun pauseTimer() {
        flag = true
        startPause.text = "Start"
        handler.removeCallbacks(runtime)
    }

    private fun updateTimerDisplay() {
        val hr = t / 3600
        val min = (t % 3600) / 60
        val sec = t % 60
        val time = String.format("%02d:%02d:%02d", hr, min, sec)
        timeText.text = time
    }

    private fun resetTimer() {
        pauseTimer()
        t = 0
        updateTimerDisplay()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runtime)
    }
}
