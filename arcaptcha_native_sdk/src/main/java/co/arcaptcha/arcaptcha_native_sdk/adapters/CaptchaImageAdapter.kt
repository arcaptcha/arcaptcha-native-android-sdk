package co.arcaptcha.arcaptcha_native_sdk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import co.arcaptcha.arcaptcha_native_sdk.R
import com.bumptech.glide.Glide

class CaptchaImageAdapter(
    private val context: Context,
    private var imageUrls: List<String> = ArrayList(),
    protected var onAdd: () -> Unit = {},
    protected var onRemove: () -> Unit = {},
) : BaseAdapter() {
    public var isEnabled: Boolean = true
    private val selectedPositions = mutableSetOf<Int>()

    override fun getCount(): Int = imageUrls.size
    override fun getItem(position: Int): Any = imageUrls[position]
    override fun getItemId(position: Int): Long = position.toLong()
    fun getSelectedIndices(): Set<Int> = selectedPositions

    fun setCallbacks(onRemove: () -> Unit, onAdd: () -> Unit){
        this.onRemove = onRemove
        this.onAdd = onAdd
    }

    fun setImages(images: List<String>){
        this.imageUrls = images
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    fun applyNormalStyle(imageView: ImageView, checkmark: ImageView){
        imageView.scaleX = 1f
        imageView.scaleY = 1f
        imageView.alpha = 1f
        checkmark.visibility = View.GONE
    }

    fun applyActiveStyle(imageView: ImageView, checkmark: ImageView){
        imageView.scaleX = 0.9f
        imageView.scaleY = 0.9f
        imageView.alpha = 0.5f
        checkmark.visibility = View.VISIBLE
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val checkmark = view.findViewById<ImageView>(R.id.checkmarkIcon)

        Glide.with(context)
            .load(imageUrls[position])
            .into(imageView)

        val isSelected = position in selectedPositions
        if(isSelected) applyActiveStyle(imageView, checkmark)
        else applyNormalStyle(imageView, checkmark)

        view.setOnClickListener {
            if(!isEnabled) return@setOnClickListener
            
            if (position in selectedPositions) {
                selectedPositions.remove(position)
                applyNormalStyle(imageView, checkmark)
                onRemove()
            } else {
                selectedPositions.add(position)
                applyActiveStyle(imageView, checkmark)
                onAdd()
            }
        }

        return view
    }
}