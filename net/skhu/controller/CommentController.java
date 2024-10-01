package net.skhu.controller;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.dto.WuxiaCommentDto;
import net.skhu.dto.WuxiaReCommentDto;
import net.skhu.entity.Comment;
import net.skhu.entity.CommentRecommend;
import net.skhu.entity.CommentRecommendation;
import net.skhu.entity.ReplyComment;
import net.skhu.entity.User;
import net.skhu.entity.Wuxia;
import net.skhu.entity.WuxiaComment;
import net.skhu.repository.CommentRecommendRepository;
import net.skhu.repository.CommentRecommendationRepository;
import net.skhu.repository.CommentRepository;
import net.skhu.repository.ReplyCommentRepository;
import net.skhu.repository.UserRepository;
import net.skhu.repository.WuxiaCommentRepository;
import net.skhu.repository.WuxiaRepository;





@RestController
@RequestMapping("")
public class CommentController {

	@Autowired CommentRepository commentRepository;
	@Autowired CommentRecommendRepository commentRecommendRepository;
	@Autowired WuxiaCommentRepository wuxiacommentRepository;
	@Autowired WuxiaRepository wuxiaRepository;
	@Autowired UserRepository userRepository;
	@Autowired CommentRecommendationRepository commentRecommendationRepository;
	@Autowired ReplyCommentRepository replyCommentRepository;


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
		//System.out.println(list.size() + " 삭제");
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
		if(title.getTitle().equals("추천순")) {
			list = commentRepository.findAllByOrderByRecommendDesc();
		}
		else {
			list = commentRepository.findAllByOrderByDateDesc();
		}
		//System.out.println(list.size() + " 리스트");
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiacommentsave")
	public List<WuxiaComment> WuxiaCommentSave(@RequestBody WuxiaCommentDto comment) { //댓글 등록
		WuxiaComment wuxiacomment = new WuxiaComment();
		Optional<User> userOptional = userRepository.findById(comment.getUserId());
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			wuxiacomment.setUser(user);
		}
		Optional<Wuxia> wuxiaOptional = wuxiaRepository.findById(comment.getWuxiaId());
		if(wuxiaOptional.isPresent()) {
			Wuxia wuxia = wuxiaOptional.get();
			wuxiacomment.setWuxia(wuxia);
		}
		wuxiacomment.setCreatedAt(comment.getCreatedAt());
		wuxiacomment.setContentText(comment.getContent());
		wuxiacommentRepository.save(wuxiacomment);
		List<WuxiaComment> list =wuxiacommentRepository.findByWuxiaIdOrderByCreatedAtDesc(comment.getWuxiaId());
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiarecommentsave")
	public List<ReplyComment> WuxiaReCommentSave(@RequestBody WuxiaReCommentDto comment) { //댓글 등록
		ReplyComment recomment = new ReplyComment();
		Optional<User> userOptional = userRepository.findById(comment.getUserId());
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			recomment.setUser(user);
		}
		Optional<WuxiaComment> wuxiaCommentOptional = wuxiacommentRepository.findById(comment.getCommentId());
		if(wuxiaCommentOptional.isPresent()) {
			WuxiaComment wuxiacomment = wuxiaCommentOptional.get();
			recomment.setWuxiaComment(wuxiacomment);
		}
		recomment.setCreatedAt(comment.getCreatedAt());
		recomment.setContent(comment.getContent());
		replyCommentRepository.save(recomment);
		List<ReplyComment> list = replyCommentRepository.findByWuxiaComment_WuxiaCommentIdOrderByCreatedAtDesc(recomment.getWuxiaComment().getWuxiaCommentId());;
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiarecommentlist")
	public List<ReplyComment> WuxiaReCommentList(@RequestBody WuxiaComment wuxia) { //댓글 찾기
		List<ReplyComment> list = replyCommentRepository.findByWuxiaComment_WuxiaCommentIdOrderByCreatedAtDesc(wuxia.getWuxiaCommentId());
		return list;
	}


