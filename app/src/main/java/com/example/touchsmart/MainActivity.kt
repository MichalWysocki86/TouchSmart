package com.example.touchsmart

import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val SERVER_IP = "YOUR_PC_IP_ADDRESS" // replace with your PC's IP address
    private val SERVER_PORT = 8080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val touchpadArea = findViewById<FrameLayout>(R.id.touchpad_area)
        val leftClickButton = findViewById<Button>(R.id.btnLeftClick)
        val rightClickButton = findViewById<Button>(R.id.btnRightClick)

        touchpadArea.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> sendCoordinates(event.x, event.y)
            }
            true
        }

        leftClickButton.setOnClickListener { sendClick("LEFT_CLICK") }
        rightClickButton.setOnClickListener { sendClick("RIGHT_CLICK") }
    }

    private fun sendCoordinates(x: Float, y: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket(SERVER_IP, SERVER_PORT)
                val writer = PrintWriter(socket.getOutputStream())
                writer.write("MOVE:$x,$y\n")
                writer.flush()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendClick(action: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket(SERVER_IP, SERVER_PORT)
                val writer = PrintWriter(socket.getOutputStream())
                writer.write("$action\n")
                writer.flush()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
