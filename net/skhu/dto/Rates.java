package net.skhu.dto;


import lombok.Data;
import net.skhu.entity.Wuxia;


@Data
public class Rates {

	Wuxia wuxia;
	boolean flag;

	public Rates() {

	}

	public Rates(Wuxia wuxia, boolean flag) {
		this.wuxia = wuxia;
		this.flag = flag;
	}

}
