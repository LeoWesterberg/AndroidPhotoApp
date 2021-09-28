package se.lth.cs.leow.photoapp

import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.fragment_new_photo.*
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var navigation:NavController;

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var viewModel:SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java);
        viewModel.getListOfPoints().observe(requireActivity()){
            if(imagesView != null){
                imagesView.removeAllViews()
                for (point in it){
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, Uri.parse(point.uri))
                    val rotatedBitmap = rotatedBitmap(bitmap)
                    val im = ImageView(requireContext())
                    im.setImageBitmap(rotatedBitmap)
                    val fr = LinearLayout(requireContext())
                    val tx = TextView(requireContext())
                    tx.text ="${point.latitude} °N, ${point.longitude} °E "
                    tx.width = WRAP_CONTENT
                    tx.gravity = CENTER_HORIZONTAL
                    fr.addView(tx)
                    fr.addView(im)
                    val layoutparams = LinearLayout.LayoutParams(1200,1777)
                    layoutparams.gravity = Gravity.TOP
                    layoutparams.gravity = CENTER_HORIZONTAL
                    layoutparams.bottomMargin = 60
                    fr.orientation = LinearLayout.VERTICAL
                    fr.setBackgroundResource(R.drawable.common_google_signin_btn_text_light_normal)
                    fr.layoutParams = layoutparams
                    imagesView.addView(fr)

                }
            }

        }
    }

    private fun rotatedBitmap(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()
        matrix.setRotate(90F)
        return Bitmap.createBitmap(bitmap,0,0, bitmap.width, bitmap.height, matrix, true)

    }


}