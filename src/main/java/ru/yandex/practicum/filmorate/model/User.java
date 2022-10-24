package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

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
    @Pattern(regexp = "^\\S*$")
    protected String email;
    @NotBlank(message = "empty data in Login")
    @NonNull
    @Pattern(regexp = "^\\S*$", message = "wrong symbols in login")
    protected String login;
    protected String name;
    @Past(message = "birthday can`t be future")
    protected LocalDate birthday;

    protected Set<Long> friends;

}


