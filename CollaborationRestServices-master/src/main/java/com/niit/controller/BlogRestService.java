package com.niit.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.BlogCommentDao;
import com.niit.dao.BlogDao;
import com.niit.models.Blog;
import com.niit.models.BlogComment;



@RestController
public class BlogRestService {
	
private static Logger log = LoggerFactory.getLogger(BlogRestService.class);
	
	@Autowired
	private Blog blog;
	
	@Autowired
	BlogDao blogDao;
	
	@Autowired
	BlogCommentDao blogcommentDao;
	
	@Autowired
	BlogComment blogcomment;
	
	@GetMapping("/viewAllBlogs")
	public ResponseEntity<List<Blog>> getAllBlogs()
	{
		List<Blog> blogList=blogDao.getAllBlog();

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<Blog>>(blogList,HttpStatus.OK);
	}
	
	@PostMapping("/createblog")
	public Blog createBlog(@RequestBody Blog newBlog)
	{
		log.debug("Calling createBlog method ");
		//before creating user, check whether the id exist in the db or not
		
		blog = blogDao.get(newBlog.getId());
		if( blog ==null)
		{
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			newBlog.setDate_added(new Date());// automatically takes the curent todays date
			blogDao.saveBlog(newBlog);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newBlog.setErrorCode("200");
			newBlog.setErrorMessage("Thank you For register in Blog.");
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id alredy exist in db.
			newBlog.setErrorCode("800");
			newBlog.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Endig of the  createBlog method ");
		return newBlog;		
}
	
	@GetMapping("/showBlog/{id}")
	public ResponseEntity<Blog>get(@PathVariable("id") String id){

		blog = blogDao.get(id);
		
	  return	new ResponseEntity<Blog>(blog , HttpStatus.OK);
}
	
	// comment section
	
	@GetMapping("/viewAllComments/{blogid}")
	public ResponseEntity<List<BlogComment>> getAllBlogComment(@PathVariable("blogid") String blogid)
	{
		List<BlogComment> blogcommentList=blogcommentDao.getAllBlogComment(blogid);

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<BlogComment>>(blogcommentList,HttpStatus.OK);
	}
	
	
	@PostMapping("/createblogcomment")
	public BlogComment createBlogComment(@RequestBody BlogComment newBlogComment)
	{
		try{
			newBlogComment.setDate_added(new Date());// automatically takes the current todays date
			blogcommentDao.saveBlogComment(newBlogComment);
			newBlogComment.setErrorCode("200");
			newBlogComment.setErrorMessage("Blog Comment Added Sucessfully");
		}
		catch (Exception e) {
			
		
			newBlogComment.setErrorCode("800");
			newBlogComment.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Ending of the  createBlogComment method ");
		return newBlogComment;		
	}
	
	@DeleteMapping("/deletecomment/{id}")

	public ResponseEntity<BlogComment> deleteComment(@PathVariable("id") String id){

		BlogComment b = blogcommentDao.getBlogCommentById(id);
		boolean del=blogcommentDao.deleteBlogComment(b);
		if(del){
			blogcomment.setErrorCode("200");
			blogcomment.setErrorMessage("Deleted Successfully");
		}
		else{
			blogcomment.setErrorCode("500");
			blogcomment.setErrorMessage("Error in deleting");
		}
		
	  return	new ResponseEntity<BlogComment>(blogcomment , HttpStatus.OK);
}
	
}
	


