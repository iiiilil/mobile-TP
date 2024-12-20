package com.Refee.RefeeDB

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.GridView
import androidx.fragment.app.Fragment


class ShopFragment : Fragment(R.layout.fragment_shop) {
    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DataStore에서 point 값을 가져오기
        val point = DataStore.point  // DataStore에서 저장된 point 값을 가져옵니다

        val gridView: GridView = view.findViewById(R.id.gridView)

        // MyGridAdapter에 point 값을 전달하여 생성
        val gAdapter = MyGridAdapter(requireContext(), point)

        // GridView에 어댑터 설정
        gridView.adapter = gAdapter
    }
}
