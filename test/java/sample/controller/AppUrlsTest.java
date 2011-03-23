package sample.controller;

import org.junit.Test;
import org.slim3.util.AppEngineUtil;

import scenic3.tester.AppUrlsTester;


public class AppUrlsTest extends AppUrlsTester {
	@Test
	public void checkPageMatcher(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストしない
			return;
		}
		assertAppUrls("sample", new AppUrls());
	}
}
