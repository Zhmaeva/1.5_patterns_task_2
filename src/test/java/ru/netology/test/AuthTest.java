package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;


public class AuthTest {

    // selectors
    String loginFieldSelector = "[data-test-id='login'] input";
    String passwordFieldSelector = "[data-test-id='password'] input";
    String continueButtonSelector = "[data-test-id='action-login']";
    String notificationErrorSelector = "[data-test-id='error-notification']";

    // messages
    String notificationContentText = "Ошибка! " + "Неверно указан логин или пароль";
    String notificationUserBlocked = "Пользователь заблокирован";
    String personalAccountText = "Личный кабинет";

    // methods
    public void fillingFieldsForEntering(String login, String password) {
        $(loginFieldSelector).setValue(login);
        $(passwordFieldSelector).setValue(password);
        $(continueButtonSelector).click();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        fillingFieldsForEntering(registeredUser.getLogin(), registeredUser.getPassword());
        $(".heading").should(Condition.visible, Condition.text(personalAccountText));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        fillingFieldsForEntering(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        $(notificationErrorSelector).should(Condition.visible, Condition.text(notificationContentText));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        fillingFieldsForEntering(blockedUser.getLogin(), blockedUser.getPassword());
        $(notificationErrorSelector).should(Condition.visible, Condition.text(notificationUserBlocked));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        fillingFieldsForEntering(wrongLogin, registeredUser.getPassword());
        $(notificationErrorSelector).should(Condition.visible, Condition.text(notificationContentText));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        fillingFieldsForEntering(registeredUser.getLogin(), wrongPassword);
        $(notificationErrorSelector).should(Condition.visible, Condition.text(notificationContentText));
    }
}
