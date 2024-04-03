package com.example.emmaleegomez_simpleboggle

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.util.Objects
import kotlin.math.abs
import kotlin.math.sqrt

class BottomFragment : Fragment() {
    private lateinit var newGameButton : Button
    private lateinit var scoreTextView : TextView
    private val viewModel: BoggleViewModel by activityViewModels()
    private lateinit var sensorManager: SensorManager
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var score : Int = 0

    private val cooldown : Long = 3000
    private var sensorEnabled = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.bottom_fragment, container, false)
        newGameButton = view.findViewById(R.id.newGame)
        scoreTextView = view.findViewById(R.id.score)
        scoreTextView.text = getString(R.string.score, 0)

        viewModel.changeScore.observe(viewLifecycleOwner) { item ->
            val newScore = score + item
            score = if (newScore >= 0) { newScore } else { 0 }
            scoreTextView.text = getString(R.string.score, score)
        }

        newGameButton.setOnClickListener {
            viewModel.clearGame(true)
            score = 0
            scoreTextView.text = getString(R.string.score, score)
        }

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)
            .registerListener(sensorListener, sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        return view
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (!sensorEnabled) {
                return
            }

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            val delta: Float = currentAcceleration - lastAcceleration

            if (abs(delta) > 3.5) {
                viewModel.clearGame(true)
                score = 0
                scoreTextView.text = getString(R.string.score, score)

                sensorEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    sensorEnabled = true
                }, cooldown)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
}