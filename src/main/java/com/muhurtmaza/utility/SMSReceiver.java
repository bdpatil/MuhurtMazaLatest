package com.muhurtmaza.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.muhurtmaza.ui.NewOTPActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		StringTokenizer tokenizer = new StringTokenizer("HP-Muhurt",",");
		Set<String> phoneEnrties = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			phoneEnrties.add(tokenizer.nextToken().trim());
		}
		Bundle bundle = intent.getExtras();
		Object[] pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] messages = new SmsMessage[pdus.length];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String address = messages[i].getOriginatingAddress();
			if (phoneEnrties.contains(address)) {
				Intent newintent = new Intent(context, NewOTPActivity.class);
				newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				newintent.putExtra("address", address);
				newintent.putExtra("message",messages[i].getDisplayMessageBody());
				context.startActivity(newintent);
			}
		}
	}
}