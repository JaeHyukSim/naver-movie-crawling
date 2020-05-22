package com.sist.movieManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.movieConfiguration.Config;
import com.sist.movieDAO.NaverMovieDAO;
import com.sist.movieVO.NaverMoviePictureVO;
import com.sist.movieVO.NaverMovieVO;

@Component
public class MoviePictures_JSOUP {
	
	@Autowired
	NaverMovieDAO dao;
	
	private Jsoup jsoup;
	private final static String URL = "https://movie.naver.com/movie/bi/mi/photo.nhn?code=#{code}&page=#{page}#movieEndTabMenu";
	private Document doc;
	private final static String NAVER_URL = "https://movie.naver.com/movie/sdb/browsing/bmovie_year.nhn";
	private final static String NAVER = "https://movie.naver.com";
	private final static String EXCEPTION_MESSAGE = "fail to get document";
	// 한 url의 picture url로 이동하는 메서드 by movie_id
	public List<String> searchPicturesByUrl(int movieId) {
		String id = String.valueOf(movieId);
		int num = 1;
		String number = String.valueOf(num);
		String pictureUrl = URL.replace("#{code}", id)
							   .replace("#{page}", number);
		int count = 0;
		List<String> list = new ArrayList<String>();
		Document doc = null;
		try {
			doc = jsoup.connect(pictureUrl).get();
			count = getPicturesCount(doc);
			while (list.size() < count) {
				getPicturesInList(doc, list, id);
				num++;
				number = String.valueOf(num);
				pictureUrl = URL.replace("#{code}", id)
								.replace("#{page}", number);
				doc = jsoup.connect(pictureUrl).get();

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 총 사진 개수를 구하는 메서드
	public int getPicturesCount(Document doc) {
		Element countNode = doc.selectFirst(".obj_section2 .photo .title_area .count");
		String countStr = countNode.text();
		int count = 0;
		try {
			countStr = countStr.substring(countStr.indexOf("/") + 2, countStr.indexOf("건"));
			count = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("count -> 0");
		}
		return count;
	}

	// 사진 리스트를 모두 리턴하는 메서드
	public void getPicturesInList(Document doc, List<String> list, String id) {

		Elements pictures = doc.select(".photo .gallery_group li a img");
		System.out.println("pictures : "+ pictures.html());
		for (Element picture : pictures) {
			String pictureUrl = picture.attr("src");
			list.add(pictureUrl);
			
			NaverMoviePictureVO vo = new NaverMoviePictureVO();
			vo.setMovie_id(Integer.parseInt(id));
			vo.setImg(pictureUrl);
			
			dao.insertNaverMoviePicturesAndMedia(vo);
			
			System.out.println(pictureUrl);
		}
	}
	//List 통합 메서드
	public void getNaverMoviePicturesToDB(){
		List<Integer> list = dao.getMovieIdList();
		int size = list.size();
		for(int i = 0; i < size; i++){
			searchPicturesByUrl(list.get(i));
		}
	}
}
