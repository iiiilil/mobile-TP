package com.Refee.RefeeDB

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

class MyGridAdapter(private val context: Context, private var point: Int) : BaseAdapter() {

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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_item, parent, false)
        }

        val imageView: ImageView = view!!.findViewById(R.id.gridImage)
        val textView: TextView = view.findViewById(R.id.gridText)
        val saleprice: TextView = view.findViewById(R.id.saleprice)

        imageView.setImageResource(imageList[position])
        textView.text = textList[position]
        saleprice.text = saleList[position]

        view.setOnClickListener {

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.custom_popup)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val popupContent = dialog.findViewById<TextView>(R.id.popup_content_shop)
            popupContent.text = salepoint[position].toString()
            popupContent.text = "편지지 보유 개수 ${DataStore.letterPaperCounts[position]}"

            val rootLayout = dialog.findViewById<RelativeLayout>(R.id.popup_root_layout)
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

            val btnPurchase = dialog.findViewById<Button>(R.id.btn_purchase)
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

            dialog.show()

            btnPurchase.setOnClickListener {
                val dialogYesorNo = Dialog(context)
                dialogYesorNo.setContentView(R.layout.shop_answer)
                dialogYesorNo.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val btnYes = dialogYesorNo.findViewById<Button>(R.id.btn_yes)
                val btnNo = dialogYesorNo.findViewById<Button>(R.id.btn_no)

                btnYes.setOnClickListener {
                    if (salepoint[position] <= point) {
                        Toast.makeText(context, "구입완료", Toast.LENGTH_SHORT).show()
                        point -= salepoint[position] // point 값을 업데이트
                        DataStore.letterPaperCounts[position] += 1 // 해당 아이템 개수 증가
                        dialogYesorNo.dismiss()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "포인트가 부족 합니다", Toast.LENGTH_SHORT).show()
                        dialogYesorNo.dismiss()
                    }
                }

                btnNo.setOnClickListener {
                    dialogYesorNo.dismiss()
                }

                dialogYesorNo.show()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        return view
    }
}
