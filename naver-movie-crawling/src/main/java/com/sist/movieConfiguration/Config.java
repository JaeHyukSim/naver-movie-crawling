package com.sist.movieConfiguration;

import java.util.Date;

public class Config {
	public final static int UNIT_YEAR = 10;
	public final static String FINAL_YEAR = "20200515";
	public final static String START_URL_TEMPLATE = "https://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=#{date}&page=#{page}";
	public final static String FIRST_URL_QUERY_SELECTOR_TO_SEARCH_DETAIL_PAGE = "#cbody #old_content .list_ranking tbody .title .tit5 a";
	public final static String FIRST_URL_QUERY_SELECTOR_TO_SEARCH_RANK = "#cbody #old_content .list_ranking tbody .ac img";
	public final static String FIRST_URL_QUERY_SELECTOR_TO_SEARCH_RANK2 = "#cbody #old_content .list_ranking tbody .order";
	
	public final static String DETAIL_URL_TEMPLATE = "https://movie.naver.com/movie/bi/mi/basic.nhn?code=#{movie_id}";
	public final static String DETAIL_TITLE = "#content .article .mv_info .h_movie > a";
	public final static String DETAIL_POSTER = "#content .article .mv_info_area .poster img";
	public final static String DETAIL_INFO = "#content .article .mv_info .info_spec";
	public final static String DETAIL_RUNNING_TIME = "#content .article .mv_info .info_spec";
	public final static String DETAIL_AUDIENCE_COUNT ="#content .article .mv_info .info_spec .count";
	public final static String DETAIL_STORY_FIRST = ".obj_section .video .story_area .h_tx_story";
	public final static String DETAIL_STORY_SECOND = ".obj_section .video .story_area p.con_tx";
	
	public final static String PICTURE_URL_TEMPLATE = "https://movie.naver.com/movie/bi/mi/photo.nhn?code=#{code}&page=#{page}#movieEndTabMenu";
	public final static String PICTURE_COUNT = ".obj_section2 .photo .title_area .count";
	public final static String PICTURE_URL_CONTENT = ".photo .gallery_group li a img";
	
	public final static String YOUTUBE_EMBED_URL_TEMPLATE = "https://www.youtube.com/embed/#{key}";
	public final static String YOUTUBE_QUERY_SELECTOR_IMG = ".style-scope #contents .ytd-item-section-renderer #dismissable img";
	
}
