package com.br.neogym.domain.valueobjects;

public final class Password {

    private final String value;

    public Password(String value){

        this.value = value;
    }

    private void validatePassword(String password){
        if (password.isEmpty() || password.isBlank()){
            throw new IllegalArgumentException("");
        }
    }
}
