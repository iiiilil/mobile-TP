package com.Refee.RefeeDB

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.GridView


class ShopFragment : Fragment(R.layout.fragment_shop) {
    // 추가적인 동작이나 UI 설정이 필요하면 onViewCreated나 다른 메서드를 사용합니다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView: GridView = view.findViewById(R.id.gridView)
        val gAdapter = MyGridAdapter(requireContext())
        gridView.adapter = gAdapter
    }
}