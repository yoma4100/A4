package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryFormTest {

    public String generateDate(long days, String dataFormat) {
        LocalDate ldt = LocalDate.now().plusDays(days);
        DateTimeFormatter formatData = DateTimeFormatter.ofPattern(dataFormat);
        return formatData.format(ldt);
    }

    @Test
    public void shouldSendForm() {
        Configuration.holdBrowserOpen = false;
        Configuration.timeout = 15000;

        String nearestAvailableDate = generateDate(3,"dd.MM.yyyy");
        String nearestDay = generateDate(3,"dd");

        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").click();
        $x("//input[@placeholder='Город']").setValue("Казань");
        $x("//span[@data-test-id='date']").click();
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL+"a", Keys.BACK_SPACE));
        $x("//input[@placeholder='Дата встречи']").setValue(nearestAvailableDate);
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.ESCAPE);
        $x("//input[@name='name']").click();
        $("[data-test-id='name'] input").setValue("Петров-Иванов Сидор");
        $x("//input[@name='phone']").click();
        $("[data-test-id='phone'] input").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + nearestAvailableDate))
                .shouldBe(Condition.visible);
        $(withText("Успешно!")).waitUntil(exist, 15000);
    }


    @Test
    public void shouldSendFormWithPopups() {
        Configuration.holdBrowserOpen = false;
        Configuration.timeout = 15000;

        String nextWeekDate = generateDate(14,"dd.MM.yyyy");
        String nearestDay = generateDate(14,"d");

        LocalDate current = LocalDate.now();
        LocalDate nextWeek = LocalDate.now().plusDays(14);
        String xPathForCalendar = "//*[text()='" + nearestDay + "']";

        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").click();
        $x("//input[@placeholder='Город']").setValue("Ка");
        $x("//span[text()='Казань']").click();
        $x("//span[@data-test-id='date']").click();

        if (current.getMonth() != nextWeek.getMonth()) {
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        }
        $x(xPathForCalendar).click();
        $x("//input[@name='name']").click();
        $("[data-test-id='name'] input").setValue("Петров-Иванов Сидор");
        $x("//input[@name='phone']").click();
        $("[data-test-id='phone'] input").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + nextWeekDate))
                .shouldBe(Condition.visible);
        $(withText("Успешно!")).waitUntil(exist, 15000);
    }
}
