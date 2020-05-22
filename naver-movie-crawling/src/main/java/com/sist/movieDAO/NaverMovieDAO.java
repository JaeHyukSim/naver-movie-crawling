package com.sist.movieDAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.movieMapper.MovieMapper;
import com.sist.movieVO.NaverMovieGenreMapperVO;
import com.sist.movieVO.NaverMovieGenreVO;
import com.sist.movieVO.NaverMoviePictureVO;
import com.sist.movieVO.NaverMovieVO;

@Repository
public class NaverMovieDAO {
	@Autowired
	private MovieMapper mapper;
	
	public boolean insertNaverMovies(NaverMovieVO vo){
			System.out.println("input vo : " + vo.getTitle());
			mapper.insertNaverMovies(vo);
		
		return true;
	}
	
	public boolean findNaverMoviesById(int id) {
		int res = mapper.findNaverMoviesById(id);
		if(res == 0){
			return false;
		}
		return true;
	}
	
	public boolean insertNaverMoviePicturesAndMedia(NaverMoviePictureVO vo){
		System.out.println("input vo : " + vo.getImg());
		mapper.insertNaverMoviePicturesAndMedia(vo);
		return true;
	}
	
	public List<Integer> getMovieIdList(){
		List<Integer> list = new ArrayList();
		list = mapper.getMovieIdList();
		return list;
	}
	
	public List<String> getMovieTitleList(){
		List<String> list = new ArrayList<String>();
		list = mapper.getMovieTitleList();
		return list;
	}
	public String getMovieTitleListById(int movie_id){
		return mapper.getMovieTitleListById(movie_id);
	}
	public boolean findMoviePicturesByYoutubeUrl(String url){
		int res = 0;
		res = mapper.findMoviePicturesByYoutubeUrl(url);
		NaverMoviePictureVO vo = mapper.test(url);
		if(res == 0){
			return false;
		}
		return true;
	}
	
	public List<NaverMovieVO> getMovieIdAndGenreList(){
		return mapper.getMovieIdAndGenreList();
	}
	
	public int insertMovieGenre(NaverMovieGenreVO vo){
		return mapper.insertMovieGenre(vo);
	}
	
	public int insertMovieGenreMapper(NaverMovieGenreMapperVO vo){
		return mapper.insertMovieGenreMapper(vo);
	}
}
