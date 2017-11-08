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

import com.niit.dao.JobApplicationDao;
import com.niit.dao.JobDao;
import com.niit.models.BlogComment;
import com.niit.models.Job;
import com.niit.models.JobApplication;
import com.niit.models.User;


@RestController
public class JobRestService {

	private static Logger log = LoggerFactory.getLogger(JobRestService.class);
	
	@Autowired
	private Job job;
	
	@Autowired
	JobDao jobDao;
	
	@Autowired
	JobApplication  jobApplication;
	
	@Autowired
	JobApplicationDao  jobApplicationDao;
	
	@GetMapping("/viewAllJobs")
	public ResponseEntity<List<Job>> getAllJobs()
	{
		List<Job> jobList=jobDao.getAllJob();

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<Job>>(jobList,HttpStatus.OK);
	}
	
	@PostMapping("/createjob")
	public Job createJob(@RequestBody Job newJob)
	{
		log.debug("Calling createJob method ");
		//before creating user, check whether the id exist in the db or not
		
		job = jobDao.getJobById(newJob.getId());
		if( job ==null)
		{
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			jobDao.saveJob(newJob);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newJob.setErrorCode("200");
			newJob.setErrorMessage("Thank you For register in Blog.");
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id alredy exist in db.
			newJob.setErrorCode("800");
			newJob.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Endig of the  createJob method ");
		return newJob;		
}	
	
	@PostMapping("/applyjob")
	public JobApplication createJobApplication(@RequestBody JobApplication newJobApplication)
	{
		log.debug("Calling createjobapplication method ");
		//before creating user, check whether the id exist in the db or not
		
		jobApplication = jobApplicationDao.get(newJobApplication.getId());
		if( jobApplication ==null)
		{
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			jobApplicationDao.save(newJobApplication);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newJobApplication.setErrorCode("200");
			newJobApplication.setErrorMessage("Thank you fo registration.");	
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id already exist in db.
			newJobApplication.setErrorCode("800");
			newJobApplication.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Endig of the  createJobApplication method ");
		return newJobApplication;		
}

	@GetMapping("/appliedjobs/{id}")
	public ResponseEntity<List<JobApplication>> getAlljobapplication(@PathVariable("id") String userid)
	{
		List<JobApplication> jobApplicationList=jobApplicationDao.getAlljobapplication(userid);

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<JobApplication>>(jobApplicationList,HttpStatus.OK);
	}
	
	@GetMapping("/JobApplication")
	public ResponseEntity<List<JobApplication>> list()
	{
		System.out.println(" method reached to job REST controller ");
		List<JobApplication> jobApplicationList=jobApplicationDao.list();

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<JobApplication>>(jobApplicationList,HttpStatus.OK);
	}
	
	@GetMapping("/Job/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable("id") String id)
	{
		log.debug("Starting of the method getJobById");
		log.info("Trying to get Jobdetails of the id " + id);
		System.out.println(id);
		Job j = jobDao.getJobById(id);
		
		if(j==null)
		{
			System.out.println("not found");
			j = new Job();
			j.setErrorCode("404");
			j.setErrorMessage("Job does not exist with the id :" + id);
		}
		else
		{
			j.setErrorCode("200");
			j.setErrorMessage("success");
		}
		
		
		log.debug("**************Ending of the method getJobById");
	  return	new ResponseEntity<Job>(j , HttpStatus.OK);
	}
	
	@PostMapping("/changeStatus")
	public JobApplication changeStatus(@RequestBody JobApplication newJobApplication)
	{
		log.debug("Calling changeStatus method ");
		//before creating user, check whether the id exist in the db or not
		
		jobApplication = jobApplicationDao.get(newJobApplication.getId());
		if( jobApplication !=null)
		{
			log.debug(" in if method");
			//id does not exist in the db
			jobApplicationDao.updateJobApplication(newJobApplication);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newJobApplication.setErrorCode("200");
			newJobApplication.setErrorMessage("Thank you fo registration.");	
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id already exist in db.
			newJobApplication.setErrorCode("800");
			newJobApplication.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Ending of the changeStatus method ");
		return newJobApplication;		
}

}



