package com.foolmind.game.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "admins")
public class Admin extends Employee {
    // default constructor for spring boot
    public Admin() {
    }

    // Builder Pattern
    private Admin(Builder builder) {
        setEmail(builder.email);
        setSaltedHashedPassword(builder.saltedHashedPassword);
        setName(builder.name);
        setAddress(builder.address);
        setPhoneNumber(builder.phoneNumber);
    }

    public static final class Builder {
        private @Email @NotBlank String email;
        private @NotBlank String saltedHashedPassword;
        private @NotBlank String name;
        private @NotBlank String address;
        private @NotBlank String phoneNumber;

        public Builder() {
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder saltedHashedPassword(String val) {
            saltedHashedPassword = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            phoneNumber = val;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}
