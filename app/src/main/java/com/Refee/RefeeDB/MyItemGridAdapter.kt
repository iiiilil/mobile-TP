package com.Refee.RefeeDB

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.Refee.RefeeDB.DataStore

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
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_item, parent, false)
        }

        val imageView: ImageView = view!!.findViewById(R.id.gridImage)
        val textView: TextView = view.findViewById(R.id.gridText)
        val itemCountView: TextView = view.findViewById(R.id.saleprice)

        imageView.setImageResource(imageList[position])
        textView.text = textList[position]
        itemCountView.text = "${DataStore.letterPaperCounts[position]}개"  // 전역 데이터 사용

        return view
    }
}
