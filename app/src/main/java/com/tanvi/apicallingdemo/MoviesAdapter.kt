package com.tanvi.apicallingdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MoviesAdapter(var movies:MutableList<Result>):RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {
   // class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
         holder.bind(movies[position], position+1)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
    fun updateMovieList(updatedMovies: List<Result>) {
/*
        val diffCallback = MovieDiffCallback(this.movies, updatedMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.movies.clear()
        this.movies.addAll(updatedMovies)
        diffResult.dispatchUpdatesTo(this)
*/
       movies = updatedMovies as MutableList<Result>
        notifyDataSetChanged()

    }
  // applying paginations
    class MoviesViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        private val photo: ImageView = itemView.findViewById(R.id.movie_photo)
        private val title:TextView = itemView.findViewById(R.id.movie_title)
        private val overview:TextView = itemView.findViewById(R.id.movie_overview)
        private val rating: TextView = itemView.findViewById(R.id.movie_rating)
        private val sNo: TextView = itemView.findViewById(R.id.sNo)

        fun bind(movie: Result, position: Int) {
            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(photo)
            title.text = "Title: "+movie.title
            overview.text = movie.overview
            rating.text = "Rating : "+movie.vote_average.toString()
            sNo.text = "#${position}"

        }
    }

     class MovieDiffCallback(oldList: List<Result>, newList: List<Result>) :
         DiffUtil.Callback() {
         private val oldMovieListList: List<Result>
         private val newMovieResultLists: List<Result>
         override fun getOldListSize(): Int {
             return oldMovieListList.size
         }

         override fun getNewListSize(): Int {
             return newMovieResultLists.size
         }

         override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
             return oldMovieListList[oldItemPosition].id == newMovieResultLists[newItemPosition].id
         }

         override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
             val oldEmployee: Result = oldMovieListList[oldItemPosition]
             val newEmployee: Result = newMovieResultLists[newItemPosition]
             return oldEmployee.title == newEmployee.title
         }
         @Nullable
         override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
             return super.getChangePayload(oldItemPosition, newItemPosition)
         }

         init {
             oldMovieListList = oldList
             newMovieResultLists = newList
         }
     }
}
