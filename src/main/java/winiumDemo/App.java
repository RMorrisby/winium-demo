package winiumDemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Partial demonstration of Winium. This demo opens up a new instance of Window's calculator, tries to click some of
 * the buttons (this doesn't work), then reads out the calculator's display.
 *
 * You must separately start Winium.Desktop.Driver.exe beforehand (just run it in a terminal & keep the terminal open)
 * Seemingly, you must run Winium.Desktop.Driver.exe with admin-rights (i.e. 'Run As Administrator')
 */
public class App {

    static Logger log = LoggerFactory.getLogger(App.class);

    /*
    Need to start Winium Desktop Driver exe separately before running any Winium script
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Hello World!");
        log.info("Hello World2!");

//        goDemo1();

        goDemo2();

    }

    public static void goDemo1() throws MalformedURLException, InterruptedException {
        DesktopOptions option = new DesktopOptions();

        option.setApplicationPath("C:\\Windows\\System32\\calc.exe");
        WiniumDriver driver = null;

        try {
            driver = new WiniumDriver(new URL("http://localhost:9999"), option);  // throws UnreachableBrowserException
            log.info("Winium Driver object formed");

            Thread.sleep(5000);
            log.info("After sleep 1");
//            pressAltTab();

            String handle = switchToCalc(driver);

            Thread.sleep(2000);
            log.info("After sleep 2");
            clickSeven(driver);
            Thread.sleep(1000);
//            driver.findElement(By.name("Plus")).click(); // nothing happens
            driver.findElement(By.name("Calculator")).findElement(By.name("Plus")).click(); // nothing happens
            Thread.sleep(1000);
            driver.findElement(By.name("Eight")).click(); // nothing happens
            Thread.sleep(1000);
            driver.findElement(By.name("Equals")).click(); // nothing happens

            Thread.sleep(5000);
            log.info("After sleep 3");
            String output = driver.findElement(By.id("CalculatorResults")).getAttribute("Name");

            log.info("Result after addition is: " + output);

            log.info("Hello World3!");
            log.info("Hello World4!");

            driver.quit();

        } catch (UnreachableBrowserException e) {
            log.info("Could not connect to Winium Desktop Driver. Has it been started?");
        }
        log.info("Hello World5!");
        log.info("Hello World6!");
        log.info("Hello World7!");
        log.info("Hello World8!");
    }

    private static void clickSeven(WiniumDriver driver) {

        log.info(String.valueOf(driver.getSessionId()));
//        log.info(String.valueOf(driver.getTitle())); //getTitle isn't implemented?!?!
//        log.info(String.valueOf(driver.getPageSource())); //getPageSource isn't implemented?!?!

        driver.findElement(By.name("Calculator")).findElement(By.name("Seven")).click();
//        driver.findElement(By.name("Seven")).click();

    }

    /**
     * Returns the window handle
     *
     * @param driver
     * @return
     */
    private static String switchToCalc(WiniumDriver driver) {
        // Handles can be got, but they're treated as Longs instead of Strings,
        // despite the compiler thinking they're strings???

//        java.util.List<String> handles = Lists.newArrayList(driver.getWindowHandles());
//        log.info(handles.toString());
//        log.info(String.valueOf(handles.size()));
//        log.info(String.valueOf(handles.get(0)).getClass().getCanonicalName());
//        log.info(handles.get(0).getClass().getCanonicalName());
//
//        for (String h : handles) {
//            log.info(String.valueOf(h));
//        }

//            WebElement newWindow = driver.findElement(By.name("calc.exe"));

        WebElement newWindow = driver.findElement(By.name("Calculator"));
        String newWindowHandle = newWindow.getAttribute("NativeWindowHandle");
        log.info("new handle : " + newWindowHandle);
        driver.switchTo().window(newWindowHandle);

        return newWindowHandle;
    }

    // Works; is brittle
    public static void pressAltTab() {

        Robot robot = null;
        try {
            robot = new Robot();

            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_TAB);

        } catch (AWTException e) {
            log.error("Failed to have Robot send Alt-Tab");
            e.printStackTrace();
        }
    }



    /////////////////////////////////////////////////////////////////////////////////
    // A second demo, lifted straight from https://gist.github.com/jaspreet07/3cf118a794dd6d7759e374c974859981
    // (with some extra logging)
    //
    // Doesn't work - service won't start.

    public static void goDemo2() throws IOException, InterruptedException {
        WiniumDriver driver = null;

        DesktopOptions option = new DesktopOptions();

        option.setApplicationPath("C:\\Windows\\System32\\calc.exe");

        WiniumDriverService service = new WiniumDriverService.Builder()
                .usingDriverExecutable(new File("C:\\path\\Winium.Desktop.Driver.exe")).usingPort(9999)
                .withVerbose(true).withSilent(false).buildDesktopService();

        service.start();
        log.info("Winium Service started");

        driver = new WiniumDriver(service, option);
        log.info("Winium Driver object formed");

        Thread.sleep(3000);
        log.info("After sleep 1");

        driver.findElement(By.id("num3Button")).click();

        boolean num4val = driver.findElement(By.id("num4Button")).isEnabled();

        log.info(String.valueOf(num4val));

        driver.findElement(By.id("num4Button")).click();
        Thread.sleep(1000);
         driver.findElement(By.id("num5Button")).click();

        String output = driver.findElement(By.id("CalculatorResults")).getAttribute("Name");

        log.info("Display after input is: " + output);

    }

}
