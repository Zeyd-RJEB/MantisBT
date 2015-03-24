package tn.rnu.ensi.mantisbt.mailconfig;

import java.util.ArrayList;

import tn.rnu.ensi.mantisbt.utilities.Utils;
import android.content.Context;
import android.util.Log;

public class BackgroundMail {
	String TAG = "Background Mail Library";
	String username, password, mailto, subject, body, sendingMessage,
			sendingMessageSuccess;
	boolean processVisibility = true;
	ArrayList<String> attachments = new ArrayList<String>();
	Context mContext;

	public BackgroundMail(Context context) {
		this.mContext = context;

		// set default display messages
		this.sendingMessage = "Loading...";
		this.sendingMessageSuccess = "Your message was sent successfully.";
	}

	public void setGmailUserName(String string) {
		this.username = string;
	}

	public void setGmailPassword(String string) {
		this.password = string;
	}

	public void setProcessVisibility(boolean state) {
		this.processVisibility = state;
	}

	public void setMailTo(String string) {
		this.mailto = string;
	}

	public void setFormSubject(String string) {
		this.subject = string;
	}

	public void setFormBody(String string) {
		this.body = string;
	}

	public void setSendingMessage(String string) {
		this.sendingMessage = string;
	}

	public void setSendingMessageSuccess(String string) {
		this.sendingMessageSuccess = string;

	}

	public void setAttachment(String attachments) {
		this.attachments.add(attachments);

	}

	public void send() {
		boolean valid = true;
		if (username == null) {
			Log.e(TAG, "You didn't set a Gmail username!");
			valid = false;
		}
		if (password == null ) {
			Log.e(TAG, "You didn't set a Gmail password!");
			valid = false;
		}
		if (mailto == null ) {
			Log.e(TAG, "You didn't set an email recipient!");
			valid = false;
		}
		if (Utils.isNetworkAvailable(mContext) == false) {
			Log.e(TAG, "User doesn't have a working internet connection!");
			valid = false;
		}
		if (valid == true) {
			new Thread() {
				public void run() {
					try {
						GmailSender sender = new GmailSender(username, password);
						if (!attachments.isEmpty()) {
							for (int i = 0; i < attachments.size(); i++) {
								if (!attachments.get(i).equals(null)) {
									sender.addAttachment(attachments.get(i));
								}
							}
						}
						sender.sendMail(subject, body, username, mailto);
					} catch (Exception e) {
						e.printStackTrace();
						if (e.getMessage() != null)
							Log.e(TAG, e.getMessage().toString());
					}

				}

			}.start();

		}
	}
}
