package net.skhu.model;

import lombok.Data;



@Data
public class Pagination {
	int pg = 1;
	int sz = 8;
	int recordCount; //전체 데이터 수
	public Pagination(int pg2, int sz2) {
		this.pg = pg2;
		this.sz = sz2;
		// TODO Auto-generated constructor stub
	}



}
