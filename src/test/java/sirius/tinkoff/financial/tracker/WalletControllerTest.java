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
import sirius.tinkoff.financial.tracker.dao.dto.response.WalletGetResponse;
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
class WalletControllerTest extends IntegrationTest {

    private static final String LOGIN = "test1";
    private static Long walletId;
    private static Long categoryId;

    @Test
    public void getCurrentUser() throws Exception {
        getMock("/api/users/current", LOGIN, status().isOk())
                .andExpect(jsonPath("$.login").value(LOGIN));
    }

    @Test
    public void getEmptyWallets() throws Exception {
        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.walletDtoList").isEmpty())
                .andExpect(jsonPath("$.allConsumption").value(0))
                .andExpect(jsonPath("$.allProfit").value(0));
    }

    @Test
    public void addInvalidWallets() throws Exception {
        WalletCreateRequest provide = new WalletCreateRequest("string", -432);
        postMock("/api/wallets", LOGIN, provide, status().isBadRequest());

        provide = new WalletCreateRequest("string", 0);
        postMock("/api/wallets", LOGIN, provide, status().isBadRequest());

        provide = new WalletCreateRequest("12345678901234567890123456789033", null);
        postMock("/api/wallets", LOGIN, provide, status().isBadRequest());
    }

    @Test
    @Order(0)
    public void addSomeWallet() throws Exception {
        WalletCreateRequest provide = new WalletCreateRequest("string", null);
        postMock("/api/wallets", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("string"))
                .andExpect(jsonPath("$.limit").isEmpty());

        provide = new WalletCreateRequest("string", 50);
        postMock("/api/wallets", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("string"))
                .andExpect(jsonPath("$.limit").value(50));
        // toDo profit and ...

        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.allProfit").value(0))
                .andExpect(jsonPath("$.allConsumption").value(0))
                .andExpect(jsonPath("$.walletDtoList").isNotEmpty())
                .andExpect(jsonPath("$.walletDtoList[0].name").value("string"))
                .andExpect(jsonPath("$.walletDtoList[0].limit").isEmpty())
                .andExpect(jsonPath("$.walletDtoList[0].profit").value(0))
                .andExpect(jsonPath("$.walletDtoList[0].consumption").value(0))
                .andExpect(jsonPath("$.walletDtoList[1].name").value("string"))
                .andExpect(jsonPath("$.walletDtoList[1].limit").value(50))
                .andExpect(jsonPath("$.walletDtoList[1].profit").value(0))
                .andExpect(jsonPath("$.walletDtoList[1].consumption").value(0));
    }

    @Test
    @Order(1)
    public void getEmptyListOfTransactions() throws Exception {
        AllWalletsForUserResponse response = (AllWalletsForUserResponse)
                getObject("/api/users/current/wallets", LOGIN, AllWalletsForUserResponse.class);

        assertEquals(2, response.getWalletDtoList().size());
        assertEquals(0, response.getAllConsumption());
        assertEquals(0, response.getAllProfit());

        for (WalletGetResponse i : response.getWalletDtoList()) {
            getMock("/api/wallets/" + i.getId() + "/transactions", LOGIN, status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Test
    @Order(2)
    public void deleteWallets() throws Exception {
        AllWalletsForUserResponse response = (AllWalletsForUserResponse)
                getObject("/api/users/current/wallets", LOGIN, AllWalletsForUserResponse.class);

        Long walletId = response.getWalletDtoList().get(0).getId();
        deleteMock("/api/wallets/" + walletId, LOGIN, null, status().isOk());

        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.allProfit").value(0))
                .andExpect(jsonPath("$.allConsumption").value(0))
                .andExpect(jsonPath("$.walletDtoList").isNotEmpty())
                .andExpect(jsonPath("$.walletDtoList[0].name").value("string"))
                .andExpect(jsonPath("$.walletDtoList[0].limit").value(50))
                .andExpect(jsonPath("$.walletDtoList[0].profit").value(0))
                .andExpect(jsonPath("$.walletDtoList[0].consumption").value(0));

        walletId = response.getWalletDtoList().get(1).getId();
        deleteMock("/api/wallets/" + walletId, LOGIN, null, status().isOk());

        getMock("/api/users/current/wallets", LOGIN, status().isOk())
                .andExpect(jsonPath("$.allProfit").value(0))
                .andExpect(jsonPath("$.allConsumption").value(0))
                .andExpect(jsonPath("$.walletDtoList").isEmpty());
    }

    @Test
    @Order(3)
    public void addOneCategory() throws Exception {
        CategoryCreateRequest provide = new CategoryCreateRequest("new", 13L, true);
        postMock("/api/categories", LOGIN, provide, status().isOk())
                .andExpect(jsonPath("$.name").value("new"))
                .andExpect(jsonPath("$.pictureId").value(13))
                .andExpect(jsonPath("$.income").value(true));
    }

    @Test
    @Order(4)
    public void addAndCheckEmptyWallet() throws Exception {
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
    @Order(5)
    public void addTransactionsToWallet() throws Exception {
        for (CategoryDto i : getAllCategories(LOGIN)) {
            if (i.isIncome()) {
                categoryId = i.getId();
            }
        }

        List<Integer> toAdd = List.of(100, 200, 300, 400, 500, 600, 700, 800, 900);
        for (Integer i : toAdd) {
            TransactionCreateRequest provide = new TransactionCreateRequest(i, categoryId, walletId, 1629888829L);
            postMock("/api/transactions", LOGIN, provide, status().isOk());
        }

        getMock("/api/wallets/" + walletId, LOGIN, status().isOk())
                .andExpect(jsonPath("$.profit").value(toAdd.stream().mapToInt(Integer::intValue).sum()))
                .andExpect(jsonPath("$.consumption").value(0));

        getMock("/api/wallets/" + walletId + "/transactions", LOGIN, status().isOk())
                .andExpect(jsonPath("$").value(hasSize(toAdd.size())));
    }

    @Test
    @Order(6)
    public void addInvalidTransactionsToWallet() throws Exception {
        TransactionCreateRequest provide = new TransactionCreateRequest(-150, categoryId, walletId, 1629888829L);
        postMock("/api/transactions", LOGIN, provide, status().isBadRequest());

        provide = new TransactionCreateRequest(100, -4L, walletId, 1629888829L);
        postMock("/api/transactions", LOGIN, provide, status().isNotFound());

        provide = new TransactionCreateRequest(100, categoryId, 344324235L, 1629888829L);
        postMock("/api/transactions", LOGIN, provide, status().isNotFound());

        provide = new TransactionCreateRequest(null, categoryId, 344324235L, 1629888829L);
        postMock("/api/transactions", LOGIN, provide, status().isBadRequest());
    }

    @Test
    @Order(7)
    public void deleteWalletAndCheckDeletedTransactions() throws Exception {
        List<TransactionGetResponse> transactionGetResponseDtos =
                getList("/api/wallets/" + walletId + "/transactions", LOGIN, TransactionGetResponse.class);

        deleteMock("/api/wallets/" + walletId, LOGIN, null, status().isOk());
        getMock("/api/wallets/" + walletId + "/transactions", LOGIN, status().isNotFound());

        for (TransactionGetResponse i : transactionGetResponseDtos) {
            getMock("/api/transactions/" + i.getId(), LOGIN, status().isNotFound());
        }
    }
}