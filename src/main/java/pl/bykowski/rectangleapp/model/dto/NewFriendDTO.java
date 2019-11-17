package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bykowski.rectangleapp.annotation.IsThisUserNameShouldExistCheck;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewFriendDTO {
    @IsThisUserNameShouldExistCheck(value = true, message = "This user doesn't exist")
    private String name;
}
