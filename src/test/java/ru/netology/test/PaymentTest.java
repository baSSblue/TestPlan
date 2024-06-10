package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;

import java.time.Duration;


public class PaymentTest {
    PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        paymentPage = open("http://localhost:8080", PaymentPage.class);
        paymentPage.headingPaymentPage();
    }



    @Test
    @DisplayName("Заполнение формы валидными данными")
    void shouldSuccessfullyPayFromApprovedCard() {
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        paymentPage.validPayCard(cardInfo);
        paymentPage.successNotification("Операция одобрена банком.");
        var PaymentStatus = SQLHelper.getStatus();
        Assertions.assertEquals("APPROVED", PaymentStatus);
    }

    @Test
    @DisplayName("Заполнение формы данными карты со статусом DECLINED")
    void shouldShowErrorWhenPayFromDeclinedCard() {
        var cardInfo = DataHelper.SecondCardNumberAndStatus();
        paymentPage.validPayCard(cardInfo);
        paymentPage.errorNotification("Ошибка! Банк отказал в проведении операции.");
        var PaymentStatus = SQLHelper.getStatus();
        Assertions.assertEquals("DECLINED", PaymentStatus);
    }

    @Test
    @DisplayName("Введение 16значного номера карты рандомными цифрами")
    void randomCardNumber() {
        var cardInfo = DataHelper.generateCardNumber();
        paymentPage.validPayCard(cardInfo);

        paymentPage.errorNotification("Ошибка! Банк отказал в проведении операции.");
    }

    @Test
    @DisplayName("Введение невалидного 16значного номера карты из одних нулей")
    void nullCardNumber() {
        var cardInfo = DataHelper.generateRandomCard();
        paymentPage.validPayCard(cardInfo);
        paymentPage.errorNotification("Ошибка! Банк отказал в проведении операции.");
    }

    @Test
    @DisplayName("Введение невалидного 15значного номера карты")
    void random15CardNumber() {

        paymentPage.validPayCard(DataHelper.generateCardNumber());
        paymentPage.invalidFormatError();
    }

    @Test
    @DisplayName("Введение невалидного значения месяца - 13")
    void month13(){

        paymentPage.validPayCard(DataHelper.generateValidDate(13, 0, "MM")););
        paymentPage.incorrectExpirationDateError();
    }
    @Test
    @DisplayName("Введение данных карты с истекшим сроком действия (на один месяц меньше текущего)")
    void month05(){

        paymentPage.theCardIsExpired.shouldBe(visible);

    }
    @Test
    @DisplayName("Введение невалидного значения года (на 5 лет больше текущего)")
    void yearPlus5(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 5, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.incorrectExpirationDate.shouldBe(visible);
    }
    @Test
    @DisplayName("Введение данных карты с истекшим сроком действия (на один год меньше текущего)")
    void lastYear(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateLastYear(1,  "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.theCardIsExpired.shouldBe(visible);
    }
    @Test
    @DisplayName("Использование кирилицы в поле Владелец")
    void ownerCyrillic(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateRandomSurnameCyrillic());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Использование цифр в поле Владелец")
    void ownerNumbers(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateNotValidCard());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Использование спецсимволов в поле Владелец")
    void specialSymbols(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.randomSymbol());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Использование двух цифр в поле CVC/CVV")
    void cvvTwoSymbols(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateShortCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Введение в поле Владелец пробела вместо Имени и Фамилии")
    void spaceOwner(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName("    ");
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Введение в поле CVC/CVV трех нулей")
    void cvvThreeNull(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC("000");
        paymentPage.buttonContinue();
        paymentPage.invalidFormat.shouldBe(visible);
    }

    @Test
    @DisplayName("Введение в поле год два нуля")
    void yearTwoNull(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber("00");
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.incorrectExpirationDate.shouldBe(visible);
    }

    @Test
    @DisplayName("Введение в поле месяц два нуля")
    void month00(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber("00");
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.incorrectExpirationDate.shouldBe(visible);
    }

    @Test
    @DisplayName("Незаполненное поле номера карты")
    void spaceCardNumber(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber("");
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.fieldIsRequired.shouldBe(visible);
    }

    @Test
    @DisplayName("Незаполенное поле месяца")
    void spaceMonth(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber("");
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.fieldIsRequired.shouldBe(visible);
    }

    @Test
    @DisplayName("Незаполненное поле года")
    void spaceYear(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber("");
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.fieldIsRequired.shouldBe(visible);
    }

    @Test
    @DisplayName("Незаполненное поле Владелец")
    void ownerSpace(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName("");
        paymentPage.CVC(DataHelper.generateCVC_CVV());
        paymentPage.buttonContinue();
        paymentPage.fieldIsRequired.shouldBe(visible);
    }

    @Test
    @DisplayName("Незаполненное поле CVC/CVV")
    void cvvSpace(){
        var cardInfo = DataHelper.FirstCardNumberAndStatus();
        
        paymentPage.cardNumber(cardInfo.getCardNumber());
        paymentPage.monthNumber(DataHelper.generateValidDate(0, 0, "MM"));
        paymentPage.yearNumber(DataHelper.generateValidDate(0, 0, "yy"));
        paymentPage.fullName(DataHelper.generateName());
        paymentPage.CVC("");
        paymentPage.buttonContinue();
        paymentPage.fieldIsRequired.shouldBe(visible);
    }
}