package net.skhu.controller;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.entity.Comment;
import net.skhu.entity.CommentRecommend;
import net.skhu.repository.CommentRecommendRepository;
import net.skhu.repository.CommentRepository;




@RestController
@RequestMapping("")
public class CommentController {

	@Autowired CommentRepository commentRepository;
	@Autowired CommentRecommendRepository commentRecommendRepository;


	@ResponseBody
	@RequestMapping("commentsave")
	public List<Comment> CommentSave(@RequestBody Comment comment) { //댓글 등록
		commentRepository.save(comment);
		List<Comment> list = commentRepository.findAllByOrderByDateDesc();
		return list;
	}

	@ResponseBody
	@RequestMapping("commentdelete")
	public List<Comment> CommentDelete(@RequestBody Comment comment) { //댓글 삭제
		commentRepository.deleteById(comment.getId());
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

	@ResponseBody
	@RequestMapping("commentrecommend")
	public Boolean CommentRecommend(@RequestBody Comment comment, HttpServletRequest request) { //댓글 찾기
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return false;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		if(commentRecommendRepository.findByUserEmailAndCommentId(userEmail, comment.getId()) != null) {
			commentRecommendRepository.delete(commentRecommendRepository.findByUserEmailAndCommentId(userEmail, comment.getId()));
			comment.setRecommend(comment.getRecommend()-1);
			commentRepository.save(comment);
			return false;
		}
		CommentRecommend recommend = new CommentRecommend();
		recommend.setUserEmail(userEmail);
		recommend.setCommentId(comment.getId());
		commentRecommendRepository.save(recommend);
		comment.setRecommend(comment.getRecommend()+1);
		commentRepository.save(comment);
		return true;
	}



}