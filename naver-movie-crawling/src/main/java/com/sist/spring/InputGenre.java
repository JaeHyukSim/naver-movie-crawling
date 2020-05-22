package com.sist.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sist.movieManager.MovieGenre_JSOUP;

@Component
public class InputGenre {

	@Autowired
	MovieGenre_JSOUP movieGenre_JSOUP;
	
	public static void main(String[] args) {
		ApplicationContext ac = 
				new ClassPathXmlApplicationContext("app.xml");
		InputGenre ig = ac.getBean(InputGenre.class);
		
		//ig.movieGenre_JSOUP.insertGenre();
		ig.movieGenre_JSOUP.insertMovieGenreMapper();
	}
}
