package com.sist.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sist.movieManager.Movies_JSOUP;
import com.sist.movieVO.NaverMovieVO;

@Component
public class MainClass {

	@Autowired
	private Movies_JSOUP mj;
	
	public static void main(String[] args) {
		System.out.println("start");
		ApplicationContext ac = 
				new ClassPathXmlApplicationContext("app.xml");
		MainClass mc = ac.getBean(MainClass.class);
		List<NaverMovieVO> naverMovieList = mc.mj.getAllNaverMovie();
		System.out.println("success!");
		
	}

}
