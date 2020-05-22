package com.sist.movieManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sist.movieConfiguration.Config;
import com.sist.movieDAO.NaverMovieDAO;
import com.sist.movieVO.NaverMovieVO;

@Component
public class Movies_JSOUP {

	@Autowired
	private NaverMovieDAO dao;
	@Autowired
	MovieMedia_JSOUP movieMedia_JSOUP;
	@Autowired
	MoviePictures_JSOUP moviePictures_JSOUP;

	private Document doc;
	private final static String NAVER_URL = "https://movie.naver.com/movie/sdb/browsing/bmovie_year.nhn";
	private final static String NAVER = "https://movie.naver.com";
	private final static String EXCEPTION_MESSAGE = "fail to get document";

	public Movies_JSOUP() {
		try {
			String basicUrl = Config.START_URL_TEMPLATE.replace("#{date}", Config.FINAL_YEAR).replace("#{page}", "1");
			doc = Jsoup.connect(basicUrl).get();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
	}

	// VO형태로 모든 뮤비데이터를 삽입하는 메서드
	public void insertNaverMovieByUrl(String movieUrl) {
		try {
			Document document = Jsoup.connect(NAVER_URL + movieUrl).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			NaverMovieVO vo = new NaverMovieVO();

			vo.setMovie_id(Integer.parseInt(getMovieId(movieUrl)));
			vo.setTitle(getMovieTitle(movieUrl, document));
			vo.setGrade(getMovieInfoByKind(movieUrl, "grade=", document));
			try {
				Date openningDate = dateFormat.parse(getMovieInfoByKind(movieUrl, "open=", document));
				vo.setOpening_date(openningDate);

			} catch (Exception e) {
			}
			vo.setGenre(getMovieInfoByKind(movieUrl, "genre=", document));
			vo.setCountry(getMovieInfoByKind(movieUrl, "nation=", document));
			int runningTime = 0;
			try {
				runningTime = Integer.parseInt(getMovieRunningTime(movieUrl, document));
				vo.setRunning_time(runningTime);
			} catch (Exception e) {
			}
			String hitString = getMovieInfoByKind(movieUrl, "view=", document);
			int hit = 0;
			if (hitString.indexOf("예매율") != -1)
				hit = Integer.parseInt(hitString.substring(hitString.indexOf("예매율") + 4, hitString.indexOf("예매율") + 5));
			vo.setHit(hit);
			int audienceCount = 0;
			try {
				audienceCount = Integer.parseInt(getMovieAudienceCount(movieUrl, document));
			} catch (Exception e) {
			}
			vo.setAudience_count(audienceCount);
			vo.setStory(getMovieStory(movieUrl, document));
			vo.setPoster(getMoviePoster(movieUrl, document));
			//System.out.println(vo.toString());
			dao.insertNaverMovies(vo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// VO형태로 모든 뮤비를 출력해주는 메서드
	public List<NaverMovieVO> getAllNaverMovie() {
		List<NaverMovieVO> res = new ArrayList<NaverMovieVO>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

		int page = 0;
		boolean isInit = false;
		String beforeRank = "-1";
		try {

			Document doc2 = doc;
			String url = "";
			String pageStr = "";
			String year = Config.FINAL_YEAR;
			int i = 1;
			while (true) {
				page++;
				pageStr = String.valueOf(page);
				System.out.println("page : " + page);
				System.out.println("i : " + i);
				System.out.println("year : " + year);
				url = Config.START_URL_TEMPLATE.replace("#{date}", year).replace("#{page}", pageStr);
				doc2 = Jsoup.connect(url).get();

				if(page == 1){
					Element rank = doc2.selectFirst(Config.FIRST_URL_QUERY_SELECTOR_TO_SEARCH_RANK);
					System.out.println("rank attr, before Rank : " + rank.attr("alt") + ", " + beforeRank);
					if (rank.attr("alt").equals(beforeRank)) {
						if(i == Config.UNIT_YEAR){
							return res;
						}
						isInit = true;
						i++;
						year = String.valueOf(Integer.parseInt(year)-10000);
						beforeRank = "-1";
						page = 0;
					}
					if(isInit == false){
						beforeRank = rank.attr("alt");
					}else{
						isInit = false;
					}
				}else{
					Element rank = doc2.selectFirst(Config.FIRST_URL_QUERY_SELECTOR_TO_SEARCH_RANK2);
					System.out.println("rank attr, before Rank : " + rank.text() + ", " + beforeRank);
					if (rank.text().equals(beforeRank)) {
						if(i == Config.UNIT_YEAR){
							return res;
						}
						isInit = true;
						i++;
						year = String.valueOf(Integer.parseInt(year)-10000);
						beforeRank = "-1";
						page = 0;
					}
					if(isInit == false){
						beforeRank = rank.text();
					}else{
						isInit = false;
					}
				}
				Elements table = doc2.select(Config.FIRST_URL_QUERY_SELECTOR_TO_SEARCH_DETAIL_PAGE);
				for (Element e : table) {
					String detailNaverMovieUrl = e.attr("href");
					String movieId = detailNaverMovieUrl.substring(detailNaverMovieUrl.indexOf("code=") + 5);
					int id = 0;
					id = Integer.parseInt(movieId);
					// 있을 경우
					if (dao.findNaverMoviesById(id)) {
						System.out.println("아이디가 이미 있다");
						continue;
					} else {
						// 삽입
						//System.out.println("navermoviebyurl : " + NAVER + detailNaverMovieUrl);
						insertNaverMovieByUrl(NAVER + detailNaverMovieUrl);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	// 모든 HTML을 보여주는 메서드
	public String getHTML() {
		String html = "";

		html = doc.outerHtml();

		return html;
	}

	// 무비 아이디를 가져오는 메서드
	public String getMovieId(String url) {
		String res = "";
		res = url.substring(url.indexOf("code=") + 5);
		return res;
	}

	// 무비 제목을 가져오는 메서드
	public String getMovieTitle(String url, Document doc2) {
		String res = "제목이 없습니다!";

		try {
			Element title = doc2.selectFirst(Config.DETAIL_TITLE);
			res = title.text();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
		return res;
	}

	// 무비 정보를 가져오는 메서드
	public String getMovieInfoByKind(String url, String kind, Document doc2) {
		String res = "";

		try {
			Element parentTitle = doc2.selectFirst(Config.DETAIL_INFO);
			Elements title = parentTitle.select("a");
			res = getInfoTitle(kind, title);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
		return res;
	}

	// 무비의 info를 종류별로 가져오는 메서드
	public String getInfoTitle(String title, Elements wrap) {
		String res = "";
		int timer = 0;
		int count = 0;
		if (title.equals("open=")) {
			for (Element info : wrap) {
				if (count >= 2) {
					return res;
				}
				if (info.attr("href").contains(title)) {
					res += info.text();
					count++;
				}
			}
			return res;
		}

		for (Element info : wrap) {
			if (info.attr("href").contains(title)) {
				res += info.text();
			}
		}
		return res;
	}

	// 무비의 poster를 가져오는 메서드
	public String getMoviePoster(String url, Document doc2) {
		String res = "";
		try {
			Element poster = doc2.selectFirst(Config.DETAIL_POSTER);
			res = poster.attr("src");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	// 무비의 running_time을 가져온다
	public String getMovieRunningTime(String url, Document doc2) {
		String res = "";

		try {
			Element parentTitle = doc2.selectFirst(Config.DETAIL_RUNNING_TIME);
			Elements title = parentTitle.select("a");
			for (Element info : title) {
				if (info.attr("href").contains("nation=")) {
					Element target = info.parent();
					target = target.nextElementSibling();
					if (target == null) {
						return "";
					}
					res = target.text();
					break;
				}
			}
			if (res.indexOf("분") != -1)
				res = res.substring(0, res.indexOf("분"));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
		return res;
	}

	// 무비의 audience_count를 가져온다
	public String getMovieAudienceCount(String url, Document doc2) {
		String res = "";

		try {
			Elements title = doc2.select(Config.DETAIL_AUDIENCE_COUNT);
			res = title.text();
			if (res.indexOf("명") != -1)
				res = res.substring(0, res.indexOf("명"));
			res = res.replaceAll(",", "");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
		return res;
	}

	// 무비의 story를 가져온다
	public String getMovieStory(String url, Document doc2) {
		String res = "";

		try {
			Elements title = doc2.select(Config.DETAIL_STORY_FIRST);
			res = title.text();
			title = doc2.select(Config.DETAIL_STORY_SECOND);
			res += title.text();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(EXCEPTION_MESSAGE);
		}
		return res;
	}
}
