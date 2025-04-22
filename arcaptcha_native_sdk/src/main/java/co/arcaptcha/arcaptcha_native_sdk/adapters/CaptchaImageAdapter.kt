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
    private var imageUrls: List<String> = ArrayList()
) : BaseAdapter() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun getCount(): Int = imageUrls.size
    override fun getItem(position: Int): Any = imageUrls[position]
    override fun getItemId(position: Int): Long = position.toLong()
    fun getSelectedIndices(): Set<Int> = selectedPositions

    fun setImages(images: List<String>){
        this.imageUrls = images
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val overlay = view.findViewById<View>(R.id.selectionOverlay)

        Glide.with(context)
            .load(imageUrls[position])
            .into(imageView)

        overlay.visibility = if (position in selectedPositions) View.VISIBLE else View.GONE

        view.setOnClickListener {
            if (position in selectedPositions) {
                selectedPositions.remove(position)
                overlay.visibility = View.GONE
            } else {
                selectedPositions.add(position)
                overlay.visibility = View.VISIBLE
            }
        }

        return view
    }
}