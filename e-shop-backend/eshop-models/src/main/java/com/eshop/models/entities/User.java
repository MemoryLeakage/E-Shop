package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "username")
    private String username;
    @Column(name = "rating")
    private Float rating;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    public static class Builder {
        private String email;
        private String username;
        private Float rating;
        private String firstName;
        private String lastName;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder rating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build(){
            User user = new User();
            user.email = this.email;
            user.firstName = this.firstName;
            user.lastName = this.lastName;
            user.rating = this.rating;
            user.username = this.username;
            return user;
        }
    }
}
