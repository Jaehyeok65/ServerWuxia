package net.skhu.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.entity.Comment;
import net.skhu.repository.CommentRepository;




@RestController
@RequestMapping("")
public class CommentController {

	@Autowired CommentRepository commentRepository;


	@ResponseBody
	@RequestMapping("commentsave")
	public List<Comment> CommentSave(@RequestBody Comment comment) { //댓글 등록
		commentRepository.save(comment);
		List<Comment> list = commentRepository.findAllByOrderByDateDesc();
		return list;
	}

	@ResponseBody
	@RequestMapping("comment")
	public Optional<net.skhu.entity.Comment> Comment(@RequestBody Comment comment) { //댓글 찾기
		Optional<net.skhu.entity.Comment> data = commentRepository.findById(comment.getId());
		return data;
	}

	@ResponseBody
	@RequestMapping("commentlist")
	public List<Comment> CommentList(@RequestBody Comment title) { //댓글 등록
		List<Comment> list;
		if(title.getTitle().equals("조회순")) {
			list = commentRepository.findAllByOrderByViewDesc();
		}
		else if(title.getTitle().equals("추천순")) {
			list = commentRepository.findAllByOrderByRecommendDesc();
		}
		else {
			list = commentRepository.findAllByOrderByDateDesc();
		}
		return list;
	}



}