package uz.spring.appownjwtpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    @NotBlank
    private String message;

    @NotNull
    private UUID receiverId;


    private UUID attachmentId;
}
