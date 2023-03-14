package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CardDeliveryPositiveTests {
    LocalDate defaultDay = LocalDate.now().plusDays(3);
    public String setDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
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

    //Тест появления попапа и соответствия выбранной даты
    @Test
    void shouldShowSuccessfulBookingPopUpWithSelectedDate() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        String planningDate = setDate(5, "dd.MM.yyyy");
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(planningDate);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    //Тесты валидации поля Город (1-е задание) +тест дефолтной даты
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
        String planningDate = setDate(4, "dd.MM.yyyy");
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(planningDate);
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
        String deliveryDate = "22";
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").click();
        $x("//div[@class = 'popup__container']").should(visible);
        $$x("//td[@data-day]").findBy(text(deliveryDate)).click();
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@data-test-id='notification']").shouldBe(visible, ofSeconds(15));
        $x("//div[@class ='notification__title']").shouldBe(text("Успешно"));
    }

    @Test
    void shouldSelectDateFromCalendarNextMonth() {
        LocalDate meetingDate = LocalDate.now().plusDays(30);
        String deliveryDate = String.valueOf(meetingDate.getDayOfMonth());
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").click();
        $x("//div[@class = 'popup__container']").should(visible);
        if ((meetingDate.getMonthValue() > defaultDay.getMonthValue())) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$x("//td[@data-day]").findBy(text(deliveryDate)).click();
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