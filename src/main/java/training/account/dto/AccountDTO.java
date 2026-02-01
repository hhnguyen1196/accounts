package training.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {

    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
