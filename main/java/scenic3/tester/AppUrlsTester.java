package scenic3.tester;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slim3.util.ClassUtil;
import org.slim3.util.StringUtil;
import org.slim3.util.WrapRuntimeException;

import scenic3.ScenicPage;
import scenic3.UrlMatcher;
import scenic3.Urls;
import scenic3.annotation.Page;
import scenic3.tester.util.ClassUtils;

/**
 * AppUrlsで全てのPageMatcherが使われているかを確認するためのテスタークラス<br>
 * 仕様
 * <ul>
 * <li>Production環境上では動きません</li>
 * <li>デフォルトパッケージ上のPageクラスは検索できません</li>
 * <li>jarに含まれているPageクラスは検索できません</li>
 * <li></li>
 * </ul>
 * @author sue445
 *
 */
public class AppUrlsTester {
	/**
	 * AppUrlsで全てのPageMatcherが使われているかを表明する
	 * @param rootPackage
	 * @param appUrls
	 */
	protected void assertAppUrls(String rootPackage, Urls appUrls){
		List<UrlMatcher> pageMatcherList = findAllPageMatcher(rootPackage);
		for(UrlMatcher pageMatcher : pageMatcherList){
			assertThat(createErrorMessage(appUrls, pageMatcher),
					appUrls.toString(), containsString(pageMatcher.toString()));
		}
	}

	/**
	 * エラーメッセージを生成する
	 * @param appUrls
	 * @param urlMatcher
	 * @return
	 */
	private String createErrorMessage(Urls appUrls, UrlMatcher urlMatcher) {
		return "[" + urlMatcher.getClass().getName() + "] is not found in [" + appUrls.getClass().getName() + "]";
	}

	/**
	 * ルートパッケージ配下の全てのUrlMatcherクラスを検索する
	 * @param rootPackage
	 * @return
	 */
	List<UrlMatcher> findAllPageMatcher(String rootPackage){
		List<Class<?>> pageClassList = findAllPageClass(rootPackage);
		List<UrlMatcher> pageMatcherList = new ArrayList<UrlMatcher>();

		for(Class<?> pageClass : pageClassList){
			UrlMatcher pageMatcher = getPageMatcher(rootPackage, pageClass);
			if(pageMatcher != null){
				pageMatcherList.add(pageMatcher);
			}
		}

		return pageMatcherList;
	}

	/**
	 * ルートパッケージ配下の全てのPageクラスを検索する
	 * @param rootPackage
	 * @return
	 */
	// package private
	List<Class<?>> findAllPageClass(String rootPackage){
		if(StringUtil.isEmpty(rootPackage)){
			throw new NullPointerException(rootPackage + " is required");
		}

		List<Class<?>> allClassList = ClassUtils.findClass(rootPackage, null, null, true);
		List<Class<?>> pageClassNameList = new ArrayList<Class<?>>();

		for(Class<?> clazz : allClassList){
			if(isScenicPageClass(clazz)){
				pageClassNameList.add(clazz);
			}
		}

		return pageClassNameList;
	}

	/**
	 * Scenic3のPageクラスかどうか調べる
	 * @param clazz
	 * @return
	 */
	// package private
	boolean isScenicPageClass(Class<?> clazz) {
		return ScenicPage.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Page.class);
	}

	/**
	 * Pageクラス名からUrlMatcherクラスのインスタンスを取得する
	 * @param rootPackage
	 * @param pageClass
	 * @return		UrlMatcherのインスタンス。取得に失敗した場合はnullを返却する
	 */
	// package private
	UrlMatcher getPageMatcher(String rootPackage, Class<?> pageClass){
		String pageMatcherClassName = rootPackage + ".controller.matcher." + pageClass.getSimpleName() + "Matcher";

		try {
			Class<?> clazz = ClassUtil.forName(pageMatcherClassName);
			Method method = clazz.getMethod("get", (Class<?>[])null);
			return (UrlMatcher)method.invoke(null, (Object[])null);
		} catch (Exception e) {
			throw new WrapRuntimeException(pageClass.getName() + "に対応したUrlMatcherが存在しない", e);
		}
	}


}
