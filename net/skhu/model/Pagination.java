package net.skhu.model;

import lombok.Data;



@Data
public class Pagination {
	int pg = 1;
	int sz = 8;
	int recordCount; //전체 데이터 수


}
