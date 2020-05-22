package com.sist.movieVO;

import java.util.Date;

import lombok.Data;

@Data
public class NaverMovieVO {
	private int movie_id;
	private String title;
	private String grade;
	private Date opening_date;
	private String genre;
	private String country;
	private int running_time;
	private int hit;
	private int audience_count;
	private String story;
	private String poster;
	
}
