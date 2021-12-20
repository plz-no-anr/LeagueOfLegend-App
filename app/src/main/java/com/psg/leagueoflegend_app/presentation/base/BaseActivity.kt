package com.psg.leagueoflegend_app.presentation.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.psg.leagueoflegend_app.utils.AppLogger
import com.psg.leagueoflegend_app.R


abstract class BaseActivity<T: ViewDataBinding, V: BaseViewModel>(@LayoutRes val res: Int): AppCompatActivity() {
    lateinit var binding: T
    abstract val TAG: String
    abstract val  viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLogger.i(TAG,"onCreate")
        binding = DataBindingUtil.setContentView(this, res)
    }

    open fun setToolbar(toolbar: Toolbar){
        toolbar.title = R.string.app_name.toString()
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                Toast.makeText(applicationContext, "Search Click", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.option -> {
                Toast.makeText(applicationContext, "Option Click", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        AppLogger.i(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        AppLogger.i(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        AppLogger.i(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        AppLogger.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLogger.i(TAG,"onDestroy")
    }

}