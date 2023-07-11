package org.example.Resources;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.Resources.User;

public class UserGenerator {
    public static User random() {
        return new User(RandomStringUtils.randomAlphabetic(8) + "@praktikum.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphabetic(6));
    }
}
