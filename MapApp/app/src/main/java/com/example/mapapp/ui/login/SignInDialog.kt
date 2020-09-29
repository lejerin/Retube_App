package com.example.mapapp.ui.login

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.mapapp.R
import kotlinx.android.synthetic.main.signin_fragment_dialog.view.*


class SignInDialog(
    private val clickListener: SignInClickListener
) : DialogFragment() {

    interface SignInClickListener {
        fun onSignIn(id: String, pw: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomFullDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signin_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)


    }

    override fun onStart() {
        super.onStart()

        val window: Window = dialog!!.window!!

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params: WindowManager.LayoutParams = window.attributes
        // 화면에 가득 차도록
        // 화면에 가득 차도록
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        // 열기&닫기 시 애니메이션 설정

        // 열기&닫기 시 애니메이션 설정
        params.windowAnimations = R.style.AnimationPopupStyle
        window.attributes = params
        // UI 하단 정렬
        // UI 하단 정렬
        window.setGravity(Gravity.BOTTOM)
    }




    private fun setupClickListeners(view: View) {
        view.btn_close.setOnClickListener {
            hideSoftKeyBoard()
            dismiss()
        }
        view.btn_sign_in.setOnClickListener {

            hideSoftKeyBoard()
            clickListener.onSignIn(view.text_id.text.toString(), view.text_pw.text.toString())

        }
    }

    private fun hideSoftKeyBoard() {
        activity!!.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}