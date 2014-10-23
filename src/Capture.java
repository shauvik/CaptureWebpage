import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;


public class Capture {

	public static void main(String[] args) throws IOException {
		String url1 = "https://dl.dropboxusercontent.com/u/22889397/sonal/oracle.html";
		String url2 = "https://dl.dropboxusercontent.com/u/22889397/sonal/test_new.html";
		
		WebDriver driver = new FirefoxDriver();
		capture(url1, driver, "oracle");
		
		driver = new FirefoxDriver();
		capture(url2, driver, "test");
	}

	private static void capture(String url, WebDriver driver, String page)
			throws IOException {
		driver.get(url);
		
		// Get JS DOM
		try {
			String webdiff_script = getPkgFileContents(Capture.class, "webdiff2.js");
			String pageStructure =  (String) ((JavascriptExecutor) driver).executeScript(webdiff_script);
			Files.write(Strings.nullToEmpty(pageStructure), new File("output/"+ page + ".json"), Charsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Could not save page structure");
		}
		// Save screenshot
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(String.format("output/%s.png",page)));
		driver.quit();
	}

	/**
	 * Read a file that is in the package structure
	 * 
	 * @param pkgFileName
	 * @return file contents
	 */
	public static String getPkgFileContents(Class cls, String pkgFileName) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(cls.getResourceAsStream(pkgFileName), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
}
