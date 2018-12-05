package org.thegivingkitchen.android.thegivingkitchen

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.thegivingkitchen.android.thegivingkitchen.ui.home.assistance.AssistanceFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.events.EventsFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.give.GiveFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet.SafetynetFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
