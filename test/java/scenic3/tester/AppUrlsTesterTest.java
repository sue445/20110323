package scenic3.tester;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slim3.util.AppEngineUtil;

import scenic3.UrlMatcher;
import scenic3.tester.page.Dummy1Page;
import scenic3.tester.page.Dummy2Page;
import scenic3.tester.page.Dummy3Page;
import scenic3.tester.page.Dummy4Page;
import scenic3.tester.util.ClassUtils;


public class AppUrlsTesterTest {
	private static final String SCENIC3_ROOT_PACKAGE = "scenic3.tester";
	private AppUrlsTester tester = new AppUrlsTester();

	@Test
	public void isScenicPageClass1(){
		assertThat(tester.isScenicPageClass(Dummy1Page.class), is(true));
	}

	@Test
	public void isScenicPageClass2(){
		assertThat(tester.isScenicPageClass(Dummy2Page.class), is(false));
	}

	@Test
	public void isScenicPageClass3(){
		assertThat(tester.isScenicPageClass(Dummy3Page.class), is(false));
	}

	@Test
	public void findAllPageClass1(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		List<Class<?>> actual = tester.findAllPageClass(SCENIC3_ROOT_PACKAGE);
		String[] expected = {
				"scenic3.tester.page.Dummy1Page",
				"scenic3.tester.page.Dummy4Page",
		};
		assertThat(ClassUtils.toString(actual), contains(expected));
	}

	@Test
	public void getPageMatcher(){
		UrlMatcher actual = tester.getPageMatcher(SCENIC3_ROOT_PACKAGE, Dummy4Page.class);
		assertThat(actual.getClass().getName(), is("scenic3.tester.controller.matcher.Dummy4PageMatcher"));
	}

	@Test
	public void findAllPageMatcher(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		List<UrlMatcher> actual = tester.findAllPageMatcher(SCENIC3_ROOT_PACKAGE);
		assertThat(actual.size(), is(2));
		assertThat(actual.get(0).getClass().getName(), is("scenic3.tester.controller.matcher.Dummy1PageMatcher"));
		assertThat(actual.get(1).getClass().getName(), is("scenic3.tester.controller.matcher.Dummy4PageMatcher"));
	}

	@Ignore("確認用")
	@Test
	public void test(){
		List<Class<?>> pageList = tester.findAllPageClass("net.sue445.jubeatplusplus");
		for(Class<?> clazz : pageList){
			System.out.println(clazz);
		}

		List<UrlMatcher> matcherList = tester.findAllPageMatcher("net.sue445.jubeatplusplus");
		for(UrlMatcher matcher : matcherList){
			System.out.println(matcher.getClass());
		}
	}

}
