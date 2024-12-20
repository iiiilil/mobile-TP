
package com.Refee.RefeeDB

import android.os.Bundle
import android.view.View
import android.widget.GridView
import androidx.fragment.app.Fragment

class MyItemFragment : Fragment(R.layout.fragment_my_item) {
    // UI 설정과 필요한 동작 정의
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView: GridView = view.findViewById(R.id.gridView)
        val gAdapter = MyItemGridAdapter(requireContext())
        gridView.adapter = gAdapter
        // 클릭 이벤트는 정의하지 않습니다.
    }
}
