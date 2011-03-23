package scenic3.tester.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.util.AppEngineUtil;

public class ClassUtilsTest {
	private String beforeClassPath;


	@Before
	public void before(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}
		beforeClassPath = ClassUtils.getClassPath();
	}

	@After
	public void after(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}
		setClassPath(beforeClassPath);
	}

	private void setClassPath(String classpath) {
		System.getProperties().setProperty("java.class.path", classpath);
	}


	@Test
	public void findClass1(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		assertThat(ClassUtils.toString(ClassUtils.findClass("scenic3.tester.util")),
				contains(
						"scenic3.tester.util.ClassUtils",
						"scenic3.tester.util.ClassUtilsTest",
						"scenic3.tester.util.Dummy1"
				)
		);
	}

	@Test
	public void findClass2(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		assertThat(ClassUtils.toString(ClassUtils.findClass("scenic3.tester.util", new String[]{".*Test"}, null, false)),
				contains(
						"scenic3.tester.util.ClassUtilsTest"
				)
		);
	}

	@Test
	public void findClass3(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		assertThat(ClassUtils.toString(ClassUtils.findClass("scenic3.tester.util", null, new String[]{"Dummy1"}, false)),
				contains(
						"scenic3.tester.util.ClassUtils",
						"scenic3.tester.util.ClassUtilsTest"
				)
		);
	}

	@Test
	public void findClass4(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		assertThat(ClassUtils.toString(ClassUtils.findClass("scenic3.tester", null, null, true)),
				contains(
						"scenic3.tester.AppUrlsTester",
						"scenic3.tester.AppUrlsTesterTest",
						"scenic3.tester.controller.matcher.Dummy1PageMatcher",
						"scenic3.tester.controller.matcher.Dummy4PageMatcher",
						"scenic3.tester.page.Dummy1Page",
						"scenic3.tester.page.Dummy2Page",
						"scenic3.tester.page.Dummy3Page",
						"scenic3.tester.page.Dummy4Page",
						"scenic3.tester.util.ClassUtils",
						"scenic3.tester.util.ClassUtilsTest",
						"scenic3.tester.util.Dummy1"
				)
		);
	}

	@Test
	public void getPackageDirList1(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		List<String> packageDirList = ClassUtils.getPackageDirList("scenic3.tester.util");
		assertThat(packageDirList, is(notNullValue()));
		assertThat(packageDirList.size(), is(1));

		String packageDirectory = packageDirList.get(0).replace(File.separator, "/");
		assertThat(packageDirectory, endsWith("/WEB-INF/classes/scenic3/tester/util"));
	}

	@Test
	public void getPackageDirList2(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		setClassPath("hoge");
		List<String> packageDirList = ClassUtils.getPackageDirList("scenic3.tester.util");
		assertThat(packageDirList.size(), is(0));
	}

	@Test
	public void isMultipleMatch1(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		String str = null;
		String[] regexes = null;
		assertThat(ClassUtils.isMultipleMatch(str, regexes), is(false));
	}

	@Test
	public void isMultipleMatch2(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		String str = "1234abcdef";
		String[] regexes = new String[]{null};
		assertThat(ClassUtils.isMultipleMatch(str, regexes), is(false));
	}

	@Test
	public void isMultipleMatch3(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		String str = "1234abcdef";
		String[] regexes = new String[]{".*[A-Z].*", ".*[a-z].*"};
		assertThat(ClassUtils.isMultipleMatch(str, regexes), is(true));
	}

	@Test
	public void isMultipleMatch4(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		String str = "1234abcdef";
		String[] regexes = new String[]{".*[A-Z].*"};
		assertThat(ClassUtils.isMultipleMatch(str, regexes), is(false));
	}

	@Test
	public void getClassPath1(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		assertThat(ClassUtils.getClassPath(), is(notNullValue()));
		assertThat(ClassUtils.getClassPath().length(), is(greaterThan(0)));
	}

	@Test(expected = NullPointerException.class)
	public void getClassPath2(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			throw new NullPointerException();
		}

		setClassPath("");
		ClassUtils.getClassPath();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testToString(){
		if(AppEngineUtil.isProduction()){
			// Production環境ではクラスパスがとれないためテストできない
			return;
		}

		List<Class<?>> classList = Arrays.asList(ClassUtils.class, ClassUtilsTest.class);
		List<String> actual = ClassUtils.toString(classList);
		assertThat(actual, contains("scenic3.tester.util.ClassUtils", "scenic3.tester.util.ClassUtilsTest"));
	}
}
