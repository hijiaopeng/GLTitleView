package com.jiaopeng.gltitleview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable

/**
 * 描述：标题栏TitleView
 *      1：设置左侧图片或文字
 *      2：设置中间文字
 *      3：设置右侧图片或文字
 *      4：设置底部线的显示和隐藏
 *      5：设置沉浸式
 *      6：设置自定义布局
 *      7：全局设置左侧点击事件
 *      8：设置activity栈管理器
 *
 * @author JiaoPeng by 2020/9/7
 */
class GLTitleView : FrameLayout {

    var titleHeight: Float = 0F
    var titleBackgroundColor: Int = 0
    var startImgResource: Int = 0
    var startImgBounds: Float = 0F
    var startDrawablePadding: Float = 0F
    var endImgResource: Int = 0
    var endImgBounds: Float = 0F
    var endDrawablePadding: Float = 0F
    var startTextString: String = ""
    var startTextColor: Int = 0
    var startTextSize: Float = 0F
    var startTextBold: Boolean = false
    var startTextPadding: Float = 0F
    var centerTextString: String = ""
    var centerTextColor: Int = 0
    var centerTextSize: Float = 0F
    var centerTextBold: Boolean = false
    var endTextString: String = ""
    var endTextColor: Int = 0
    var endTextSize: Float = 0F
    var endTextBold: Boolean = false
    var endTextPadding: Float = 0F
    var colorStyle: Int = -1
    var customizeLayout: Int = -1

    var startTextView: TextView? = null
    var endTextView: TextView? = null
    var centerTextView: TextView? = null
    var customizeView: View? = null
    var bottomLineView: View? = null

    private var oldBottomHeight = 0F

    var isShowBottomLine: Boolean
        set(value) {
            bottomLineView?.let {
                when (value) {
                    true -> {
                        bottomLineHeight = oldBottomHeight
                        centerTextView?.setPadding(0, 0, 0, bottomLineHeight.toInt())
                        bottomLineView?.visibility = VISIBLE
                    }
                    else -> {
                        bottomLineHeight = 0F
                        centerTextView?.setPadding(0, 0, 0, 0)
                        bottomLineView?.visibility = GONE
                    }
                }
            }
            invalidate()
            field = value
        }

    var bottomLineColor: Int = 0
    var bottomLineHeight: Float = 0F
    var bottomLineToBothSides: Float = 0F

    private var mImmersive: Boolean = false
    private var mStatusBarHeight: Int = 0

    var startViewClickListener: (() -> Unit)? = null
    var endViewClickListener: (() -> Unit)? = null

    companion object {

        private var defaultViewClickListener: (() -> Unit)? = null

        fun initDefaultStartViewClickListener(onDefaultClick: (() -> Unit)?) {
            defaultViewClickListener = onDefaultClick
        }

    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GLTitleView)
        titleHeight =
            typeArray.getDimension(R.styleable.GLTitleView_titleHeight, dp2px(46F).toFloat())
        titleBackgroundColor = typeArray.getColor(
            R.styleable.GLTitleView_titleBackgroundColor,
            Color.parseColor("#00000000")
        )
        startImgResource =
            typeArray.getResourceId(R.styleable.GLTitleView_startImgResource, 0)
        startImgBounds =
            typeArray.getDimension(R.styleable.GLTitleView_startImgBounds, dp2px(12F).toFloat())
        startDrawablePadding = typeArray.getDimension(
            R.styleable.GLTitleView_startDrawablePadding,
            dp2px(4F).toFloat()
        )
        endImgResource = typeArray.getResourceId(R.styleable.GLTitleView_endImgResource, 0)
        endImgBounds =
            typeArray.getDimension(R.styleable.GLTitleView_endImgBounds, dp2px(12F).toFloat())
        endDrawablePadding = typeArray.getDimension(
            R.styleable.GLTitleView_endDrawablePadding,
            dp2px(4F).toFloat()
        )

