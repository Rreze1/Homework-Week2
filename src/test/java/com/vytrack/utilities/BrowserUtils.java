package com.vytrack.utilities;

import com.vytrack.utilities.Driver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BrowserUtils{
    /*
    takes screenshot
    @param name
    take a name of a test and returns
     */
    public static String getScreenShot (String name) throws IOException {
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) Driver.get();
        File source = ts.getScreenshotAs(OutputType.FILE);
        String target = System.getProperty("user.dir")+"/test-output/Screenshots/"+ name + date + ".png";
        File finalDestination = new File (target);
        FileUtils.copyFile(source, finalDestination);
        return target;

    }

    public static void switchToWindow(String targetTitle){
        String origin = Driver.get().getWindowHandle();
        for (String handle: Driver.get().getWindowHandles()){
            Driver.get().switchTo().window(handle);
            if (Driver.get().getTitle().equals(targetTitle)){
                return;
            }
        }
        Driver.get().switchTo().window(origin);
    }

    public static void hover (WebElement element){
        Actions actions = new Actions(Driver.get());
        actions.moveToElement(element).perform();
    }
    /**
     * Extracts text from list of elements matching the provided locator into new List<String>
     *
     * @param locator
     * @return list of strings
     */

    public static List<String> getElementsText(By locator){
        List<WebElement>elems = Driver.get().findElements(locator);
        List<String> elemTexts = new ArrayList<>();

        for (WebElement el: elems){
            elemTexts.add(el.getText());

        }
        return elemTexts;

    }
    /** Performs a pause
     * @param seconds
     */
    public static void waitFor(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Waits for the provided element to be visible on the page
     *
     * @param element
     * @param timeToWaitInSec
     * @return
     */

    public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec){
        WebDriverWait wait= new WebDriverWait(Driver.get(), timeToWaitInSec);
        return wait.until(ExpectedConditions.visibilityOf(element));

    }
    /**
     * Waits for element matching the locator to be visible on the page
     *
     * @param locator
     * @param timeout
     * @return
     */

    public static WebElement waitForVisibility (By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
//    /**
//     * Waits for provided element to be clickable
//     *
//     * @param element
//     * @param timeout
//     * @return
//     */
    public static WebElement waitForClickability(By locator, int timeout){
        WebDriverWait wait = new WebDriverWait(Driver.get(), timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    /**
     * Verifies whether the element matching the provided locator is displayed on page
     *
     * @param by
     * @throws AssertionError if the element matching the provided locator is not found or not displayed
     */
    public static void verifyElementDisplayed (By by){
        try{
            assertTrue(Driver.get().findElement(by).isDisplayed(), "Element not visible: "+ by);
        }catch (NoSuchElementException e ){
            e.printStackTrace();
            Assert.fail("Element not found: "+by);
        }
    }
    /**
     * Verifies whether the element matching the provided locator is NOT displayed on page
     *
     * @param by
     * @throws AssertionError the element matching the provided locator is displayed
     */
    public static void verifyElementNotDisplayed (By by){
        try{
            assertFalse (Driver.get().findElement(by).isDisplayed(), "Element should not be visible: "+by);

        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }
    /**
     * Verifies whether the element is displayed on page
     *
     * @param element
     * @throws AssertionError if the element is not found or not displayed
     */

    public static void verifyElementDisplayed (WebElement element){
        try {
            assertTrue(element.isDisplayed(), "Element not visible: " +element);
        }catch (NoSuchElementException e ){
            e.printStackTrace();
            Assert.fail("Element not found: " + element);
        }
    }
    /**
     * Waits for element to be not stale
     *
     * @param element
     */
    public void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    /**
     * Clicks on an element using JavaScript
     *
     * @param element
     */
    public static void clickWithJS(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].click();", element);
    }


    /**
     * Scrolls down to an element using JavaScript
     *
     * @param element
     */
    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(Driver.get()).doubleClick(element).build().perform();
    }

    /**
     * Changes the HTML attribute of a Web Element to the given value using JavaScript
     *
     * @param element
     * @param attributeName
     * @param attributeValue
     */
    public static void setAttribute(WebElement element, String attributeName, String attributeValue) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, attributeValue);
    }

    /**
     * Highlighs an element by changing its background and border color
     * @param element
     */
    public static void highlight(WebElement element) {
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
        waitFor(1);
        ((JavascriptExecutor) Driver.get()).executeScript("arguments[0].removeAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }
    /**
     * Checks or unchecks given checkbox
     *
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    /**
     * attempts to click on provided element until given time runs out
     *
     * @param element
     * @param timeout
     */
    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }
    /**
     * executes the given JavaScript command on given web element
     *
     * @param element
     */
    public static void executeJScommand(WebElement element, String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.get();
        jse.executeScript(command, element);

    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param command
     */
    public static void executeJScommand(String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.get();
        jse.executeScript(command);

    }


}