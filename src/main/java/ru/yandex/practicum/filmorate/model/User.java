package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    public User(@NonNull String email, @NonNull String login, String name, LocalDate birthday) {

        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    protected int id;
    @NotBlank(message = "empty data in Email")
    @NonNull
    @Email(message = "invalid Email")
    protected String email;
    @NotBlank(message = "empty data in Login")
    @NonNull
    protected String login;
    protected String name;
    @Past(message = "birthday can`t be future")
    protected LocalDate birthday;

}


