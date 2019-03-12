package com.mailalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class CheckMailAndSend {
    Context ctx;
    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail="mailproject84@gmail.com";
    String fromPassword="newsecure123";
    List<String> toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;
    SharedPreferences prefs;
    public  void send(Context ctx, String key, String data,Date date) {

        this.ctx=ctx;

        prefs=ctx.getSharedPreferences("prefs",0);

        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
int dow=cal.get(Calendar.DAY_OF_WEEK);

toEmailList=new Vector<String>();
toEmailList.add(prefs.getString("email",""));
        toEmailList.add(prefs.getString("alemail",""));

        new SendMailTask().execute(toEmailList,key +"Alert ",data+" "+date);

    }

  void   init(
    List toEmailList, String emailSubject, String emailBody) {
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }
    public class SendMailTask extends AsyncTask {




        protected void onPreExecute() {
            Toast.makeText(ctx, "Getting ready...", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected Object doInBackground(Object... args) {
            try {
                Log.i("SendMailTask", "About to instantiate GMail...");
                publishProgress("Processing input....");
              init((List) args[0], args[1].toString(),
                        args[2].toString());
                publishProgress("Preparing mail message....");
                createEmailMessage();
                publishProgress("Sending email....");
                sendEmail();
                publishProgress("Email Sent.");
                Log.i("SendMailTask", "Mail Sent.");
            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("SendMailTask", e.getMessage(), e);
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Object... values) {
            Log.d("MailAlert",values[0].toString());

        }

        @Override
        public void onPostExecute(Object result)

        {}

    }


    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (Object toEmail : toEmailList) {
            Log.i("GMail","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail.toString()));
        }

        emailMessage.setSubject(emailSubject);
        //emailMessage.setContent(emailBody, "text/html");// for a html email
         emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }
}
