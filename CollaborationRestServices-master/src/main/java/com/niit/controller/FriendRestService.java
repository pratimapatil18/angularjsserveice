package com.niit.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.FriendDao;
import com.niit.models.Friend;

@RestController
public class FriendRestService {
	
	
	@Autowired
	FriendDao friendDao;
	
	@Autowired 
	Friend friend;
	
	private static Logger log = LoggerFactory.getLogger(FriendRestService.class);
	
	
	@GetMapping("/allfriends/{id}")
	public ResponseEntity<List<Friend>> getAllFriends(@PathVariable("id") String userid)
	{
		log.debug("inside getAllFriends");
		List<Friend> friendList =  friendDao.getAllFriends(userid);	
		//ResponseEntity:  we can send the data + HTTP status codes + error message
		// like 200 - success
		// 404 - page not found
		return new ResponseEntity<List<Friend>>(friendList, HttpStatus.OK);
	}
	
	
	@PostMapping("/addfriend")
	public Friend createFriend(@RequestBody Friend newFriend)
	{
			
			
			boolean saved=friendDao.save(newFriend);
			if(saved){
			newFriend.setErrorCode("200");
			newFriend.setErrorMessage("Thank you For register in Friend.");
			}
			else
			{
				newFriend.setErrorCode("800");
				newFriend.setErrorMessage("some error occured.");
			}
		return newFriend;		
}
	
	
	@GetMapping("/sentrequests/{id}")
	public ResponseEntity<List<Friend>> getSentRequests(@PathVariable("id") String id)
	{
		log.debug("inside getAllFriends");
		List<Friend> friendList =  friendDao.getSentRequests(id);	
		//ResponseEntity:  we can send the data + HTTP status codes + error message
		// like 200 - success
		// 404 - page not found
		return new ResponseEntity<List<Friend>>(friendList, HttpStatus.OK);
	}
	
	@PostMapping("/sendreq/{id}/{userId}")
	public Friend sendreq(@PathVariable("id") String id,@PathVariable("userId") String userId)
	{
		log.debug("Calling sendreq method ");
		//before creating job application, check whether the id exist in the db or not
		
		friend = friendDao.getFriendById(id);
		if(friend==null)
		{
			log.debug("Req Id does not exist...trying to create new Req Id");
			//id does not exist in the db
			Friend friend=new Friend();
			friend.setFriendid(id);
			friend.setUserid(userId);
			friend.setIsOnline('Y');
			friend.setLastSeenTime(new Date());
			friend.setStatus("N");
			friendDao.save(friend);
			friend.setErrorCode("200");
			friend.setErrorMessage("Thank you for sending friend request.");
		}
		else
		{
			log.debug("Please choose another id as it exists");
			//id already exists in db.
			friend.setErrorCode("800");
			friend.setErrorMessage("Please choose another id as it exists");
			
		}
		log.debug("Ending of the sendreq method ");
		return friend;
		
	}
	
	@PostMapping("/acceptreq/{id}/{userId}")
	public Friend acceptreq(@PathVariable("id") String id,@PathVariable("userId") String userId)
	{
		log.debug("Calling acceptreq method ");
		//before creating job application, check whether the id exist in the db or not
		
		friend = friendDao.get(id,userId);
		if(friend!=null)
		{
			log.debug("Found request id");
			//id exists in the db
			friend.setLastSeenTime(new Date());
			friend.setStatus("Y");
			friendDao.update(friend);
			friend.setErrorCode("200");
			friend.setErrorMessage("Thank you for sending friend request.");
		}
		else
		{
			log.debug("Request Id not found");
			//id does not exist
			Friend friend=new Friend();
			friend.setErrorCode("800");
			friend.setErrorMessage("Request Id not found");
			
		}
		log.debug("Ending of the acceptreq method ");
		return friend;	
	}
	
	@GetMapping("/requests/{id}")
	public ResponseEntity<List<Friend>> getAllRequests(@PathVariable("id") String id)
	{
		log.debug("inside getAllFriends");
		List<Friend> friendList =  friendDao.getAllRequests(id);	
		//ResponseEntity:  we can send the data + HTTP status codes + error message
		// like 200 - success
		// 404 - page not found
		return new ResponseEntity<List<Friend>>(friendList, HttpStatus.OK);
	}

}
