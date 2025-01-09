package com.Refee.RefeeDB

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.Fragment

class MyItemFragment : Fragment(R.layout.fragment_my_item) {
    private lateinit var btnCancel: Button // 취소 버튼 선언 추가

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 취소 버튼 클릭 리스너
        btnCancel = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            navigateToProfileFragment()
        }
    }

    private fun navigateToProfileFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment()) // R.id.fragmentContainer는 Fragment를 표시하는 컨테이너 ID
            .addToBackStack(null) // 필요한 경우 뒤로가기 스택 추가
            .commit()
    }
}

        // UI 설정과 필요한 동작 정의
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView: GridView = view.findViewById(R.id.gridView)
        val gAdapter = MyItemGridAdapter(requireContext())
        gridView.adapter = gAdapter
        // 클릭 이벤트는 정의하지 않습니다.
    }
    */

