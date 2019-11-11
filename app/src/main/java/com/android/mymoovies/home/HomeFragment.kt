package com.android.mymoovies.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mymoovies.R
import com.android.mymoovies.databinding.FragmentHomeBinding
import com.android.mymoovies.domain.Movie

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, HomeViewModelFactory(activity.application))
            .get(HomeViewModel::class.java)
    }

    private var viewModelAdapter: DevByteAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<Movie>> { movies ->
            movies?.apply {
                viewModelAdapter?.movies = movies
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModelAdapter = DevByteAdapter(MovieClick {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
//            val packageManager = context?.packageManager ?: return@MovieClick

            // Try to generate a direct intent to the YouTube app
//            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
//            if(intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
//                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
//            }

//            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    class MovieClick(val block: (Movie) -> Unit) {
        /**
         * Called when a video is clicked
         *
         * @param video the video that was clicked
         */
        fun onClick(video: Movie) = block(video)
    }

    class DevByteAdapter(private val callback: MovieClick) : RecyclerView.Adapter<DevByteViewHolder>() {

        /**
         * The videos that our Adapter will show
         */
        var movies: List<Movie> = emptyList()
            set(value) {
                field = value
                // For an extra challenge, update this to use the paging library.

                // Notify any registered observers that the data set has changed. This will cause every
                // element in our RecyclerView to be invalidated.
                notifyDataSetChanged()
            }

        /**
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
            val withDataBinding: FragmentHomeBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                DevByteViewHolder.LAYOUT,
                parent,
                false)
            return DevByteViewHolder(withDataBinding)
        }

        override fun getItemCount() = movies.size

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
         * position.
         */
        override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
            holder.viewDataBinding.also {
                it.movie = movies[position]
                it.movieCallback = callback
            }
        }

    }

    /**
     * ViewHolder for DevByte items. All work is done by data binding.
     */
    class DevByteViewHolder(val viewDataBinding: FragmentHomeBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.video_item
        }
    }

}