	@ResponseBody
	@RequestMapping("wuxiacommentlist")
	public List<WuxiaComment> WuxiaCommentList(@RequestBody Wuxia wuxia) { //댓글 찾기
		Wuxia newwuxia = wuxiaRepository.findByTitle(wuxia.getTitle());
		List<WuxiaComment> list = wuxiacommentRepository.findByWuxiaIdOrderByCreatedAtDesc(newwuxia.getId());
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiacommentdelete")
	public List<WuxiaComment> DeleteWuxiaComment(@RequestBody WuxiaComment comment) { //댓글 번호를 받아야함
		WuxiaComment comments = new WuxiaComment();
		Optional<WuxiaComment> wuxiacomment = wuxiacommentRepository.findById(comment.getWuxiaCommentId());
		wuxiacommentRepository.deleteById(comment.getWuxiaCommentId());
		if(wuxiacomment.isPresent()) {
			WuxiaComment newwuxia = wuxiacomment.get();
			comments.setWuxia(newwuxia.getWuxia());
		}
		List<WuxiaComment> list = wuxiacommentRepository.findByWuxiaIdOrderByCreatedAtDesc(comments.getWuxia().getId());
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiarecommentdelete")
	public List<ReplyComment> DeleteWuxiaReComment(@RequestBody ReplyComment comment) { //댓글 번호를 받아야함
		Optional<ReplyComment> replycomment = replyCommentRepository.findById(comment.getReplyId());
		replyCommentRepository.deleteById(comment.getReplyId());
		if(replycomment.isPresent()) {
			ReplyComment newreply = replycomment.get();
			comment.setWuxiaComment(newreply.getWuxiaComment());
		}
		List<ReplyComment> list = replyCommentRepository.findByWuxiaComment_WuxiaCommentIdOrderByCreatedAtDesc(comment.getWuxiaComment().getWuxiaCommentId());
		return list;
	}

	@ResponseBody
	@RequestMapping("wuxiacommentrecommend")
	public List<WuxiaComment> RecommendWuxiaComment(@RequestBody WuxiaComment comment, HttpServletRequest request) { //댓글 번호를 받아야함
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return null;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		User user = userRepository.findByUserEmail(userEmail);
		Optional<WuxiaComment> newcomment = wuxiacommentRepository.findById(comment.getWuxiaCommentId());
		WuxiaComment comments = newcomment.get();
		CommentRecommendation recommend = commentRecommendationRepository.findByUserIdAndWuxiaCommentWuxiaCommentId(user.getId(), comment.getWuxiaCommentId());
		if(recommend == null) { //추천 기록이 없다면 추천을 등록함
			CommentRecommendation newrecommend = new CommentRecommendation();
			newrecommend.setUser(user);
			newrecommend.setWuxiaComment(comment);
			commentRecommendationRepository.save(newrecommend);
			comments.setRecommendationcount(comments.getRecommendationcount() + 1);
			wuxiacommentRepository.save(comments);
		}
		else { //추천 기록이 있다면 추천을 취소함
			commentRecommendationRepository.deleteById(recommend.getRecommendationId());
			comments.setRecommendationcount(comments.getRecommendationcount() - 1);
			wuxiacommentRepository.save(comments);
		}
		List<WuxiaComment> list = wuxiacommentRepository.findByWuxiaIdOrderByCreatedAtDesc(comments.getWuxia().getId());
		return list;
		//return null;
	}

	@ResponseBody
	@RequestMapping("wuxiarecommentrecommend")
	public List<ReplyComment> RecommendWuxiaReComment(@RequestBody ReplyComment comment, HttpServletRequest request) { //댓글 번호를 받아야함
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return null;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		System.out.println(comment);
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		User user = userRepository.findByUserEmail(userEmail);
		Optional<ReplyComment> newcomment = replyCommentRepository.findById(comment.getReplyId());
		ReplyComment comments = newcomment.get();
		System.out.println(comments);
		CommentRecommendation recommend = commentRecommendationRepository.findByUserIdAndReplyCommentReplyId(user.getId(), comment.getReplyId());
		if(recommend == null) { //추천 기록이 없다면 추천을 등록함
			CommentRecommendation newrecommend = new CommentRecommendation();
			newrecommend.setUser(user);
			newrecommend.setReplyComment(comment);
			commentRecommendationRepository.save(newrecommend);
			comments.setRecommendationcount(comments.getRecommendationcount() + 1);
			replyCommentRepository.save(comments);
		}
		else { //추천 기록이 있다면 추천을 취소함
			commentRecommendationRepository.deleteById(recommend.getRecommendationId());
			comments.setRecommendationcount(comments.getRecommendationcount() - 1);
			replyCommentRepository.save(comments);
		}
		List<ReplyComment> list = replyCommentRepository.findByWuxiaComment_WuxiaCommentIdOrderByCreatedAtDesc(comments.getWuxiaComment().getWuxiaCommentId());
		return list;
	}



	@ResponseBody
	@RequestMapping("commentrecommend")
	public Comment CommentRecommend(@RequestBody Comment comment, HttpServletRequest request) { //댓글 찾기
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return null;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		CommentRecommend prevrecommend = commentRecommendRepository.findByUserEmailAndCommentId(userEmail, comment.getId());
		Optional<Comment> optionalComment = commentRepository.findById(comment.getId());
		if (optionalComment.isPresent()) {
		    Comment data = optionalComment.get();
		    if(prevrecommend != null) { //null이 아니라면 이미 추천한 것
		    	commentRecommendRepository.delete(prevrecommend); //재추천을 대비하여 삭제
		    	data.setRecommend(data.getRecommend()-1);
				commentRepository.save(data);
				return data;
			}
			// null이라면 추천하지 않은 것
			CommentRecommend recommend = new CommentRecommend();
			recommend.setUserEmail(userEmail);
			recommend.setCommentId(comment.getId());
			commentRecommendRepository.save(recommend);
			data.setRecommend(data.getRecommend()+1);
			commentRepository.save(data);
			return data;
		}
		else {
			return null;
		}
	}




}