package com.guilhermecosta.todo.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {

    ADMIN(1, "ROLE_ADMIN"), //Deve seguir esse modelo para o Spring detectar
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    //Informa codigo para receber o seu Enum correspondente
    public static ProfileEnum toEnum(Integer code) {

        if (Objects.isNull(code)) return null;

        for (ProfileEnum ref : ProfileEnum.values()) {
            if (code.equals(ref.getCode())) return ref;
        }

        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
