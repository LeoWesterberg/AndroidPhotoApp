package se.lth.cs.leow.photoapp

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MapsFragment : Fragment() {
    private lateinit var viewModel:SharedViewModel
    private lateinit var googleMaps:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMaps = googleMap
        viewModel.getListOfPoints().observe(requireActivity()){
            googleMap.clear()
            for (point in it) {
                val position = LatLng(point.latitude!!,point.longitude!!)
                val rotatedBmp = rotateBitMap(Uri.parse(point.uri))
                val scaledBmp = Bitmap.createScaledBitmap(rotatedBmp, 135, 240, false)

                googleMap.addMarker(MarkerOptions().position(position).title(point.uri + "+" + point.id).icon(
                    BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(scaledBmp))))

            }

            if(viewModel.lastLatitude != null){
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(viewModel.lastLatitude!!,viewModel.lastLongitude!!)))
            }
        }
        googleMap.setOnMarkerClickListener{
            val uri = Uri.parse(it.title.split("+")[0])
            val id = it.title.split("+")[1]
            val rotatedBmp = rotateBitMap(uri)
            viewModel.setLastCoordinates(it.position.latitude,it.position.longitude)
            val fragment = ImageFragment.newInstance(rotatedBmp,id.toInt())
            (activity as MainActivity?)?.switchFragment(fragment)

            return@setOnMarkerClickListener true
        }
    }
    private fun rotateBitMap(uri:Uri): Bitmap{
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val matrix = Matrix()
        matrix.setRotate(90F)
        return Bitmap.createBitmap(bitmap,0,0, bitmap.width, bitmap.height, matrix, true)
    }

    //Found from internet(Make your own later)
    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java);
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        }
    }

