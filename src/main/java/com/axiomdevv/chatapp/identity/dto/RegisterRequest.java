package com.axiomdevv.chatapp.identity.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank
                              @Size(max = 50)
                              @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,49}$",message = "Username must start with a letter and contain only letters, digits and underscores (3–50 characters).")
                              String username ,
                              @NotBlank
                              @Email
                              @Size(max = 100)
                              String email ,
                              @NotBlank
                              @Size(min = 8 ,max = 72)
                              String password)
{
    @Override
    public String toString(){

        return  "RegisterRequest[" +
                "Username : " + username +
                "  Email : " + email +
                "  Password : " + "******" + " ]";
    }
}
