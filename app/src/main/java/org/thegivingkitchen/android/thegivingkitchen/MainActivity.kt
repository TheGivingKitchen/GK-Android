package org.thegivingkitchen.android.thegivingkitchen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // todo: preserve state in each tab
    // todo: [bug] clicking active tab icon refreshes the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav_home.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private val navController: NavController by lazy {
        findNavController(this, R.id.fragmentContainer_home)
    }
}
