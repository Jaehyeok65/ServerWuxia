package net.skhu.dto;


import lombok.Data;


@Data
public class WuxiaCommentDto {

	int userId;
	int wuxiaId;
	String Content;
	String createdAt;

	public WuxiaCommentDto(int userId, int wuxiaId, String Content, String createdAt) {
		this.userId = userId;
		this.wuxiaId = wuxiaId;
		this.Content = Content;
		this.createdAt = createdAt;
	}

}
