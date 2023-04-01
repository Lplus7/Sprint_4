package ru.yandex.praktikum.mytest;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import ru.yandex.praktikum.pom.MainPage;
import ru.yandex.praktikum.pom.OrderPage;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@RunWith(Parameterized.class)
public class OrderTest {

    public static WebDriver driver;
    public static MainPage objMainPage;
    public OrderPage objOrderPage;
    private final int indexButton;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String dateOrder;
    private final String period;
    private final String color;
    private final String comment;

    public OrderTest(int indexButton, String name, String surname, String address, String metro,
                            String phone, String dateOrder, String period, String color, String comment) {
        this.indexButton = indexButton;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.dateOrder = dateOrder;
        this.period = period;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Оформление заказа: " +
        "Способ вызова: {0}; " +
        "Имя: {1}; " +
        "Фамилия: {2}; " +
        "Адрес: {3}; " +
        "Метро: {4}; " +
        "Телефон: {5}; " +
        "Когда нужен: {6}; " +
        "Срок аренды: {7}; " +
        "Цвет: {8}; " +
        "Комментарий: {9}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {0, "Никита", "Мансуров", "Москва", "Выставочная", "+7901234567", "10.03.2023", "трое суток", "grey", "Проверка 1"},
                {1, "Кристина", "Мансурова", "Москва", "Беговая", "+7902345678", "20.03.2023", "сутки", "black", "Проверка 2"}
        };
    }

    @BeforeClass
    public static void initialOrder() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver = new Augmenter().augment(driver);
    }

    @Test
    public void testOrder() {

        driver.get("https://qa-scooter.praktikum-services.ru/");
        objMainPage = new MainPage(driver);
        objMainPage.waitForLoadPage();
        objMainPage.clickGetCookie();

        objMainPage.clickOrder(indexButton);
        objOrderPage = new OrderPage(driver);
        objOrderPage.waitForLoadOrderPage();
        objOrderPage.setDataFieldsAndClickNext(name, surname, address, metro, phone);
        objOrderPage.waitForLoadRentPage();
        objOrderPage.setOtherFieldsAndClickOrder(dateOrder, period, color, comment);

        assertTrue("Отсутствует сообщение об успешном завершении заказа", objMainPage.isElementExist(objOrderPage.orderPlaced));
    }

    @AfterClass
    public static void tearDown() {
        if (driver!=null)
            driver.quit();
    }

}