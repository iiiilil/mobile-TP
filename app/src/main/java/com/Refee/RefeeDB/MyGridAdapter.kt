package com.Refee.RefeeDB

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MyGridAdapter(private val context: Context) : BaseAdapter() {




    private val imageList = arrayOf(
        R.drawable.letter_ic, R.drawable.red_letter, R.drawable.orange_letter, // 이미지 리소스
        R.drawable.green_letter, R.drawable.blue_letter, R.drawable.purple_letter ,
        R.drawable.yellow_letter, R.drawable.gray_letter, R.drawable.silver_letter, // 이미지 리소스
        R.drawable.gold_letter, R.drawable.flower_letter
    )

    private val textList = arrayOf(
        "기본 편지", "빨간 편지", "주황 편지", "초록 편지", "파랑 편지", "보라 편지",
        "노랑 편지", "회색 편지", "은색 편지", "금색 편지", "꽃무니 편지"
    )

    private val saleList = arrayOf(
        "10 point", "15 point", "15 point", "15 point", "15 point", "15 point",
        "15 point", "15 point", "25 point", "30 point", "50 point",
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

    @SuppressLint("ObjectAnimatorBinding")
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
        val saleprice: TextView = view.findViewById(R.id.saleprice)

        imageView.setImageResource(imageList[position])  // 이미지 설정
        textView.text = textList[position]               // 텍스트 설정
        saleprice.text = saleList[position]



        // 아이템 클릭 리스너 설정
        view.setOnClickListener {

            // 클릭 시 처리할 작업 (예: Toast 메시지)
            // 클릭 애니메이션 추가 (이미지 크기 애니메이션)
            val scaleAnimation = ScaleAnimation(
                0.8f, 1.0f,  //
                0.8f, 1.0f,  //
                Animation.RELATIVE_TO_SELF, 0.5f,  // 중심을 기준으로 애니메이션
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = 200  // 애니메이션 지속 시간 설정
            scaleAnimation.fillAfter = true  // 애니메이션 종료 후 상태 유지
            imageView.startAnimation(scaleAnimation)

            // 클릭 시 처리할 작업 (예: Toast 메시지)
            //Toast.makeText(context, "아이템 $position 클릭됨: ${textList[position]}", Toast.LENGTH_SHORT)
            //    .show()
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.custom_popup)  // 팝업의 레이아웃 설정
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // 팝업의 텍스트 설정
            val popupContent = dialog.findViewById<TextView>(R.id.popup_content_shop)
            popupContent.text = "아이템 $position 클릭됨: ${textList[position]}"
            val letterPater = dialog.findViewById<ImageView>(R.id.letterPaper)
            letterPater.setImageResource(imageList[position])

            // 닫기 버튼 설정
            val btnClose = dialog.findViewById<ImageView>(R.id.btn_close_shop)
            btnClose.setOnClickListener {
                dialog.dismiss()  // 다이얼로그 닫기
            }
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
            btnCancel.setOnClickListener {
                dialog.dismiss()  // 다이얼로그 닫기
            }


            // 팝업 띄우기
            dialog.show()

        }


        return view


    }
}
