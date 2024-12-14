package com.Refee.RefeeDB

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class NotificationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification) // 새 액티비티의 레이아웃 파일

        // Toolbar 설정 (뒤로가기 버튼과 "알림" 텍스트)
        val toolbar = findViewById<Toolbar>(R.id.notification_toolbar)
        setSupportActionBar(toolbar)

        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 프로젝트 이름과 ic_refee 아이콘을 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(null)  // 아이콘 제거

        // "알림" 텍스트 설정
        toolbar.title = "알림"

        // 알림 내역은 비워둠 (추후 내용 추가)
    }

    // 뒤로가기 버튼 클릭 시 MainActivity로 이동
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭 시 MainActivity로 이동
                val intent = Intent(this, MainScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // MainActivity로 돌아가기
                startActivity(intent)
                finish() // 현재 Activity 종료
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}