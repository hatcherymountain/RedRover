package com.askredrover.communication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.eos.Eos;
import com.askredrover.RedRover;
import com.eos.routing.Action;
import com.eos.routing.Controller;
import com.eos.routing.Externals;
import com.eos.routing.JSONReader;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class Emails {

	public static final int TEMPLATE_CHANGEPASSWORD = -2;
	public static final int TEMPLATE_NEWUSER = -1;

	private Eos eos = null;
	private RedRover rr = null;
	

	public Emails(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr=rr;
	}

	/**
	 * Find Template and read content.
	 * 
	 * @param templateid
	 * @return
	 */
	private String readTemplate(String emailtemplate) {

		String templateBody = "";

		/** Where are the files located? **/
		String loc = eos.properties().getProperty("emailTemplateLocation").value();

		if (loc == null) {
			loc = "/emails";
		} // default old location.

		if (emailtemplate != null) {

			/** The template file **/
			if (!emailtemplate.contains(".html")) {
				emailtemplate = emailtemplate + ".html";
			}

			String f = eos.getFilePath() + "/" + loc + "/" + emailtemplate + "";

			try {

				templateBody = com.eos.utils.Files.readFileContents(f);

			} catch (Exception e) {
				eos.getLog().log("Errors reading template file " + f + " -" + e.toString(), 1);
				templateBody = "";
			}
		} else {
			System.err.println("Errors reading email template. File:" + emailtemplate + "");
			eos.log("Errors reading template with ID:" + emailtemplate + "", "Emails", "readTemplateContents", 2);
		}

		return templateBody;
	}

	/**
	 * Will replace all instances of the FIELD with the VALUE in the CONTENT
	 * 
	 * @param content
	 * @param field
	 * @param value
	 * @return String
	 */
	private String replaceContent(String content, String field, String value) {
		return content.replace(field, value);
	}

	

/**
 * Sends meeting details to a user
 * @param to
 * @param subject
 * @param minutes
 * @param agenda
 * @param actionitems
 * @return
 */
	public boolean sendMeeting(String ventureid, String subject,String minutes, String agenda, String actionitems) { 

		boolean sent = false;

		try {
			
			
			String templateContent = readTemplate("meeting.html");
			
			if (templateContent == null) {
				templateContent = "";
			}
			
			
			if (templateContent.length() > 0) {

				/**
				 * Replace the key elements [if they exist]. In this method we have to, subject
				 **/
			
				/** Send to the team members ... **/
				ArrayList<com.askredrover.ventures.Member> members = rr.ventures().members(ventureid); int msize = members.size();
				
				String url = eos.url();
				String logo = eos.logo();

				for (int i = 0; i < msize; i++) {
					com.askredrover.ventures.Member m = (com.askredrover.ventures.Member)members.get(i);
					com.eos.accounts.User user = eos.getUsers().getUser(eos.e(m.userid()));
					if(user!=null && user.getStatus()==1) { 
					templateContent = replaceContent(templateContent, "[EMAIL]",user.getEmail());
					templateContent = replaceContent(templateContent, "[SUBJECT]", subject);
					templateContent = replaceContent(templateContent, "[URL]", url);
					templateContent = replaceContent(templateContent, "[LOGO]", logo);
					templateContent = replaceContent(templateContent, "[MINUTES]", minutes);
					templateContent = replaceContent(templateContent, "[AGENDA]", agenda);
					templateContent = replaceContent(templateContent, "[ITEMS]", actionitems);

					sent = sendEmail(user.getEmail(), subject, templateContent);

					if (sent) {
						eos.audit().audit("Sent meeting minutes email to " + user.getEmail(), "sendemail", "EmailManager",
								"sendEmailWithTemplate");
					} else {
						throw new Exception("Could not send email to " + user.getEmail() + ". Check logs");
					}
				}
			}
		}
		} catch (Exception e) {
			System.err.println("Email failure: " + e.toString());
			eos.getLog().log("Errors sending email to batch group in Redrover EMAILS.sendMeeting. Err:" + e.toString(), "Emails",
					"sendMeeting", 0);
		}
		return sent;
	}

	

	/**
	 * Send email using SendGrid.
	 * 
	 * @param recipient
	 * @param subject
	 * @param bodyOfEmail
	 * @return
	 */
	public boolean sendEmail(String recipient, String subject, String bodyOfEmail) {

		boolean isSent = false;

		try {

			String from = eos.accounts().properties().get("emailfrom").value();
			if (from.length() == 0) {
				from = eos.emailFrom();
			}

			Email emailFrom = new Email(from);
			Email to = new Email(recipient);
			Content content = new Content("text/html", bodyOfEmail);
			Mail mail = new Mail(emailFrom, subject, to, content);

			SendGrid sg = new SendGrid(eos.sendGridKey());
			Request request = new Request();

			try {

				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				Response response = sg.api(request);
				isSent = true;
				// System.out.println("RESPONSE FROM EMAIL ATTEMPT:" +
				// response.getStatusCode());

			} catch (IOException ex) {

				System.err.println("ERRORS SENDING EMAIL:" + ex.toString());
				throw ex;

			}

		} catch (Exception e) {
			System.err.println("Email failure: " + e.toString());
			eos.getLog().log("Errors sending email using SENDGRID Interface. Error:" + e.getMessage(), "EmailManager",
					"sendEmail", com.eos.Constants.LOG_FATAL);
		}

		return isSent;

	}

}