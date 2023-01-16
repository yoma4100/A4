package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryFormTest {

    public String nearestAvailableDate() {
        LocalDateTime ldt = LocalDateTime.now().plusDays(3);
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String nearestAvailableDate = format1.format(ldt);

        return nearestAvailableDate;
    }

    public String nearestDay() {
        LocalDateTime ldt = LocalDateTime.now().plusDays(3);
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd");
        String nearestDay = format1.format(ldt);

        return nearestDay;
    }

    @Test
    public void shouldSendForm() {
        int i = 0;

        String nearestAvailableDate = nearestAvailableDate();
        String nearestDay = nearestDay();

        String xPathForCalendar = "//td[text()='" + nearestDay + "']";

        Configuration.holdBrowserOpen = false;

        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").click();
        $x("//input[@placeholder='Город']").setValue("Казань");
        $x("//span[@data-test-id='date']").click();
        while (i < 11) {
            $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
            i++;
        }
        $x("//input[@placeholder='Дата встречи']").setValue(nearestAvailableDate);
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.ESCAPE);
        $x("//input[@name='name']").click();
        $("[data-test-id='name'] input").setValue("Петров-Иванов Сидор");
        $x("//input[@name='phone']").click();
        $("[data-test-id='phone'] input").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $(withText("Успешно!")).waitUntil(exist, 15000);
    }

    @Test
    public void shouldSendFormWithPopups() {
        int i = 0;

        String nearestAvailableDate = nearestAvailableDate();
        String nearestDay = nearestDay();

        String xPathForCalendar = "//td[text()='" + nearestDay + "']";

        Configuration.holdBrowserOpen = false;

        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").click();
        $x("//input[@placeholder='Город']").setValue("Ка");
        $x("//span[text()='Казань']").click();
        $x("//span[@data-test-id='date']").click();
        while (i < 11) {
            $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
            i++;
        }
        $x("//input[@placeholder='Дата встречи']").setValue(nearestAvailableDate);
        $x(xPathForCalendar).click();
        $x("//input[@name='name']").click();
        $("[data-test-id='name'] input").setValue("Петров-Иванов Сидор");
        $x("//input[@name='phone']").click();
        $("[data-test-id='phone'] input").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $(withText("Успешно!")).waitUntil(exist, 15000);
    }
}
