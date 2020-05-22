package com.sist.movieManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.movieConfiguration.Config;
import com.sist.movieDAO.NaverMovieDAO;
import com.sist.movieVO.NaverMoviePictureVO;

@Component
public class MovieYoutube_JSOUP {
	@Autowired
	private NaverMovieDAO dao;

	public void getMovieYoutubeByTitle() {
		List<Integer> idList = dao.getMovieIdList();
		System.out.println("idList size : " + idList.size());
		for (int s : idList) {
			try {
				getMovieYoutube(s);
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getMovieYoutube(int movie_id) {
		String title = dao.getMovieTitleListById(movie_id);
		System.out.println("title : " + title);
		String key = "";
		String img = "";
		int index = 0;
		try {
			Document doc = Jsoup.connect("https://www.youtube.com/results?search_query=" + title + " 예고편").get();
			//System.out.println("doc html: " + doc.html());
			//System.out.println("doc text: " + doc.text());
			System.out.println("doc index of url : " + doc.html().indexOf("\"url\":\"https"));
			index = doc.html().indexOf("\"url\":\"https")+7;
			img = doc.html().substring(index);
			img = img.substring(0,img.indexOf("\""));
			System.out.println("img : " + img);
			Pattern p = Pattern.compile("/watch\\?v=[^가-힣]+");
			Matcher m = p.matcher(doc.toString());
			// 찾기
			while (m.find()) {
				String temp = m.group();
				key = temp.substring(temp.indexOf("=") + 1, temp.indexOf("\""));
				System.out.println("key : " + key);
				String youtubeUrl = Config.YOUTUBE_EMBED_URL_TEMPLATE.replace("#{key}", key);
				System.out.println("youtubeUrl : " + youtubeUrl);
				if(dao.findMoviePicturesByYoutubeUrl(youtubeUrl) == true){
					System.out.println("이미 해당 유튜브 데이터가 존재합니다");
					return;
				}
				NaverMoviePictureVO vo = new NaverMoviePictureVO();
				vo.setMovie_id(movie_id);
				vo.setUrl(youtubeUrl);
				vo.setImg(img);
				System.out.println("vo : " + vo.toString());
				dao.insertNaverMoviePicturesAndMedia(vo);
				break;
				// System.out.println(temp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