        startTextString = typeArray.getString(R.styleable.GLTitleView_startTextString) ?: ""
        startTextColor =
            typeArray.getColor(R.styleable.GLTitleView_startTextColor, getDefaultTextColor())
        startTextSize =
            typeArray.getDimension(R.styleable.GLTitleView_startTextSize, dp2px(12F).toFloat())
        startTextBold = typeArray.getBoolean(R.styleable.GLTitleView_startTextBold, false)
        startTextPadding =
            typeArray.getDimension(R.styleable.GLTitleView_startTextPadding, dp2px(12F).toFloat())
        centerTextString = typeArray.getString(R.styleable.GLTitleView_centerTextString) ?: ""
        centerTextColor =
            typeArray.getColor(R.styleable.GLTitleView_centerTextColor, getDefaultTextColor())
        centerTextSize =
            typeArray.getDimension(R.styleable.GLTitleView_centerTextSize, dp2px(12F).toFloat())
        centerTextBold = typeArray.getBoolean(R.styleable.GLTitleView_centerTextBold, true)
        endTextString = typeArray.getString(R.styleable.GLTitleView_endTextString) ?: ""
        endTextColor =
            typeArray.getColor(R.styleable.GLTitleView_endTextColor, getDefaultTextColor())
        endTextSize =
            typeArray.getDimension(R.styleable.GLTitleView_endTextSize, dp2px(12F).toFloat())
        endTextBold = typeArray.getBoolean(R.styleable.GLTitleView_endTextBold, false)
        endTextPadding =
            typeArray.getDimension(R.styleable.GLTitleView_endTextPadding, dp2px(12F).toFloat())
        colorStyle = typeArray.getInt(R.styleable.GLTitleView_colorStyle, -1)
        customizeLayout = typeArray.getResourceId(R.styleable.GLTitleView_customizeLayout, -1)
        isShowBottomLine = typeArray.getBoolean(R.styleable.GLTitleView_isShowBottomLine, false)
        bottomLineColor =
            typeArray.getColor(R.styleable.GLTitleView_bottomLineColor, getDefaultBottomLineColor())
        bottomLineHeight =
            typeArray.getDimension(R.styleable.GLTitleView_bottomLineHeight, dp2px(1F).toFloat())
        bottomLineToBothSides =
            typeArray.getDimension(R.styleable.GLTitleView_bottomLineToBothSides, 0F)

