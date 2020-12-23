package com.eshop.controllers;

import java.util.ArrayList;
import java.util.List;

public class ViolationsResponse {
    private List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() {
        return violations;
    }

    public static class Violation {
        private String fieldName;

        private String message;

        public Violation() {
        }

        public Violation(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }
    }
}
