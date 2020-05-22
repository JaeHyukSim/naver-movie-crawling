package com.sist.movieManager;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.movieDAO.NaverMovieDAO;
import com.sist.movieVO.NaverMoviePictureVO;

@Component
public class MovieMedia_JSOUP {
	
	@Autowired
	NaverMovieDAO dao;
	
	private Jsoup jsoup;
	private final static String URL = "https://movie.naver.com/movie/bi/mi/media.nhn?code=#{code}";
	private final static String MEDIA_URL = "https://movie.naver.com";

	// 한 url의 media url로 이동하는 메서드 by movie_id
	public List<Pair> searchMediasByUrl(int movieId) {
		String id = String.valueOf(movieId);
		String mediaUrl = URL.replace("#{code}", id);
		List<Pair> list = new ArrayList<Pair>();
		Document doc = null;
		try {
			doc = jsoup.connect(mediaUrl).get();
			System.out.println("list size : " + list.size());
			getMediasInList(doc, list, id);
			mediaUrl = URL.replace("#{code}", id);
			doc = jsoup.connect(mediaUrl).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// media 리스트를 모두 리턴하는 메서드
	public void getMediasInList(Document doc, List<Pair> list, String id) {

		Elements medias = doc.select(".video .ifr_module ul li a.video_obj");
		System.out.println("medias : " + medias.outerHtml());
		for (Element media : medias) {
			Pair pair = new Pair();
			String mediaUrl = media.attr("href");
			pair.first = MEDIA_URL + mediaUrl;
			pair.third = media.selectFirst("img").attr("src");
			list.add(pair);
			NaverMoviePictureVO vo = new NaverMoviePictureVO();
			vo.setMovie_id(Integer.parseInt(id));
			vo.setImg(pair.third);
			vo.setUrl(pair.first);
			
			dao.insertNaverMoviePicturesAndMedia(vo);
		}
	}
}
