package com.sist.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sist.movieManager.MovieMedia_JSOUP;
import com.sist.movieManager.MoviePictures_JSOUP;
import com.sist.movieManager.Movies_JSOUP;
import com.sist.movieManager.Pair;
import com.sist.movieVO.NaverMovieVO;

@Component
public class InputPicturesAndMedia {

	@Autowired
	MovieMedia_JSOUP movieMedia_JSOUP;
	@Autowired
	MoviePictures_JSOUP moviePictures_JSOUP;
	@Autowired
	Movies_JSOUP movies_JSOUP;
	
	public static void main(String[] args) {
		//1. id를 탐색한다.
		//2. id로 미디어와 픽쳐를 긁어온다
		//3. db에 저장한다
		List<NaverMovieVO> res = new ArrayList<NaverMovieVO>();
		List<Pair> list;
		List<List<String>> movieList = new ArrayList<List<String>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		
		System.out.println("start");
		ApplicationContext ac = 
				new ClassPathXmlApplicationContext("app.xml");
		InputPicturesAndMedia mc = ac.getBean(InputPicturesAndMedia.class);
		mc.moviePictures_JSOUP.getNaverMoviePicturesToDB();
		
	}

}
