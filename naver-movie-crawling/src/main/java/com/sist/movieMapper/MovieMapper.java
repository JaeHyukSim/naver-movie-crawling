package com.sist.movieMapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sist.movieVO.NaverMovieGenreMapperVO;
import com.sist.movieVO.NaverMovieGenreVO;
import com.sist.movieVO.NaverMoviePictureVO;
import com.sist.movieVO.NaverMovieVO;

public interface MovieMapper {
	@Insert("INSERT INTO naver_re_movies(movie_id,"
			+ "title,"
			+ "grade,"
			+ "opening_date,"
			+ "genre,"
			+ "country,"
			+ "running_time,"
			+ "hit,"
			+ "audience_count,"
			+ "story,"
			+ "poster) VALUES ("
			+ "#{movie_id,jdbcType=INTEGER}, "
			+ "#{title,jdbcType=VARCHAR}, "
			+ "#{grade,jdbcType=VARCHAR}, "
			+ "#{opening_date,jdbcType=DATE}, "
			+ "#{genre,jdbcType=VARCHAR}, "
			+ "#{country,jdbcType=VARCHAR}, "
			+ "#{running_time,jdbcType=INTEGER}, "
			+ "#{hit,jdbcType=INTEGER}, "
			+ "#{audience_count,jdbcType=INTEGER}, "
			+ "#{story,jdbcType=VARCHAR}, "
			+ "#{poster,jdbcType=VARCHAR})")
	public boolean insertNaverMovies(NaverMovieVO vo);
	
	@Select("SELECT COUNT(*) FROM naver_re_movies WHERE movie_id = #{id}")
	public int findNaverMoviesById(int id);
	
	@Insert("INSERT INTO movie_re_pictures(picture_id, "
			+ "movie_id, "
			+ "url, "
			+ "img) "
			+ "VALUES("
			+ "(SELECT NVL(MAX(picture_id)+1,1) FROM movie_re_pictures), "
			+ "#{movie_id,jdbcType=INTEGER}, "
			+ "#{url,jdbcType=VARCHAR}, "
			+ "#{img,jdbcType=VARCHAR})")
	public boolean insertNaverMoviePicturesAndMedia(NaverMoviePictureVO vo);
	
	@Select("SELECT movie_id FROM naver_re_movies")
	public List<Integer> getMovieIdList();
	
	@Select("SELECT title FROM naver_re_movies")
	public List<String> getMovieTitleList();
	
	@Select("SELECT COUNT(*) FROM movie_re_pictures WHERE url = #{url}")
	public int findMoviePicturesByYoutubeUrl(String url);
	
	@Select("SELECT title FROM naver_re_movies WHERE movie_id = #{movie_id}")
	public String getMovieTitleListById(int movie_id);
	
	@Select("SELECT * FROM movie_re_pictures WHERE url = #{url}")
	public NaverMoviePictureVO test(String url);
	
	@Select("SELECT movie_id, genre FROM naver_re_movies")
	public List<NaverMovieVO> getMovieIdAndGenreList();
	
	@Insert("INSERT INTO naver_movie_genre("
			+ "genre_no,"
			+ "genre)"
			+ "VALUES("
			+ "#{genre_no},"
			+ "#{genre})")
	public int insertMovieGenre(NaverMovieGenreVO vo);
	
	@Insert("INSERT INTO movie_genre_mapper("
			+ "genre,"
			+ "movie_id) VALUES("
			+ "#{genre},"
			+ "#{movie_id})")
	public int insertMovieGenreMapper(NaverMovieGenreMapperVO vo);
}
