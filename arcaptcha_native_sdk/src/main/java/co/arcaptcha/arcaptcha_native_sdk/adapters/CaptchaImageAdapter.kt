package co.arcaptcha.arcaptcha_native_sdk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.utils.ScreenUtils
import com.bumptech.glide.Glide

class CaptchaImageAdapter(
    private val context: Context,
    private var imageUrls: List<String> = ArrayList(),
    protected var onAdd: () -> Unit = {},
    protected var onRemove: () -> Unit = {},
) : BaseAdapter() {
    public var isEnabled: Boolean = true
    private val selectedPositions = mutableSetOf<Int>()

    // Layout parameters
    var gridWidth: Int = 0
    var numColumns: Int = 3
    var horizontalSpacingDp: Int = 10
    var verticalSpacingDp: Int = 10
    var gridPaddingDp: Int = 4

    override fun getCount(): Int = imageUrls.size
    override fun getItem(position: Int): Any = imageUrls[position]
    override fun getItemId(position: Int): Long = position.toLong()
    fun getSelectedIndices(): Set<Int> = selectedPositions

    fun setCallbacks(onRemove: () -> Unit, onAdd: () -> Unit) {
        this.onRemove = onRemove
        this.onAdd = onAdd
    }

    fun setImages(images: List<String>) {
        this.imageUrls = images
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    private fun updateStyle(imageView: ImageView, checkmark: ImageView, selected: Boolean) {
        imageView.scaleX = if (selected) 0.9f else 1f
        imageView.scaleY = if (selected) 0.9f else 1f
        imageView.alpha = if (selected) 0.5f else 1f
        checkmark.visibility = if (selected) View.VISIBLE else View.GONE
    }

    private fun getFinalGridWidth() : Int {
        return if(gridWidth > 0) gridWidth
        else context.resources.displayMetrics.widthPixels
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val checkmark = view.findViewById<ImageView>(R.id.checkmarkIcon)

        val screenWidth = getFinalGridWidth()
        val spacingPx = ScreenUtils.dpToPx(context, horizontalSpacingDp)
        val paddingPx = ScreenUtils.dpToPx(context, gridPaddingDp)
        val totalSpacing = spacingPx * (numColumns - 1)
        val totalPadding = paddingPx * 2
        val availableWidth = screenWidth - totalSpacing - totalPadding
        val itemSize = availableWidth / numColumns

        val layoutParams = view.layoutParams ?: AbsListView.LayoutParams(itemSize, itemSize)
        layoutParams.width = itemSize
        layoutParams.height = itemSize
        view.layoutParams = layoutParams

        Glide.with(context)
            .load(imageUrls[position])
            .into(imageView)

        updateStyle(imageView, checkmark, position in selectedPositions)

        view.setOnClickListener {
            if (!isEnabled) return@setOnClickListener

            val wasSelected = position in selectedPositions
            if (wasSelected) {
                selectedPositions.remove(position)
                onRemove()
            } else {
                selectedPositions.add(position)
                onAdd()
            }
            updateStyle(imageView, checkmark, !wasSelected)
        }

        return view
    }

    fun calculateGridHeight(): Int {
        val screenWidth = getFinalGridWidth()
        val spacingPx = ScreenUtils.dpToPx(context, horizontalSpacingDp)
        val paddingPx = ScreenUtils.dpToPx(context, gridPaddingDp)

        val totalHorizontalSpacing = spacingPx * (numColumns - 1)
        val totalPadding = paddingPx * 2
        val availableWidth = screenWidth - totalHorizontalSpacing - totalPadding
        val itemSize = availableWidth / numColumns

        val numRows = (imageUrls.size + numColumns - 1) / numColumns
        val totalVerticalSpacing = ScreenUtils.dpToPx(context, verticalSpacingDp) * (numRows - 1)

        return itemSize * numRows + totalVerticalSpacing + totalPadding
    }
}