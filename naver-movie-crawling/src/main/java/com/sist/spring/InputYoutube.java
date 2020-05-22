package com.sist.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sist.movieManager.MovieMedia_JSOUP;
import com.sist.movieManager.MovieYoutube_JSOUP;

@Component
public class InputYoutube {

	@Autowired
	private MovieYoutube_JSOUP movieYoutube_JSOUP;
	
	public static void main(String[] args) {
		System.out.println("start");
		ApplicationContext ac = 
				new ClassPathXmlApplicationContext("app.xml");
		InputYoutube iu = ac.getBean(InputYoutube.class);
		iu.movieYoutube_JSOUP.getMovieYoutubeByTitle();
		System.out.println("end");
	}

}
