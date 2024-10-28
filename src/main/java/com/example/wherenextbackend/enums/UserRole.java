package com.example.wherenextbackend.enums;

public enum UserRole {

        ADMIN("Administrator"),
        USER("User");

        private String label;

        UserRole(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }


