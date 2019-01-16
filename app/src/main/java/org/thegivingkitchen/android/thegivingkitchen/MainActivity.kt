package org.thegivingkitchen.android.thegivingkitchen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import androidx.navigation.Navigation
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        // val fragment =
        // this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        val fragment = this.supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0)
        val controller = Navigation.findNavController(this, R.id.nav_host_fragment)
        if (fragment is BackPressedListener) {
            if (!(fragment as BackPressedListener).onBackPressed()) {
                super.onBackPressed()
            }
        } else if(!controller.popBackStack()) {
            finish()
        }

        // (fragment as? BackPressedListener)?.onBackPressed()?.not()?.let {
        //     Toast.makeText(baseContext, "main activity says: " + it, Toast.LENGTH_SHORT).show()
        //     super.onBackPressed()
        // }

    }
}
