package sample.controller;

import sample.controller.matcher.FrontPageMatcher;
import sample.controller.matcher.HomePageMatcher;
import sample.controller.matcher.UserPageMatcher;
import scenic3.UrlsImpl;

/**
 * Scenic3でアプリケーションに利用するPageクラスを設定します。
 *
 * @author shuji.w6e
 */
public class AppUrls extends UrlsImpl {

	public AppUrls() {
		excludes("/favicon.ico", "/css/*", "/ktrwjr/*", "/_ah/*");

		// TODO Pageを追加したら追加する
		add(UserPageMatcher.get());		// /user
		add(HomePageMatcher.get());		// /home
		add(FrontPageMatcher.get());	// /
	}

}