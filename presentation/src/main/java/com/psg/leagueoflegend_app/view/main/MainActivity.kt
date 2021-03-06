package com.psg.leagueoflegend_app.view.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.psg.domain.model.Profile
import com.psg.domain.model.Summoner
import com.psg.leagueoflegend_app.R
import com.psg.leagueoflegend_app.base.BaseActivity
import com.psg.leagueoflegend_app.databinding.ActivityMainBinding
import com.psg.leagueoflegend_app.utils.AppLogger
import com.psg.leagueoflegend_app.utils.NetworkUtils
import com.psg.leagueoflegend_app.view.search.SearchActivity
import com.psg.leagueoflegend_app.view.spectator.SpectatorActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_settingkey.*
import kotlinx.android.synthetic.main.header_navi.view.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener {
    override val TAG: String = MainActivity::class.java.simpleName
    override val viewModel: MainViewModel by viewModels()
    private val adapter = MainAdapter()

    private var list: List<Summoner> = listOf()
    private var profile: Profile = Profile("", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initViewModel()
        setToolbar(binding.toolbar)
        checkNetwork()
        initView()
        setRv()
        setObserve()
        setEventFlow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.option -> {
                binding.dlMain.openDrawer(Gravity.RIGHT)
                if (profile.icon != "") {
                    binding.nvMain.tv_name.text = profile.name
                    binding.nvMain.tv_level.text = "LV: ${profile.level}"
                    viewModel.bindImage(binding.nvMain.iv_image, profile.icon)
                    AppLogger.p("?????????:${profile.icon}")
                } else {
                    binding.nvMain.tv_name.text = "??????"
                    binding.nvMain.tv_level.text = "LV: "
                    viewModel.bindImage(
                        binding.nvMain.iv_image,
                        "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/29.png"
                    )
                }
                true // ????????? ???????????? ??????
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        if (binding.dlMain.isDrawerOpen(Gravity.RIGHT)) {
            binding.dlMain.closeDrawer(Gravity.RIGHT)
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.summonerUpdate()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.viewModel = viewModel

        binding.slMain.setOnRefreshListener {
            refresh(list)
        }
        binding.tvDeleteAll.setOnClickListener {
//            viewModel.deleteAll()
            makeToast("?????? ?????????????????????.")
        }

        binding.dlMain.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        binding.nvMain.setNavigationItemSelectedListener(this)

    }


    override fun setObserve() {
        viewModel.summonerList.observe(this) {
            if (it != null) {
                list = it
                refresh(it)
            } else {
                AppLogger.p("????????? null")
            }
        }

        viewModel.profile.observe(this) {
            profile = if (it != null) {
                it
            } else {
                AppLogger.p("????????? null")
                Profile("", "", "")
            }
        }

        viewModel.league.observe(this) {
            if (it != null) {
                when (it.code) {
//                    0 -> makeToast("????????? ?????????")
//                    1 -> makeToast("?????? ??????")
//                    2 -> makeToast("?????? ?????? ???????????? ????????? ?????????\n ????????? ????????? ???????????????.")
//                    3 -> makeToast("?????? ?????? ????????? ???????????? ????????????.")
                    401 -> makeToast("????????? ???????????? ???????????????.")
                    403 -> makeToast("????????? ?????????????????????.")
                    404 -> makeToast("???????????? ?????? ??????????????????.")
                    429 -> AppLogger.p("?????? ?????? ??????")
                    else -> makeToast("?????? ?????? ????????? ???????????? ????????????.")
                }
            }
        }
    }


    private fun checkNetwork() {
        NetworkUtils.getNetworkStatus().observe(this) { isConnected ->
            if (!isConnected) makeToast("???????????? ???????????? ?????? ????????????.")

        }
    }

    private fun refresh(list: List<Summoner>) {
        viewModel.refresh(list)
        viewModel.isRefresh.observe(this) {
            if (it) {
                binding.slMain.isRefreshing = false
            }
        }

    }

    override fun setRv() {
        adapter.setHasStableIds(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // ?????????????????? ?????? ??????
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvMain.layoutManager = layoutManager
        binding.rvMain.adapter = adapter
        viewModel.summonerList.observe(this) {
            if (it != null) {
                adapter.setData(it)
                AppLogger.p("Main: null??? ??????")
            } else {
                AppLogger.p("Main: db??? null")
            }
        }

        adapter.setOnItemClickListener(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: Summoner, pos: Int) {
                when (v.id) {
                    R.id.iv_delete -> {
                        viewModel.deleteSummoner(data)
                        AppLogger.p("???????????????")
                        makeToast("?????? ??????")
                        viewModel.summonerUpdate()
                    }
                    R.id.iv_addProfile -> {
                        viewModel.insertProfile(Profile(data.name, data.level, data.icon))
                        AppLogger.p("???????????????")
                        makeToast("????????? ?????? ??????")
                        viewModel.profileUpdate()
                    }
                    R.id.ll_spectator -> {
                        if (data.isPlaying) {
                            val intent = Intent(this@MainActivity, SpectatorActivity::class.java)
                            intent.putExtra("name", data.name)
                            startActivity(intent)
                            AppLogger.p("?????????????????? ??????")
                        } else {
                            makeToast("???????????? ????????????.")
                        }

                    }

                }


            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_info -> {
                AppLogger.p("????????????")
                true
            }
            R.id.item_key -> {
                AppLogger.p("?????????")
                setKeyDialog()
                true // ????????? ???????????? ??????
            }
            R.id.item_delete -> {
                AppLogger.p("????????????")
                viewModel.deleteProfile()
                binding.dlMain.closeDrawer(Gravity.RIGHT)
                true // ????????? ???????????? ??????
            }
            else -> {
                AppLogger.p("else")
                true
            }
        }
    }

    /**
     * ????????? ??????????????? ??????
     */
    @SuppressLint("SetTextI18n")
    fun setKeyDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settingkey)
        val params = dialog.window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT

        AppLogger.p("api????${viewModel.apiKey.value}")
        dialog.tv_key.text = viewModel.apiKey.value

        dialog.show()
        dialog.tv_key.setOnClickListener {
            if (dialog.tv_key.text.isNotEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("??? ??????").setMessage("????????? ?????????????????????????")
                builder.setPositiveButton("??????") { dialog1, _ ->
//                    viewModel.delApikey()
                    dialog1.dismiss()
                    dialog.tv_key.text = ""
                }
                builder.setNegativeButton("??????") { dialog, _ ->
                    dialog.dismiss()

                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }
        dialog.btn_confirm.setOnClickListener {
            val key = dialog.et_key.text.toString().replace(" ", "")
            if (key.isNotEmpty()) {
                viewModel.setApikey(key)
                dialog.dismiss()
            } else {
                makeToast("?????? ??????????????????.")
            }
        }
        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_newKey.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://developer.riotgames.com/")
            startActivity(i)
        }

    }


    private fun deleteKeyDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("??? ??????").setMessage("????????? ?????????????????????????")
        builder.setPositiveButton("??????") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton("??????") { dialog, _ ->
            dialog.dismiss()

        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}