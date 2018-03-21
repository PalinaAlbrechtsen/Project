package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Param {

    private Long id;
    private String date;
    private Integer height;
    private Double weight;
    private Subscriber subscriber;
}
