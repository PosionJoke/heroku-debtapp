package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bykowski.rectangleapp.annotation.IsThisUserNameExistCheck;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewFriendDTO {
    @IsThisUserNameExistCheck(message = "This user don't exist")
    private String name;
}
