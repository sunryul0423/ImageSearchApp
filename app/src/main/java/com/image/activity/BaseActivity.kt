package com.image.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.image.R
import com.image.dialog.ProgressDialog

/**
 * AppCompatActivity를 Base로 공통적으로 필요한 변수,함수 정의
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.20
 * @property viewBinding Binding ContentView
 * @property layoutResourceId ContentView ID
 * @property progressDialog 프로그래스 다이얼로그(로딩)
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var viewBinding: T
    protected abstract val layoutResourceId: Int
    protected lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        progressDialog = ProgressDialog(this)
    }

    /**
     * 네트워크 에러발생 Toast 메세지
     */
    protected fun networkErrorToast() {
        Toast.makeText(this, getString(R.string.network_error_msg), Toast.LENGTH_LONG).show()
    }
}