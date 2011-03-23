package scenic3.tester.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slim3.util.ClassUtil;

/**
 * クラスファイルを直接操作するためのユーティリティ<br>
 * (appengineのProduction環境上では使えないので注意（クラスパスが取得できない）
 * @author sue445
 *
 */
public final class ClassUtils {
	private static final String CLASS_EXTENSION = ".class";

	private ClassUtils(){

	}

	/**
	 * 任意のパッケージにあるクラスを全て取得する
	 * @param packageName		パッケージ
	 * @return
	 */
	public static List<Class<?>> findClass(String packageName){
		return findClass(packageName, null, null, false);
	}

	/**
	 * 任意のパッケージにあるクラスを全て取得する
	 * @param packageName			基準となるパッケージ
	 * @param includeNames	含める名前（正規表現で指定、nullの時は全て含める）
	 * @param excludeNames	除外する名前（正規表現で指定、nullの時は除外しない）
	 * @param isRecurse		下位のパッケージを再帰的に検索するかどうか
	 * @return				クラスファイル。クラスファイルが1つもなければ空リストを返却する
	 */
	public static List<Class<?>> findClass(String packageName, String[] includeNames, String[] excludeNames, boolean isRecurse){
		List<String> packageDirList = getPackageDirList(packageName);
		if(packageDirList.size() == 0){
			return Collections.emptyList();
		}

		List<String> classNameList = new ArrayList<String>();

		for(String packageDir : packageDirList){
			List<String> list = searchPackageDir(packageName, packageDir, includeNames, excludeNames, isRecurse);
			classNameList.addAll(list);
		}

		if(classNameList.size() == 0){
			return Collections.emptyList();
		}

		List<Class<?>> classList = new ArrayList<Class<?>>();
		for(String className : classNameList){
			Class<?> clazz = ClassUtil.forName(className);
			classList.add(clazz);
		}

		return classList;
	}

	/**
	 * パッケージのディレクトリを取得する
	 * @param packageName
	 * @return
	 */
	public static List<String> getPackageDirList(String packageName){
		String classpath = getClassPath();
		String packagePath = packageName.replace(".", File.separator);

		List<String> packageList = new ArrayList<String>();
		for(String path : classpath.split("[;:]")){
			path = path.trim();

			if(!isAvailableDirectory(path)){
				// ディレクトリの場合のみ検索対象とする
				continue;
			}

			String packageDir = new File(path).getAbsolutePath() + File.separator + packagePath;
			if(isAvailableDirectory(packageDir)){
				// ディレクトリが存在する
				packageList.add(packageDir);
			}
		}

		return packageList;
	}

	/**
	 * 任意のパッケージにあるクラスを全て取得する
	 * @param packageName	パッケージ名
	 * @param packageDir	パッケージのあるディレクトリ
	 * @param includeNames	含める名前（正規表現で指定、nullの時は全て含める）
	 * @param excludeNames	除外する名前（正規表現で指定、nullの時は除外しない）
	 * @param isRecurse		下位のパッケージを再帰的に検索するかどうか
	 * @return				検索したクラス名の一覧
	 */
	private static List<String> searchPackageDir(String packageName, String packageDir, String[] includeNames, String[] excludeNames, boolean isRecurse){
		List<String> classNameList = new ArrayList<String>();
		File dir = new File(packageDir);

		for(String name : dir.list()){
			String filePath = packageDir + File.separator + name;
			File file = new File(filePath);
			if(!file.exists()){
				continue;
			}

			if(file.isFile()){
				if(!isClassFile(name) || isInnerClass(name)){
					// classファイル以外とインナークラスは除外する
					continue;
				}

				String className = name.replace(CLASS_EXTENSION, "");
				if(!isEmptyAray(includeNames) && !isMultipleMatch(className, includeNames)){
					// インクルードリストに含まれていない
					continue;
				}

				if(!isEmptyAray(excludeNames) && isMultipleMatch(className, excludeNames)){
					// 除外リストに含まれている
					continue;
				}

				classNameList.add(packageName + "." + className);

			} else if(file.isDirectory() && isRecurse){
				// 下位のディレクトリを再帰的に検索する
				String subPackageName = packageName + "." + name;
				String subPackageDir = packageDir + File.separator + name;
				List<String> list = searchPackageDir(subPackageName, subPackageDir, includeNames, excludeNames, isRecurse);
				classNameList.addAll(list);
			}

		}

		return classNameList;
	}

	/**
	 * クラスファイルかどうか調べる
	 * @param fileName
	 * @return
	 */
	private static boolean isClassFile(String fileName) {
		return fileName.endsWith(CLASS_EXTENSION);
	}

	/**
	 * インナークラスかどうか調べる
	 * @param fileName
	 * @return
	 */
	private static boolean isInnerClass(String fileName) {
		return fileName.contains("$");
	}

	/**
	 * 空配列かどうか調べる
	 * @param array
	 * @return
	 */
	private static <T> boolean isEmptyAray(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 有効なディレクトリかどうか調べる
	 * @param dir
	 * @return
	 */
	private static boolean isAvailableDirectory(String dir){
		File file = new File(dir);
		return file.exists() && file.isDirectory();
	}

	/**
	 * クラスパスを取得する。
	 * @return
	 * @throws NullPointerException		クラスパスの取得に失敗
	 */
	public static String getClassPath() {
		String classpath = System.getProperties().getProperty("java.class.path");
		if(classpath == null || classpath.length() == 0){
			throw new NullPointerException("classpath can not get");
		}
		return classpath;
	}

	/**
	 * 複数の正規表現のどれか1つにマッチしているかどうか調べる
	 * @param str
	 * @param regexes
	 * @return
	 */
	public static boolean isMultipleMatch(String str, String[] regexes){
		if(str == null || regexes == null){
			return false;
		}

		for(String regex : regexes){
			if(regex == null){
				continue;
			}

			if(str.matches(regex)){
				return true;
			}
		}

		return false;
	}

	/**
	 * ClassオブジェクトをFQCNで文字列化する
	 * @param classList
	 * @return
	 */
	public static List<String> toString(List<Class<?>> classList){
		if(classList == null || classList.size() == 0){
			return null;
		}

		List<String> list = new ArrayList<String>();

		for(Class<?> clazz : classList){
			list.add(clazz.getName());
		}
		return list;
	}

}
