package com.Refee.RefeeDB

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class MainScreen : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_bottomnavgation)

        // 기본 화면을 AppsFragment로 설정
        if (savedInstanceState == null) {
            loadFragment(AppsFragment())  // 기본 화면으로 AppsFragment 설정
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.shop -> {
                    loadFragment(ShopFragment())
                    true
                }
                R.id.apps -> {
                    loadFragment(AppsFragment())  // AppsFragment 로딩
                    true
                }
                else -> false
            }
        }

        // Toolbar 설정
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }

    // 메뉴 로드
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        return true
    }

    // 메뉴 클릭 이벤트 (알림 아이콘 클릭 시 NotificationActivity로 이동)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notice_button -> {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)  // main_framelayout에 Fragment 전환
        transaction.commit()
    }
}
