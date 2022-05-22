package wook.pool.board.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import wook.pool.board.R
import wook.pool.board.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityMainBinding

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomBar()
    }

    private fun initBottomBar() {
        _binding.viewBottomBar.apply {
            onItemSelected = {

            }
            onItemReselected = {

            }
        }
    }

}