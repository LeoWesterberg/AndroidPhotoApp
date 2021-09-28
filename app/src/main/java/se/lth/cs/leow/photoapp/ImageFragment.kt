package se.lth.cs.leow.photoapp

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_image.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "uri"
private const val ARG_PARAM2 = "id"
private lateinit var viewModel:SharedViewModel

class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var uri: Bitmap? = null
    private var id:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getParcelable(ARG_PARAM1)
            id = it.getInt(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java);
        popUpView.setImageBitmap(uri)

        exitButton.setOnClickListener {
            getFragmentManager()?.popBackStackImmediate();
        }
        removeButton.setOnClickListener {
            viewModel.removePoint(id!!)
            getFragmentManager()?.popBackStackImmediate();
        }


    }

    companion object {
        @JvmStatic fun newInstance(bitmap: Bitmap,id:Int) =
                ImageFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, bitmap)
                        putInt(ARG_PARAM2, id)

                    }
                }
    }
}