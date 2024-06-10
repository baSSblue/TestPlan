package ru.netology.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private DataHelper() {
    }

    private static final Faker faker1 = new Faker(new Locale("ru"));
    private static final Faker faker = new Faker(new Locale("en"));

    public static CardInfo FirstCardNumberAndStatus() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");
    }

    public static CardInfo SecondCardNumberAndStatus() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String generateRandomCard() {
        return faker.finance().creditCard(CreditCardType.MASTERCARD);
    }

    public static String generateNotValidCard() {
        return faker.numerify("#### #### #### ###");
    }

    public static CardInfo generateCardNumber(){
        return new CardInfo("0000 0000 0000 0000", "");
    }

    public static String generateValidDate(int addMonths, int addYears, String pattern) {
        return LocalDate.now().plusMonths(addMonths).plusYears(addYears).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateLastYear(int minusYears, String pattern) {
        return LocalDate.now().minusYears(minusYears).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String justTheName() {
        return faker.name().firstName();
    }

    public static String generateRandomSurnameCyrillic() {
        return faker1.name().fullName();
    }

    public static String generateCVC_CVV() {
        return faker.numerify("###");
    }

    public static String generateShortCVC_CVV() {
        return faker.numerify("##");
    }

    public static String randomSymbol() {
        var symbol = new String[]{"@$%^", "!%^&**", "**$%^<>?", ":?:&^^", "$%^&#", "@#$%&*(", "&^*#$$%", "@##^^*^"};
        return symbol[new Random().nextInt(symbol.length)];
    }


    @Value
    public static class CardInfo {
         private String cardNumber;
         private String status;
    }


}
