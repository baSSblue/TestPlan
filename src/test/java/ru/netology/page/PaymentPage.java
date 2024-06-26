package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement heading = $(byText("Путешествие дня"));
    private final SelenideElement headingPayment = $$(".button_size_m").findBy(text("Купить"));
    private final SelenideElement headingPaymentCredit = $$(".button_view_extra").findBy(text("Купить в кредит"));
    private final SelenideElement paymentByCard = $(byText("Оплата по карте"));
    private final SelenideElement credit = $(byText("Кредит по данным карты"));
    private final SelenideElement cardNumberField = $$(".form-field").findBy(text("Номер карты")).find(".input__control");
    private final SelenideElement monthField = $$(".form-field .input-group__input-case").findBy(text("Месяц")).find(".input__control");
    private final SelenideElement yearField = $$(".form-field .input-group__input-case").findBy(text("Год")).find(".input__control");
    private final SelenideElement fieldOwner = $$(".form-field .input-group__input-case").findBy(text("Владелец")).find(".input__control");
    private final SelenideElement fieldCVC_CVV = $$(".form-field .input-group__input-case").findBy(text("CVC/CVV")).find(".input__control");
    private final SelenideElement resume = $$(".button__text").findBy(text("Продолжить"));
    private final SelenideElement successNotification = $(".notification_status_ok .notification__content");
    private final SelenideElement errorNotification = $(".notification_status_error .notification__content");
    private final SelenideElement invalidFormat = $$(".form-field .input__sub").findBy(text("Неверный формат"));
    private final SelenideElement fieldIsRequired = $$(".form-field .input__sub").findBy(text("Поле обязательно для заполнения"));
    private final SelenideElement incorrectExpirationDate = $$(".form-field .input__sub").findBy(text("Неверно указан срок действия карты"));
    private final SelenideElement theCardIsExpired = $$(".form-field .input__sub").findBy(text("Истёк срок действия карты"));

    public void headingPaymentPage() {
        heading.shouldHave(visible);
        headingPayment.click();
        paymentByCard.shouldHave(visible);
    }

    public void loanPage() {
        heading.shouldHave(visible);
        headingPaymentCredit.click();
        credit.shouldHave(visible);
    }

    public void cardNumber(String getCardNumber) {
        cardNumberField.setValue(getCardNumber);
    }

    public void monthNumber(String getMonthNumber) {
        monthField.setValue(getMonthNumber);
    }

    public void yearNumber(String getYears) {
        yearField.setValue(getYears);
    }

    public void fullName(String getName) {
        fieldOwner.setValue(getName);
    }

    public void CVC(String getCVC) {
        fieldCVC_CVV.setValue(getCVC);
    }

    public void buttonContinue() {
        resume.click();
    }


    @DisplayName("Операция одобрена банком.")
    public void successNotification(String expectedText) {
        successNotification
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    @DisplayName("Ошибка! Банк отказал в проведении операции.")
    public void errorNotification(String expectedText) {
        errorNotification
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public void invalidFormatError() {
        invalidFormat.shouldBe(visible, Duration.ofSeconds(10));
    }


    public void fieldIsRequiredError() {
        fieldIsRequired.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void incorrectExpirationDateError() {
        incorrectExpirationDate.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void theCardIsExpiredError() {
        theCardIsExpired.shouldBe(visible, Duration.ofSeconds(10));
    }
}
