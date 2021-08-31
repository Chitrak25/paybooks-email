package com.axis.paybooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.axis.paybooks.exception.AwsSesException;




@Service
public class AwsSesService {
	private String accessKey = "AKIA3FZBHSNA26AA75W3";
	private String secretKey = "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";

    private static final String CHAR_SET = "UTF-8";
    private String subject; 
    private final AmazonSimpleEmailService emailService;
    private String sender;
    
    
    
    


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Autowired
    public AwsSesService(AmazonSimpleEmailService emailService,
                         @Value("${email.sender}") String sender) {
        this.emailService = emailService;
        this.sender = sender;
    }

    /**
     * This method send email using the aws ses sdk
     *
     * @param email email
     * @param body  body
     */
    public void sendEmail(String email, String body) {
    	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        try {

            // The time for request/response round trip to aws in milliseconds
            int requestTimeout = 3000;
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(email))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset(CHAR_SET).withData(body)))
                            .withSubject(new Content()
                                    .withCharset(CHAR_SET).withData(subject)))
                    .withSource(sender).withSdkRequestTimeout(requestTimeout);
            emailService.sendEmail(request);
        } catch (RuntimeException e) {
           // log.error("Error occurred sending email to {} ", email, e);
            throw new AwsSesException("Failed to send email ", e);
        }
    }


}