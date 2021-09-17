package sirius.tinkoff.financial.tracker.service.converter;

import org.springframework.stereotype.Component;
import sirius.tinkoff.financial.tracker.dao.dto.request.WalletCreateRequest;
import sirius.tinkoff.financial.tracker.dao.dto.response.WalletGetResponse;
import sirius.tinkoff.financial.tracker.dao.entity.WalletEntity;

@Component
public class WalletConverter {

    public WalletGetResponse converter(WalletEntity entity) {
        return new WalletGetResponse()
                .setId(entity.getId())
                .setName(entity.getName())
                .setProfit(entity.getProfit())
                .setConsumption(entity.getConsumption())
                .setLimit(entity.getLimit());
    }

    public WalletEntity converterCreate(WalletCreateRequest wallet) {
        return new WalletEntity()
                .setName(wallet.getName())
                .setLimit(wallet.getLimit());
    }

}
