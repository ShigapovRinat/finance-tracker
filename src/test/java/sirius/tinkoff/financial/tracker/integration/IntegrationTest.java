package sirius.tinkoff.financial.tracker.integration;

import sirius.tinkoff.financial.tracker.dao.dto.CategoryDto;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;

import java.util.List;

public class IntegrationTest extends AbstractIntegrationTest {

    protected List<CategoryDto> getAllCategories(String login) throws Exception {
        return getList("/api/users/current/categories", login, CategoryDto.class);
    }

    protected List<TransactionGetResponse> getAllTransactions(String login, Long walletId) throws Exception {
        return getList("/api/wallets/" + walletId + "/transactions", login, TransactionGetResponse.class);
    }

}
