package br.com.choopescama.util

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import br.com.choopescama.R

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    // ---------------------------------------------------------------------------------------------
    // region Instance Attributes
    // ---------------------------------------------------------------------------------------------

    private var mGravity: Int = Gravity.CENTER
    private var mContentView: ViewGroup? = null
    private var mProgressBar: ProgressBar? = null
    private var mProgressBarContainer: LinearLayout? = null
    private var mTextViewProgressMessage: TextView? = null
    private var mIsLoading: Boolean = false
    private var mProgressText: String? = null

    //endregion

    // ---------------------------------------------------------------------------------------------
    // region Public methods
    // ---------------------------------------------------------------------------------------------

    var isLoading: Boolean
        get() = mIsLoading
        set(loading) {
            mIsLoading = loading

            if (mIsLoading) {
                showContentContainer()
            } else {
                hideContentContainer()
            }
        }

    var progressText: String?
        get() = mProgressText
        set(progressText) {
            mProgressText = progressText

            //Update view
            mTextViewProgressMessage?.text = mProgressText
        }

    init {
        setupUI(context)
        retrieveAttributes(context, attrs)
        refreshWithAttributeValues()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (mContentView == null) {
            super.addView(child, index, params)
        } else {
            //Forward these calls to the content view
            mContentView?.addView(child, index, params)
        }
    }

    //endregion

    // ---------------------------------------------------------------------------------------------
    // region Private methods
    // ---------------------------------------------------------------------------------------------

    private fun hideContentContainer() {
        mContentView?.visibility = View.VISIBLE
        mProgressBarContainer?.visibility = View.GONE
    }

    private fun showContentContainer() {
        mContentView?.visibility = View.INVISIBLE
        mProgressBarContainer?.visibility = View.VISIBLE
    }

    private fun setupUI(context: Context) {
        //Inflate and attach your child XML
        LayoutInflater.from(context).inflate(R.layout.progress_view, this)
        //Get a reference to the layout where you want children to be placed
        mContentView = findViewById(R.id.containerContent)
        mProgressBar = findViewById(R.id.progressBar)
        mProgressBarContainer = findViewById(R.id.progressBarContainer)
        mTextViewProgressMessage = findViewById(R.id.txtViewProgressMessage)
    }

    private fun retrieveAttributes(context: Context, attrs: AttributeSet?) {
        //Retrieve attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0)

        mIsLoading = typedArray.getBoolean(R.styleable.ProgressView_isLoading, false)
        mProgressText = typedArray.getString(R.styleable.ProgressView_progressText)
        mGravity = typedArray.getInt(R.styleable.ProgressView_android_gravity, Gravity.CENTER);

        //Recycle
        typedArray.recycle()
    }

    private fun refreshWithAttributeValues() {
        isLoading = mIsLoading
        progressText = mProgressText

        mProgressBarContainer?.gravity = mGravity
    }

    //endregion

}