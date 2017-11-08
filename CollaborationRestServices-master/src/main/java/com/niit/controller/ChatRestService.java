package com.niit.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.models.Message;
import com.niit.models.OutputMessage;



@RestController
@RequestMapping("/chat")
public class ChatRestService {

	
		
		  private Logger logger = LoggerFactory.getLogger(getClass());

		  @MessageMapping("/chat")
		  @SendTo("/topic/message")
		  public OutputMessage sendMessage(Message message) 
		  {
			  System.out.println("sending msggg"+message.getMessage());
			  
		    logger.info("Message sent");
		   return new OutputMessage(message, new Date());
		 	}
}
