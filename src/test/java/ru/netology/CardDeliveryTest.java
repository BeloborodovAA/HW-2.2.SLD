package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitFormSuccessfully() {
        // 1. Генерируем дату: текущая дата + 3 дня
        String deliveryDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 2. Заполняем форму
        $("[data-test-id='city'] input").setValue("Москва");

        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(deliveryDate);

        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        $("[data-test-id='notification']")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldAcceptRussianLetterYo() {
        String deliveryDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(deliveryDate);

        $("[data-test-id='name'] input").setValue("Пётр Ёлкин"); // БАГ!

        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        $("[data-test-id='notification']")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + deliveryDate));
    }
}