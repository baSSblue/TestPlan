package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {
    private static final SelenideElement headingPayment= $(".button_size_m");
    private static final SelenideElement headingPaymentCredit = $(".button_view_extra");

    public MainPage() {
        headingPayment.shouldHave(text("Купить")).shouldBe(visible);
        headingPaymentCredit.shouldHave(text("Купить в кредит")).shouldBe(visible);
    }

    public PaymentPage openPaymentPage(DataHelper.CardInfo info) {
        open("http://localhost:8080/");
        headingPayment.click();
        return new PaymentPage();
    }

    public CreditPage openCreditPage(DataHelper.CardInfo info) {
        open("http://localhost:8080/");
        headingPaymentCredit.click();
        return new CreditPage();
    }
}