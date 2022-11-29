package com.luqmanfajar.story_app.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.luqmanfajar.story_app.R


class EmailValidate : AppCompatEditText, View.OnTouchListener{

    private lateinit var errorButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


    }


    private fun init(){
        errorButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_24) as Drawable

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(!Patterns.EMAIL_ADDRESS
                        .matcher(s.toString()).matches()){

                    showErrorButton()


                } else{
                    hideErrorButton()

                }
            }

            override fun afterTextChanged(s: Editable) {
               //do nothing
            }

        })

    }
    private fun showErrorButton(){
        setButtonDrawables(endOfTheText = errorButtonImage)
    }

    private fun hideErrorButton(){
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }



    override fun onTouch(v: View?, p1: MotionEvent): Boolean {
        return false
    }
}