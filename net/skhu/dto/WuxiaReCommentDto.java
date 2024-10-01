package net.skhu.dto;


import lombok.Data;


@Data
public class WuxiaReCommentDto {

	int userId;
	int commentId;
	String Content;
	String createdAt;

	public WuxiaReCommentDto(int userId, int commentId, String Content, String createdAt) {
		this.userId = userId;
		this.commentId = commentId;
		this.Content = Content;
		this.createdAt = createdAt;
	}

}
