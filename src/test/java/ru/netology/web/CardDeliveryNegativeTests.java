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
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryNegativeTests {
    public Calendar setDate(int days) {
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, days);
        return date;
    }

    public String deliveryDate(Calendar setDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(setDate.getTime());
    }

    @BeforeEach
    public void setup() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void clear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    //Тесты валидации поля Город (1-е задание) +тест дефолтной даты
    @Test
    void shouldShowErrorIfCityNotAdministrativeCenter() {
        $x("//span[@data-test-id='city']//child::input").setValue("Сургут");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("недоступна"));
    }

    @Test
    void shouldShowErrorIfCityWithLatinLetters() {
        $x("//span[@data-test-id='city']//child::input").setValue("Moscow");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("недоступна"));
    }

    @Test
    void shouldShowErrorIfCityWithNumbers() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюм3нь");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("недоступна"));
    }

    @Test
    void shouldShowErrorIfCityWithSymbols() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тю^^ень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("недоступна"));
    }

    @Test
    void shouldShowErrorIfCityFieldEmpty() {
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='city']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("обязательно для заполнения"));
    }

    //Тест выбора города из выпадающего списка (задание 2)
    @Test
    void shouldNotSelectCityFromDropDownListLatinLetters() {
        $x("//span[@data-test-id='city']//child::input").setValue("Lo");
        $$x("//div[@class = 'popup__container']").get(1).shouldBe(hidden);
    }

    //Тесты валидации поля Дата (1-е задание)
    @Test
    void shouldShowErrorIfMeetingDayAfterTomorrow() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        String inputDeliveryDate = deliveryDate(setDate(2));
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(inputDeliveryDate);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='date']/span/span").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату"));
    }

    @Test
    void shouldShowErrorIfMeetingDateToday() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        String inputDeliveryDate = deliveryDate(setDate(0));
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(inputDeliveryDate);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='date']/span/span").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату"));
    }

    @Test
    void shouldShowErrorIfMeetingDateYesterday() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        String inputDeliveryDate = deliveryDate(setDate(-1));
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//child::input").setValue(inputDeliveryDate);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='date']/span/span").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату"));
    }

    @Test
    void shouldShowErrorIfDateEmpty() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='date']//child::input").doubleClick().
                sendKeys(Keys.BACK_SPACE);
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='date']/span/span").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Неверно"));
    }

    //Тесты на валидацию поле Фамилия и Имя
    @Test
    void shouldShowErrorIfNameWithLatinLetters() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Nikita Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("неверно"));
    }

    @Test
    void shouldShowErrorIfIfNameOneWord() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("неверно"));
    }

    @Test
    void shouldShowErrorIfWithNumber() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Н1к1та Пуп7ин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("неверно"));
    }

    @Test
    void shouldShowErrorIfNameWithSymbols() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никит@ П^пкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("неверно"));
    }

    @Test
    void shouldShowErrorIfNameEmpty() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfNameOnlySpaceBar() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("  ");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfNameOnlyDashesBar() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("---");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='name']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("неверно"));
    }

    //Тесты на валидацию поля Номер телефона
    @Test
    void shouldShowErrorIfIfPhoneStarts8() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("89057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfIfPhoneStarts7() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("79057048510");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneEmpty() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfPhone10Numbers() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+7905704851");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhone12Numbers() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+790570485102");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneWithLetters() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79о57о4851о2");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneWithSymbols() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+7#050%4&5102");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("указан неверно"));
    }

    @Test
    void shouldShowErrorIfPhoneOnlySpaceBar() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("   ");
        $x("//label[@data-test-id='agreement']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//span[@data-test-id='phone']").shouldHave(cssClass("input_invalid"));
        $x("//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorIfCheckboxEmpty() {
        $x("//span[@data-test-id='city']//child::input").setValue("Тюмень");
        $x("//span[@data-test-id='name']//child::input").setValue("Никита Пупкин");
        $x("//span[@data-test-id='phone']//child::input").setValue("+79057048510");
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//label[@data-test-id='agreement']").shouldHave(cssClass("input_invalid"));
    }
}
