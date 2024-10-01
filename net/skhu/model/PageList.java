package net.skhu.model;


import java.util.List;

import lombok.Data;



@Data
public class PageList<Wuxia> {

	int total;
	List<Wuxia> list;

	public PageList(int total, List<Wuxia> list) {
		this.total = total;
		this.list = list;
	}
}
