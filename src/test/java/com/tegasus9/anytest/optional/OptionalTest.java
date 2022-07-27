package com.tegasus9.anytest.optional;

import com.tegasus9.anytest.optional.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @author XiongYiGe
 * @date 2022/7/22
 * @description
 */
class OptionalTest {
    @Test
    void test() {
        User user = new User();
        user.setName("xiongyige");
        user.setAge(18);
        User.Address.City city = new User.Address.City();
//        city.setCityName("beijing");
        city.setCityCode("100");
        User.Address address = new User.Address();
//        address.setCity(city);
        user.setAddress(address);
        String cityName = Optional.ofNullable(user)
                .map(User::getAddress)
                .map(User.Address::getCity)
                .map(User.Address.City::getCityName)
                .orElseThrow(() -> new RuntimeException("city is null"));
        System.out.println("cityName = " + cityName);
    }
}
