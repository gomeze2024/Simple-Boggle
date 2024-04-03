package com.example.emmaleegomez_simpleboggle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.topFragment, TopFragment())
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.bottomFragment, BottomFragment())
        }
    }
}