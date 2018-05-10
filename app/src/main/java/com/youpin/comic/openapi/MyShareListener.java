package com.youpin.comic.openapi;

import android.os.Bundle;

public interface MyShareListener {
	
	public void onSuccess(Bundle info);
	
	public void onError(Bundle info);
	
	public void onCancel(Bundle info);
	
	
}
