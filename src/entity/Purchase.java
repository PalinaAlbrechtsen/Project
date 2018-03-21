package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    private Long id;
    private String dateOfPurchase;
    private Subscriber subscriber;
    private Program program;
}
