package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CardDeliveryPositiveTests {
    public Calendar setDate(int days) {
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, days);
        return date;
    }

    public String deliveryDate(Calendar setDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(setDate.getTime());
    }

    public String getDateCalendar(String deliveryDate) {
        int date = $$x("//td[@data-day]").size();
        for(int i = 0; i<date; i++){
            String text = $$x("//td[@data-day]").get(i).getText();
            if(text.equalsIgnoreCase(deliveryDate)) {
                $$x("//td[@data-day]").get(i).click();
            }
        }
        return deliveryDate;
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void clear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    //Тесты валидации поля Город (1-е задание) +тест дефолтной даты
    @Test
    void shouldShowSuccessfulBookingPopUpIfValidCity() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldShowSuccessfulBookingPopUpIfCityNameLowercase() {
        $x("//span[@data-test-id='city']//child::input").setValue("тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldShowSuccessfulBookingPopUpIfCityNameWithDash() {
        $x("//span[@data-test-id='city']//child::input").setValue("Санкт-Петербург");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    //Тесты валидации поля Город (2-е задание)
    @Test
    void shouldSelectCityFromDropDownList1Item() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тю");
        $$x("//div[@class = 'popup__container']").get(1).shouldBe(visible);
        $(byText("Тюмень")).click();
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldSelectCityFromDropDownListSeveralItems() {
        $x("//span[@data-test-id='city']//child::input").setValue("Кр");
        $$x("//div[@class = 'popup__container']").get(1).shouldBe(visible);
        $(byText("Красноярск")).click();
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    //Тест валидации поля Дата не дефолтная дата (1-е задание).
    @Test
    void shouldShowSuccessfulBookingPopUpIfCustomDate() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        String inputDeliveryDate = deliveryDate(setDate(4));
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(inputDeliveryDate);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    //Тест поля Дата с выпадающим меню (Задание 2)
    @Test
    void shouldSelectDateFromCalendar() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").click();
        $x("//div[@class = 'popup__container']").should(visible);
        getDateCalendar("20");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldSelectDateFromCalendarSwitchArrowMonth() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").click();
        $x("//div[@class = 'popup__container']").should(visible);
        $x("//div[@class='calendar__arrow calendar__arrow_direction_right']").click();
        getDateCalendar("6");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldSelectDateFromCalendarSwitchArrowYear() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").click();
        $x("//div[@class = 'popup__container']").should(visible);
        $x("//div[@class='calendar__arrow calendar__arrow_direction_right calendar__arrow_double']")
                .click();
        getDateCalendar("6");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    //Тесты валидации поля Фамилия и Имя
    @Test
    void shouldShowSuccessfulBookingPopUpIfNameWithDash() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Анна-Мария Пупкина");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldShowSuccessfulBookingPopUpIfNameWithLetterЁ() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Ёлкин Александр");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldShowSuccessfulBookingPopUpIfNameWithSeveralParts() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Абу Али ибн Сина");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    //Тест закрытия попапа
    @Test
    void shouldCloseSuccessfulBookingPopUp() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
        $x("//div[@data-test-id='notification']//child::button").click();
        $x("//div[@data-test-id='notification']").shouldNotBe(visible);
    }

    @Test
    void successfulBookingPopUpShouldNotCloseAfterClick() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
        $x("//span[@data-test-id='city']").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible);
    }
}