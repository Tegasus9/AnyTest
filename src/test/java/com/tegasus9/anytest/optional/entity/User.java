package com.tegasus9.anytest.optional.entity;

import lombok.Data;

/**
 * @author XiongYiGe
 * @date 2022/7/22
 * @description
 */
@Data
public class User {
    private String name;
    private Integer age;
    private Address address;
    @Data
    public static class Address {
        private String street;
        private City city;
        private String state;
        private String zip;
        @Data
        public static class City {
            private String cityName;
            private String cityCode;
        }
    }

}
