package sirius.tinkoff.financial.tracker;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import sirius.tinkoff.financial.tracker.dao.dto.CategoryDto;
import sirius.tinkoff.financial.tracker.dao.dto.request.CategoryCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.request.TransactionCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.request.WalletCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.AllWalletsForUserResponse;
import sirius.tinkoff.financial.tracker.dao.dto.response.TransactionGetResponse;
import sirius.tinkoff.financial.tracker.integration.IntegrationTest;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionAndCategoryControllerTest extends IntegrationTest {

    private static final String LOGIN = "test2";

    private static Long walletId;
    private static Long profitCategoryId;
    private static Long consumCategoryId;

    @Test
    @Order(0)
    public void createOneWallet() throws Exception {
        WalletCreateRequest provide = new WalletCreateRequest("string", null);
        postMock("/api/wallets", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("string"))
                .andExpect(jsonPath("$.limit").isEmpty());

        AllWalletsForUserResponse response = (AllWalletsForUserResponse)
                getObject("/api/users/current/wallets", LOGIN, AllWalletsForUserResponse.class);

        assertEquals(1, response.getWalletDtoList().size());
        assertEquals(0, response.getAllConsumption());
        assertEquals(0, response.getAllProfit());

        walletId = response.getWalletDtoList().get(0).getId();
        getMock("/api/wallets/" + walletId + "/transactions", LOGIN, status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(1)
    public void createTwoCategories() throws Exception {
        CategoryCreateRequest provide = new CategoryCreateRequest("profitCategory", 20L, true);
        postMock("/api/categories", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("profitCategory"))
                .andExpect(jsonPath("$.pictureId").value(20L))
                .andExpect(jsonPath("$.income").value(true));

        provide = new CategoryCreateRequest("consumCategory", 2L, false);
        postMock("/api/categories", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("consumCategory"))
                .andExpect(jsonPath("$.pictureId").value(2))
                .andExpect(jsonPath("$.income").value(false));

        for (CategoryDto i : getAllCategories(LOGIN)) {
            if (i.isIncome()) {
                profitCategoryId = i.getId();
            } else {
                consumCategoryId = i.getId();
            }
        }
    }

    @Test
    @Order(2)
    public void addTransactionsToCreatedWallet() throws Exception {
        int profit = 0, consum = 0;
        for (int i = 100; i >= 0; i--) {
            Long categoryIdToAdd;
            if (i % 2 == 0) {
                profit += i;
                categoryIdToAdd = profitCategoryId;
            } else {
                consum += i;
                categoryIdToAdd = consumCategoryId;
            }

            TransactionCreateRequest provide = new TransactionCreateRequest(i, categoryIdToAdd, walletId, 1L);
            postMock("/api/transactions", LOGIN, provide, status().isOk());

            getMock("/api/wallets/" + walletId, LOGIN, status().isOk())
                    .andExpect(jsonPath("$.profit").value(profit))
                    .andExpect(jsonPath("$.consumption").value(consum));
        }
    }

    @Test
    @Order(3)
    public void changeCreatedTransactionsType() throws Exception {
        List<TransactionGetResponse> transactions = getAllTransactions(LOGIN, walletId);
        for (TransactionGetResponse i : transactions) {
            i.setCategoryId(profitCategoryId);
            putMock("/api/transactions", LOGIN, i, status().isOk());
        }

        getMock("/api/wallets/" + walletId, LOGIN, status().isOk())
                .andExpect(jsonPath("$.profit").value(
                        transactions.stream().mapToInt(TransactionGetResponse::getAmount).sum()))
                .andExpect(jsonPath("$.consumption").value(0L));
    }

    // toDo shouldn't delete category if it has transactions
//    @Test
//    @Order(4)
//    public void tryToDeleteCategory() throws Exception {
//        deleteMock("/api/categories/" + consumCategoryId, login, null, status().is4xxClientError());
//        deleteMock("/api/categories/" + profitCategoryId, login, null, status().is4xxClientError());
//    }

    @Test
    @Order(5)
    public void deleteCreatedTransactions() throws Exception {
        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.walletDtoList").value(hasSize(1)));

        List<TransactionGetResponse> transactionGetResponseDtos = getAllTransactions(LOGIN, walletId);
        for (TransactionGetResponse i : transactionGetResponseDtos) {
            deleteMock("/api/transactions/delete/" + i.getId(), LOGIN, null, status().isOk());
        }

        getMock("/api/wallets/" + walletId, LOGIN, status().isOk())
                .andExpect(jsonPath("$.profit").value(0))
                .andExpect(jsonPath("$.consumption").value(0));
    }

    @Test
    @Order(6)
    public void deleteCreatedWalletAndCategories() throws Exception {
        deleteMock("/api/wallets/" + walletId, LOGIN, null, status().isOk());

        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.walletDtoList").isEmpty())
                .andExpect(jsonPath("$.allProfit").value(0))
                .andExpect(jsonPath("$.allConsumption").value(0));

        deleteMock("/api/categories/" + consumCategoryId, LOGIN, null, status().isOk());
        deleteMock("/api/categories/" + profitCategoryId, LOGIN, null, status().isOk());
    }
}