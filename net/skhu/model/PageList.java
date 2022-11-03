package net.skhu.model;


import java.util.List;

import lombok.Data;



@Data
public class PageList<Wuxia> {

	Boolean end;
	List<Wuxia> list;
}
