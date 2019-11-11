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
import com.android.mymoovies.databinding.VideoItemBinding
import com.android.mymoovies.domain.Movie

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, HomeViewModelFactory(activity.application))
            .get(HomeViewModel::class.java)
    }

    private var viewModelAdapter: MovieAdapter? = null

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

        viewModelAdapter = MovieAdapter(MovieClick {

//            val packageManager = context?.packageManager ?: return@MovieClick

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

}

class MovieClick(val block: (Movie) -> Unit) {
    fun onClick(video: Movie) = block(video)
}

class MovieAdapter(private val callback: MovieClick) : RecyclerView.Adapter<MovieViewHolder>() {

    var movies: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val withDataBinding: VideoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MovieViewHolder.LAYOUT,
            parent,
            false)
        return MovieViewHolder(withDataBinding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.movie = movies[position]
            it.movieCallback = callback
        }
    }

}

class MovieViewHolder(val viewDataBinding: VideoItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.video_item
    }
}


