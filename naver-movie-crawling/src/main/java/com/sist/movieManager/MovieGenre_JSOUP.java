package com.sist.movieManager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.movieDAO.NaverMovieDAO;
import com.sist.movieVO.NaverMovieGenreMapperVO;
import com.sist.movieVO.NaverMovieGenreVO;
import com.sist.movieVO.NaverMovieVO;

@Component
public class MovieGenre_JSOUP {

	private final static String[] LIST = { "드라마", "판타지", "서부", "공포", "멜로", "로맨스", 
			"모험", "스릴러", "느와르", "컬트", "다큐멘터리", "코미디", "가족", 
			"미스터리",
			"전쟁", "애니메이션", "무협", "범죄", "뮤지컬", "SF", "액션", 
			"에로", "서스펜스", "서사", "블랙코미디", "실험", "공연실황" };
	@Autowired
	private NaverMovieDAO dao;

	public void insertMovieGenreMapper(){
		List<NaverMovieVO> list = dao.getMovieIdAndGenreList();
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getGenre() == null){
				System.out.println("movie : " + list.get(i).getTitle() + " genre value is null");
				continue;
			}
			NaverMovieGenreMapperVO vo = new NaverMovieGenreMapperVO();
			vo.setMovie_id(list.get(i).getMovie_id());
			for(int j = 0; j < LIST.length; j++){
				if((list.get(i).getGenre()).contains(LIST[j])){
					vo.setGenre(LIST[j]);
					System.out.println("input data for mapper : " + vo.getMovie_id() + "," + vo.getGenre());
					dao.insertMovieGenreMapper(vo);
				}
			}
		}
	}
	public void insertGenre() {
		
		
		for(int i = 0; i < LIST.length; i++){
			NaverMovieGenreVO vo = new NaverMovieGenreVO();
			vo.setGenre_no(i+1);
			vo.setGenre(LIST[i]);
			System.out.println("genre no : " + (i+1) +" , genre : " + LIST[i]);
			dao.insertMovieGenre(vo);
		}
		System.out.println("insert ok!");
	}
}
