package dynamicduo;

import java.net.MalformedURLException;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class SauceLabsTest {
    private WebDriver driver;
    //Create an instance of a Selenium explicit wait so that we can dynamically wait for an element
    private WebDriverWait wait;

    @Test
    @DisplayName("testWebshop()")
    public void testWebshop(TestInfo testInfo) throws MalformedURLException {
        /**
         * In this section, we will configure our SauceLabs credentials in order to run our tests on saucelabs.com
         */
        String sauceUserName = "mpac.beks";
        String sauceAccessKey = "6cd97f69-76b0-457e-ab90-94a0db3ed7e7";

        String browser = System.getProperty("browser");
        String platform = System.getProperty("platform");
        String version = System.getProperty("version");
        System.out.println(browser + " " + version + "  " + platform);


        /**
         * In this section, we will configure our test to run on some specific
         * browser/os combination in Sauce Labs
         */
        DesiredCapabilities capabilities = new DesiredCapabilities();

        //set your user name and access key to run tests in Sauce
        capabilities.setCapability("username", sauceUserName);

        //set your sauce labs access key
        capabilities.setCapability("accessKey", sauceAccessKey);

        //set browser to Safari
        capabilities.setCapability("browserName", browser);

        //set operating system to macOS version 10.13
        capabilities.setCapability("platform", platform);

        //set the browser version to 11.1
        capabilities.setCapability("version", version);

        //set your test case name so that it shows up in Sauce Labs
        capabilities.setCapability("name", testInfo.getDisplayName());

        driver = new RemoteWebDriver(new URL("http://ondemand.eu-central-1.saucelabs.com:80/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 5);
        boolean result = login();
        emptyCart();

        if (result) {
            double totalApperal = getRandomApperal(2,10);
            double totalPhones = getRandomPhone(2,10);
            double expectedtotal = totalApperal + totalPhones;
            System.out.println("Expected total is " + expectedtotal);

            double cartTotal = getTotalPrice();
            assertEquals(expectedtotal, cartTotal);
            if(expectedtotal == cartTotal)
                ((JavascriptExecutor) driver).executeScript("sauce:job-result=passed");
            else
                ((JavascriptExecutor) driver).executeScript("sauce:job-result=failed");

        } else {
            ((JavascriptExecutor) driver).executeScript("sauce:job-result=failed");
        }


        driver.navigate().to("http://demowebshop.tricentis.com/logout");


        /**
         * Here we teardown the driver session and send the results to Sauce Labs
         */
        driver.quit();

    }

    private boolean login() {
        //navigate to the url of the Sauce Labs Sample app
        driver.navigate().to("http://demowebshop.tricentis.com/login");


        //wait for the user name field to be visible and store that element into a variable
//            By userNameFieldLocator = By.id("Email");
        By userNameFieldLocator = By.cssSelector("input#Email");
        wait.until(ExpectedConditions.visibilityOfElementLocated(userNameFieldLocator));

        //type the user name string into the user name field
        driver.findElement(userNameFieldLocator).sendKeys("kelsinga@avans.nl");

        //type the password into the password field
//            driver.findElement(By.id("Password")).sendKeys("krizzie11");
        driver.findElement(By.cssSelector("input#Password")).sendKeys("krizzie11");

        //hit Login button
        driver.findElement(By.cssSelector("input.login-button")).click();

        //Synchronize on the next page and make sure it loads
        By accountLocator = By.className("account");
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountLocator));
        //Assert that the inventory page displayed appropriately
        boolean result = driver.findElements(accountLocator).size() > 0;
        if(result)
            System.out.println("Logged in");
        else
            System.out.println("Failed to Log in");

        return result;
    }

    private void emptyCart(){
        driver.navigate().to("http://demowebshop.tricentis.com/cart");
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[name=removefromcart]"));
        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }

       driver.findElement(By.cssSelector("input.update-cart-button")).click();

       wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("input[name=removefromcart]"), 0));
        System.out.println("Emptied cart");

    }

    private double getTotalPrice(){
        driver.navigate().to("http://demowebshop.tricentis.com/cart");
        By priceLocator = By.cssSelector("div.mini-shopping-cart div.totals strong");
        String cartTotal = driver.findElement(priceLocator).getAttribute("innerHTML");
        System.out.println("Cart total price is " + cartTotal);
        return Double.parseDouble(cartTotal);

    }


    private double getRandomPhone(int min, int max) {
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;

        int randomPhone = new Random().nextInt(phones.values().length - 1);
        driver.navigate().to(phones.values()[randomPhone].getValue());

        By quantityLocator = By.cssSelector("input.qty-input");
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityLocator));
        WebElement quantity = driver.findElement(quantityLocator);
        quantity.clear();
        quantity.sendKeys(String.valueOf(randomNumber));

        driver.findElement(By.cssSelector("input.add-to-cart-button")).click();

        String price = driver.findElement(By.cssSelector("div.product-price>span")).getText();
        double total = Double.parseDouble(price) * randomNumber;
        System.out.println("randomPhones total is " + total);
        return total;
    }


    private double getRandomApperal(int min, int max) {
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;

        int randomClothes = new Random().nextInt(clothes.values().length - 1);
        driver.navigate().to(clothes.values()[randomClothes].getValue());

        By quantityLocator = By.cssSelector("input.qty-input");
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityLocator));
        WebElement quantity = driver.findElement(quantityLocator);
        quantity.clear();
        quantity.sendKeys(String.valueOf(randomNumber));

        driver.findElement(By.cssSelector("input.add-to-cart-button")).click();

        String price = driver.findElement(By.cssSelector("div.product-price>span")).getText();
        double total = Double.parseDouble(price) * randomNumber;
        System.out.println("randomApparel total is " + total);
        return total;

    }




    private enum phones {
        USED_PHONE("http://demowebshop.tricentis.com/used-phone"),
        SMARTPHONE("http://demowebshop.tricentis.com/smartphone");

        private String value;

        private phones(String newvalue) {
            value = newvalue;
        }

        private String getValue() {
            return value;
        }
    }

    private enum clothes {
        CASUAL_GOLF_BELT("http://demowebshop.tricentis.com/casual-belt"),
        BLUE_JEANS("http://demowebshop.tricentis.com/blue-jeans");

        private String value;

        private clothes(String newvalue) {
            value = newvalue;
        }

        private String getValue() {
            return value;
        }

    }

}



