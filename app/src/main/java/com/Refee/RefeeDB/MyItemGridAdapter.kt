package com.Refee.RefeeDB

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MyItemGridAdapter(private val context: Context) : BaseAdapter() {

    private val imageList = arrayOf(
        R.drawable.letter_ic, R.drawable.red_letter, R.drawable.orange_letter,
        R.drawable.green_letter, R.drawable.blue_letter, R.drawable.purple_letter,
        R.drawable.yellow_letter, R.drawable.gray_letter, R.drawable.silver_letter,
        R.drawable.gold_letter, R.drawable.flower_letter
    )

    private val textList = arrayOf(
        "기본 편지", "빨간 편지", "주황 편지", "초록 편지", "파랑 편지", "보라 편지",
        "노랑 편지", "회색 편지", "은색 편지", "금색 편지", "꽃무니 편지"
    )

    // 각 아이템의 개수를 0으로 초기화
    private val itemCounts = IntArray(imageList.size) { 0 }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            // view가 없으면 새로 생성
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_item, parent, false)
        }

        // 이미지와 텍스트 설정
        val imageView: ImageView = view!!.findViewById(R.id.gridImage)
        val textView: TextView = view.findViewById(R.id.gridText)
        val itemCountView: TextView = view.findViewById(R.id.saleprice) // 기존 saleprice를 아이템 개수로 사용

        imageView.setImageResource(imageList[position])  // 이미지 설정
        textView.text = textList[position]               // 텍스트 설정
        itemCountView.text = "${itemCounts[position]}개"  // 아이템 개수 표시

        // 클릭 이벤트 제거

        return view
    }
}
