package com.Refee.RefeeDB


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
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
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
//
var point : Int = 100
val letter_paper = arrayOf(
    0, // 기본 편지 "#FFFFFF"
    0, // 빨간 편지 "#F58A82"
    0, // 주황 편지 "#EEA45B"
    0, // 초록 편지 "#DAFD84"
    0, // 파랑 편지 "#94EBFD"
    0, // 보라 편지 "#CAAEFD"
    0, // 노랑 편지 "#FDFCBD"
    0, // 회색 편지 "#CDCDCD"
    0, // 은색 편지
    0, // 금색 편지
    0  // 꽃 편지
)


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
        "15 point", "15 point", "25 point", "30 point", "50 point"
    )

    private val salepoint = arrayOf(
        10, 15, 15, 15, 15, 15, 15, 15, 25, 30, 50
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



//        // 아이템 클릭 리스너 설정
//        view.setOnClickListener {
//
//            // 클릭 시 처리할 작업 (예: Toast 메시지)
//            // 클릭 애니메이션 추가 (이미지 크기 애니메이션)
//            val scaleAnimation = ScaleAnimation(
//                0.8f, 1.0f,  //
//                0.8f, 1.0f,  //
//                Animation.RELATIVE_TO_SELF, 0.5f,  // 중심을 기준으로 애니메이션
//                Animation.RELATIVE_TO_SELF, 0.5f
//            )
//            scaleAnimation.duration = 200  // 애니메이션 지속 시간 설정
//            scaleAnimation.fillAfter = true  // 애니메이션 종료 후 상태 유지
//            imageView.startAnimation(scaleAnimation)
//
//
//            val dialog = Dialog(context)
//
//            dialog.setContentView(R.layout.custom_popup)  // 팝업의 레이아웃 설정
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//            // 팝업의 텍스트 설정
//            val popupContent = dialog.findViewById<TextView>(R.id.popup_content_shop)
//            popupContent.text = "아이템 $position 클릭됨: ${textList[position]}"
//            val letterPater = dialog.findViewById<ImageView>(R.id.letterPaper)
//            letterPater.setImageResource(imageList[position])
//
//            // 닫기 버튼 설정
//            val btnClose = dialog.findViewById<ImageView>(R.id.btn_close_shop)
//            btnClose.setOnClickListener {
//                dialog.dismiss()  // 다이얼로그 닫기
//            }
//            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
//            btnCancel.setOnClickListener {
//                dialog.dismiss()  // 다이얼로그 닫기
//            }
//
//
//            // 팝업 띄우기
//            dialog.show()
//
//        }
        view.setOnClickListener {

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

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.custom_popup)  // 팝업의 레이아웃 설정
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // 팝업의 텍스트 설정
            val popupContent = dialog.findViewById<TextView>(R.id.popup_content_shop)
            popupContent.text = salepoint[position].toString()
            popupContent.text = "편지지 보유 개수 ${letter_paper[position]}"

            // 팝업 전체의 배경 색상 변경 (색상 대신 이미지 배경을 적용)
            val rootLayout = dialog.findViewById<RelativeLayout>(R.id.popup_root_layout) // 팝업의 루트 레이아웃 참조

            // 원하는 색상 또는 이미지를 설정
            val backgroundColor = when (position) {
                0 -> Color.parseColor("#FFFFFF")
                1 -> Color.parseColor("#F58A82")
                2 -> Color.parseColor("#EEA45B")
                3 -> Color.parseColor("#DAFD84")
                4 -> Color.parseColor("#94EBFD")
                5 -> Color.parseColor("#CAAEFD")
                6 -> Color.parseColor("#FDFCBD")
                7 -> Color.parseColor("#CDCDCD")

                8 -> Color.parseColor("#E7E7E7")
                9 -> Color.parseColor("#CFB472")

                else -> Color.parseColor("#AF8493")

            }
            rootLayout.setBackgroundColor(backgroundColor)

            // 팝업 띄우기 전에 버튼을 참조
            val btnPurchase = dialog.findViewById<Button>(R.id.btn_purchase)
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

            // 팝업 띄우기
            dialog.show()

            btnPurchase.setOnClickListener {
                // 구매 팝업으로 변경
                val dialogYesorNo = Dialog(context)
                dialogYesorNo.setContentView(R.layout.shop_answer)  // 팝업의 레이아웃 변경
                dialogYesorNo.window?.setBackgroundDrawableResource(android.R.color.transparent)

                // 새 레이아웃에서 버튼을 찾기
                val btnYes = dialogYesorNo.findViewById<Button>(R.id.btn_yes)
                val btnNo = dialogYesorNo.findViewById<Button>(R.id.btn_no)

                // '예' 버튼 클릭 시 처리
                btnYes.setOnClickListener {
                    if (salepoint[position] <= point) {
                        Toast.makeText(context, "구입완료", Toast.LENGTH_SHORT).show()
                        point -= salepoint[position]
                        letter_paper[position] += 1
                        dialogYesorNo.dismiss()  // 다이얼로그 닫기
                        dialog.dismiss()
                        // 구매 관련 처리
                    }
                    else {
                        Toast.makeText(context, "포인트가 부족 합니다", Toast.LENGTH_SHORT).show()
                        dialogYesorNo.dismiss()  // 다이얼로그 닫기
                    }
                }

                // '아니오' 버튼 클릭 시 처리
                btnNo.setOnClickListener {
                    dialogYesorNo.dismiss()  // 다이얼로그 닫기
                }

                // 새 레이아웃에서 다이얼로그 다시 띄우기
                dialogYesorNo.show()
            }


            // 취소 버튼 설정
            btnCancel.setOnClickListener {
                dialog.dismiss()  // 다이얼로그 닫기
            }
        }




        return view


    }
}

