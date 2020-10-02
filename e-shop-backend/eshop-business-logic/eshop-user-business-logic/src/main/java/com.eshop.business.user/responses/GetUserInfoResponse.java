package com.eshop.business.user.responses;

import java.util.Set;

public class GetUserInfoResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
    private Float rating;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Float getRating() {
        return rating;
    }

    public static class Builder {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private Set<String> roles;
        private Float rate;

        public Builder username(String username) {
            this.username = username;
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

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder rate(Float rate) {
            this.rate = rate;
            return this;
        }

        public GetUserInfoResponse build() {
            GetUserInfoResponse response = new GetUserInfoResponse();
            response.username = this.username;
            response.firstName = this.firstName;
            response.lastName = this.lastName;
            response.email = this.email;
            response.rating = this.rate;
            response.roles = this.roles;
            return response;
        }
    }
}
