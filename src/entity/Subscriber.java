package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriber {

    private String name;
    private String dateOfBirth;
    private Country country;
    private City city;
    private Gender gender;
    private User user;
 //   List<Param> params = new ArrayList<>();
}
