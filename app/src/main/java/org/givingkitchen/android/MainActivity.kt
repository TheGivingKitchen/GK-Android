package org.givingkitchen.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.givingkitchen.android.util.FragmentBackPressedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        // Check if currently visible fragment wants to override the back press
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val currentFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment as? FragmentBackPressedListener
        currentFragment?.let {
            if (currentFragment.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}
