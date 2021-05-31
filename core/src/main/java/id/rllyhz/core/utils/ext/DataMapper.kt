package id.rllyhz.core.utils.ext

import id.rllyhz.core.data.local.entity.FavMovie
import id.rllyhz.core.data.local.entity.FavTVShow
import id.rllyhz.core.data.remote.response.MovieDetailResponse
import id.rllyhz.core.data.remote.response.MovieResponse
import id.rllyhz.core.data.remote.response.TVShowDetailResponse
import id.rllyhz.core.data.remote.response.TVShowResponse
import id.rllyhz.core.domain.model.Movie
import id.rllyhz.core.domain.model.TVShow
import id.rllyhz.core.utils.DataHelper

fun List<MovieResponse>.asModels(): List<Movie> {
    val allMovies = mutableListOf<Movie>()

    for (movie in this) {
        movie.apply {
            allMovies.add(
                Movie(
                    id,
                    posterPath ?: "",
                    backdropPath ?: "",
                    title,
                    null,
                    null,
                    rate,
                    releasedDate,
                    language,
                    synopsis,
                    null
                )
            )
        }
    }

    return allMovies
}

@JvmName("asModelsTVShowResponse")
fun List<TVShowResponse>.asModels(): List<TVShow> {
    val allTvShows = mutableListOf<TVShow>()

    for (tvShow in this) {
        tvShow.apply {
            allTvShows.add(
                TVShow(
                    id,
                    posterPath ?: "",
                    backdropPath ?: "",
                    title,
                    null,
                    null,
                    rate,
                    releasedDate,
                    language,
                    synopsis,
                    null
                )
            )
        }
    }

    return allTvShows
}

fun MovieDetailResponse.asModels(): Movie =
    Movie(
        this.id,
        this.posterPath ?: "",
        this.backdropPath ?: "",
        this.title,
        DataHelper.getGenresStringFormat(genres),
        duration,
        rate,
        releasedDate,
        language,
        synopsis,
        status
    )

fun TVShowDetailResponse.asModels(): TVShow =
    TVShow(
        this.id,
        this.posterPath ?: "",
        this.backdropPath ?: "",
        this.title,
        DataHelper.getGenresStringFormat(genres),
        duration[0],
        rate,
        releasedDate,
        language,
        synopsis,
        status
    )

fun Movie.asFavModel(): FavMovie =
    FavMovie(this.id, posterPath, backdropPath, title, year, rating)

fun TVShow.asFavModel(): FavTVShow =
    FavTVShow(this.id, posterPath, backdropPath, title, year, rating)

fun FavMovie.asModel(): Movie =
    Movie(
        this.id,
        this.posterPath,
        this.backdropPath,
        this.title,
        null,
        null,
        0f,
        "",
        "",
        "",
        null
    )

fun FavTVShow.asModel(): TVShow =
    TVShow(
        this.id,
        this.posterPath,
        this.backdropPath,
        this.title,
        null,
        null,
        0f,
        "",
        "",
        "",
        null
    )

fun Movie.getDateInString() = DataHelper.getDateInString(releasedAt)
fun TVShow.getDateInString() = DataHelper.getDateInString(releasedAt)