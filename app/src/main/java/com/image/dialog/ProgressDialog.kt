package com.image.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.image.R

/**
 * 프로그래스 다이얼로그
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.20
 * @param context 호출 context
 */
class ProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val manager = WindowManager.LayoutParams().apply {
            this.gravity = Gravity.CENTER
            this.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            this.dimAmount = 0.4f
        }
        window?.attributes = manager
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_progress)
    }
}