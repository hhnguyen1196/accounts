package training.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {

    private String name;

    private String email;

    private String mobileNumber;

    private AccountDTO accountDTO;
}
