package com.niit.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.dao.UserDao;
import com.niit.models.User;



@RestController
public class UserRestService {
	
	private static Logger log = LoggerFactory.getLogger(UserRestService.class);
	
	@Autowired
	private User user;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	HttpSession session;

	@GetMapping("/viewAllUser")
	public ResponseEntity<List<User>> getAllUser()
	{
		log.debug(" All User ");
		List<User> userList =  userDao.getAllUser();	
		//ResponseEntity:  we can send the data + HTTP status codes + error message
		// like 200 - success
		// 404 - page not found
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}
	
	@PostMapping("/saveUser")
	public User saveUser(@RequestBody User newuser)
	{
		log.debug("Calling saveUser method ");
		
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			
			userDao.saveUser(newuser);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newuser.setErrorCode("200");
			newuser.setErrorMessage("Thank you For register in user.");
		
		log.debug("Endig of the  createuser method ");
		return newuser;		
}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserByID(@PathVariable("id") String id)
	{
		log.debug("Starting of the method getUserByID");
		log.info("Trying to get userdetails of the id " + id);
		System.out.println(id);
		User u = userDao.getUserById(id);
		//System.out.println("Hi "+u.getName());
		
		if(u==null)
		{
			System.out.println("not found");
			u = new User();
			u.setErrorCode("404");
			u.setErrorMessage("User does not exist with the id :" + id);
		}
		else
		{
			u.setErrorCode("200");
			u.setErrorMessage("success");
		}
		
		log.info("**************** Name of the user is " + u.getName());
		log.debug("**************Ending of the method getUserByID");
	  return	new ResponseEntity<User>(u , HttpStatus.OK);
	}

	@GetMapping("/userByName/{nm}")
	public ResponseEntity<User> getUserByNam(@PathVariable("nm") String nm)
	{
		
		User u = userDao.getUserByName(nm);
		//System.out.println("Hi "+u.getName());
		
		if(u==null)
		{
			System.out.println("not found");
			u = new User();
			u.setErrorCode("404");
			u.setErrorMessage("User does not exist with the name: "+nm);
		}
		else
		{
			u.setErrorCode("200");
			u.setErrorMessage("success");
		}
		
		log.info("**************** Name of the user is " + u.getName());
		log.debug("**************Ending of the method getUserByID");
	  return	new ResponseEntity<User>(u , HttpStatus.OK);
	}

	
	@PostMapping("/validate")
	public ResponseEntity<User> validateCredentials(@RequestBody User user)
	{
		log.debug("->->->->calling method authenticate"+user.getEmail()+user.getPassword());
		user = userDao.isValidate(user.getEmail(), user.getPassword());
		log.debug("user"+user);
		
		if (user == null) {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
			log.debug("->->->->InValid Credentials");

		 }
		 else
		 {
			 
					user.setErrorCode("200");
					user.setErrorMessage("You have successfully logged in.");
					user.setIsOnline('Y');
					log.debug("->->->->Valid Credentials");
				
					session.setAttribute("loggedInUserID", user.getId());
					session.setAttribute("loggedInUserRole", user.getUserrole());
			
					log.debug("You are loggin with the role : " +session.getAttribute("loggedInUserRole"));

				userDao.setOnline(user.getId());
		 }
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping("/uploadFile/{id}")
	public String storeFile(@PathVariable("id") String id, @RequestParam("file") MultipartFile[] files,
			   HttpServletRequest request) {
			  System.out.println(files[0]);
			  // storing file on server
			  System.out.println(request.getRealPath("/").substring(0, 69));
			  String path = request.getRealPath("/").substring(0,69) + "CollaborationFrontEnd-master\\resources\\images\\" + files[0].getOriginalFilename();
			  System.out.println(path);
			  MultipartFile file = files[0];

			  if (!file.isEmpty()) {

			   try {
			    byte[] bytes = file.getBytes();
			    System.out.println(file.getOriginalFilename());

			    File serverFile = new File(path);
			    serverFile.createNewFile();

			    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			    stream.write(bytes);
			    stream.close();
			    userDao.updateProfilepic(files[0].getOriginalFilename(),id);
			   } catch (Exception ex) {
			    System.out.println(ex);
			   }

			  }
			  System.out.println(id);
			  return "upload";
			 }
	
	@GetMapping("/logout")
	public ResponseEntity<User> logout(HttpSession session)
	{
	log.debug("->->->->calling method logout");
	String loggedInUserID = (String) session.getAttribute("loggedInUserID");
	
	 user = userDao.get(loggedInUserID);
	 user.setIsOnline('N');
	 userDao.updateUser(user);

	session.invalidate();

	user.setErrorCode("200");
	user.setErrorMessage("You have successfully logged in");
	return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping("/searchpeople")
	public ResponseEntity<List<User>> searchUsers(@RequestBody String name)
	{
		
		List<User> userList =  userDao.searchlist(name);
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
}
}