        typeArray.recycle()
        if (customizeLayout != -1) {
            initCustomizeLayout()
            return
        }
        initView()
    }

    private fun initView() {

        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight()
        }

        when (colorStyle) {
            //白色
            1 -> {
                titleBackgroundColor = Color.parseColor("#FFFFFF")
                startImgResource = R.drawable.back_black
                startImgBounds = dp2px(18F).toFloat()
                startDrawablePadding = dp2px(4F).toFloat()
                startTextPadding = dp2px(16F).toFloat()
                centerTextColor = Color.parseColor("#171717")
                centerTextSize = dp2px(18F).toFloat()
                centerTextBold = true
            }
            //蓝色
            2 -> {
                titleBackgroundColor = Color.parseColor("#007AFF")
                startImgResource = R.drawable.back_white
                startImgBounds = dp2px(18F).toFloat()
                startDrawablePadding = dp2px(4F).toFloat()
                startTextPadding = dp2px(16F).toFloat()
                centerTextColor = Color.parseColor("#FFFFFF")
                centerTextSize = dp2px(18F).toFloat()
                centerTextBold = true
            }
        }

        this.setBackgroundColor(titleBackgroundColor)

        //绘制左侧TextView
        startTextView = TextView(context)
        startTextView?.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt())
        startTextView?.gravity = Gravity.CENTER
        if (startImgResource != 0) {
            val drawable = getDrawable(context, startImgResource)
            drawable?.setBounds(0, 0, startImgBounds.toInt(), startImgBounds.toInt())
            startTextView?.setCompoundDrawables(drawable, null, null, null)
            startTextView?.compoundDrawablePadding = startDrawablePadding.toInt()
            startTextView?.setPadding(startTextPadding.toInt(), 0, 0, 0)
        }
        if (!TextUtils.isEmpty(startTextString)) {
            startTextView?.text = startTextString
            startTextView?.textSize = px2sp(startTextSize).toFloat()
            startTextView?.setTextColor(startTextColor)
            startTextView?.setPadding(startTextPadding.toInt(), 0, 0, 0)
            if (startTextBold) {
                startTextView?.typeface = Typeface.DEFAULT_BOLD
            }
        }
        startTextView?.setOnClickListener {
            if (startViewClickListener != null) {
                startViewClickListener?.invoke()
            } else {
                defaultViewClickListener?.invoke()
            }
        }
        addView(startTextView)

        //绘制右侧TextView
        endTextView = TextView(context)
        endTextView?.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt())
        endTextView?.gravity = Gravity.CENTER
        if (endImgResource != 0) {
            val endDrawable = getDrawable(context, endImgResource)
            endDrawable?.setBounds(0, 0, endImgBounds.toInt(), endImgBounds.toInt())
            endTextView?.setCompoundDrawables(null, null, endDrawable, null)
            endTextView?.compoundDrawablePadding = endDrawablePadding.toInt()
            endTextView?.setPadding(0, 0, endTextPadding.toInt(), 0)
        }
        if (!TextUtils.isEmpty(endTextString)) {
            endTextView?.text = endTextString
            endTextView?.textSize = px2sp(endTextSize).toFloat()
            endTextView?.setTextColor(endTextColor)
            endTextView?.setPadding(0, 0, endTextPadding.toInt(), 0)
            if (endTextBold) {
                endTextView?.typeface = Typeface.DEFAULT_BOLD
            }
        }
        endTextView?.setOnClickListener {
            endViewClickListener?.invoke()
        }
        val endLl = LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt())
        endLl.gravity = Gravity.END
        addView(endTextView, endLl)

        //绘制中间TextView
        centerTextView = TextView(context)
        centerTextView?.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt())
        centerTextView?.setPadding(0, 0, 0, bottomLineHeight.toInt())
        centerTextView?.gravity = Gravity.CENTER
        centerTextView?.text = centerTextString
        centerTextView?.textSize = px2sp(centerTextSize).toFloat()
        centerTextView?.setTextColor(centerTextColor)
        if (centerTextBold) {
            centerTextView?.typeface = Typeface.DEFAULT_BOLD
        }
        val centerLl = LayoutParams(LayoutParams.WRAP_CONTENT, titleHeight.toInt())
        centerLl.gravity = Gravity.CENTER
        addView(centerTextView, centerLl)

        if (isShowBottomLine) {
            oldBottomHeight = bottomLineHeight
            bottomLineView = View(context)
            bottomLineView?.setBackgroundColor(bottomLineColor)
            val bottomLp = LayoutParams(LayoutParams.MATCH_PARENT, bottomLineHeight.toInt())
            bottomLineView?.layoutParams = bottomLp
            val bottomLl = LayoutParams(LayoutParams.MATCH_PARENT, bottomLineHeight.toInt())
            bottomLl.marginStart = bottomLineToBothSides.toInt()
            bottomLl.marginEnd = bottomLineToBothSides.toInt()
            bottomLl.gravity = Gravity.BOTTOM
            addView(bottomLineView, bottomLl)
        }
    }

    /**
     * 添加自定义布局
     */
    private fun initCustomizeLayout() {
        customizeView = LayoutInflater.from(context).inflate(customizeLayout, null)
        addView(customizeView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mWidth = getMySize(widthMeasureSpec)
        val mHeight = getMySize(heightMeasureSpec) + mStatusBarHeight + bottomLineHeight.toInt()
        setMeasuredDimension(mWidth, mHeight)
    }

    /**
     * 获取测量大小
     */
    private fun getMySize(measureSpec: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = when (specMode) {
            //确切大小,所以将得到的尺寸给view
            MeasureSpec.EXACTLY -> specSize
            //此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
            MeasureSpec.AT_MOST -> dp2px(46F).coerceAtMost(specSize)
            else -> dp2px(46F)
        }
        return result
    }

    private fun dp2px(dpValue: Float): Int {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    private fun getDefaultTextColor(): Int {
        return Color.parseColor("#333333")
    }

    private fun getDefaultBottomLineColor(): Int {
        return Color.parseColor("#F5F5F5")
    }

    /**
     * 是否设置沉浸式
     */
    fun setImmersive(immersive: Boolean) {
        mImmersive = immersive
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight()
        } else {
            mStatusBarHeight = 0
        }
        setPadding(0, mStatusBarHeight, 0, 0)
        invalidate()
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     * @return
     */
    fun getStatusBarHeight(): Int {
        return getInternalDimensionSize(Resources.getSystem(), "status_bar_height")
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

}