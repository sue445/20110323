package sample;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import scenic3.tester.util.ClassUtil;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * テストスイートと同一パッケージにあるテストクラスを自動的に探して実行するためのスイートクラス
 * <ul>
 * <li>クラス名の末尾が"Test"である</li>
 * <li>クラス名の先頭が"Abstract"でない</li>
 * </ul>
 * クラスをテスト対象のクラスとみなしてテストを実行します<br>
 * また下記のアノテーションをつけることによりオプションを設定できます。
 * @author sue445
 *
 */
public class AllTestsSuite extends Suite {
	/**
	 * 下位のパッケージを再帰的に検索する
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SearchRecurse{

	}

	/**
	 * 検索対象に含めるクラス名
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface IncludeNames{
		/**
		 * 検索対象に含めるクラス名（正規表現）<br>
		 * 指定しない場合は全て含める。（".*"と同義）
		 */
		String[] value();
	}

	/**
	 * 検索対象に含めないクラス名
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ExcludeNames{
		/**
		 * 検索対象に含ないクラス名（正規表現）<br>
		 * 指定しない場合は何も除外しない。
		 */
		String[] value();
	}

	public AllTestsSuite(Class<?> clazz) throws InitializationError {
		super(clazz, getTestClasses(clazz));
	}

	private static Class<?>[] getTestClasses(Class<?> clazz) throws InitializationError{
		boolean isRecurse = clazz.isAnnotationPresent(SearchRecurse.class);
		String[] includeNames = {".*Test$"};
		String[] excludeNames = {"^Abstract.*"};
		if(clazz.isAnnotationPresent(IncludeNames.class)){
			includeNames = clazz.getAnnotation(IncludeNames.class).value();
		}
		if(clazz.isAnnotationPresent(ExcludeNames.class)){
			excludeNames = clazz.getAnnotation(ExcludeNames.class).value();
		}

		List<Class<?>> list = ClassUtil.findClass(clazz.getPackage(), includeNames, excludeNames, isRecurse);

		if(list == null || list.size() == 0){
			throw new InitializationError("Not found TestClass in this package");
		}
		return list.toArray(new Class<?>[0]);
	}
}
