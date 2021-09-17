package sirius.tinkoff.financial.tracker.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "wallet")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_seq")
    @SequenceGenerator(name = "wallet_seq", sequenceName = "wallet_seq", allocationSize = 1)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions;

    @Column(nullable = false, insertable = false)
    private Integer profit;

    @Column(nullable = false, insertable = false)
    private Integer consumption;

    @Column(name = "limitation")
    private Integer limit;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserEntity user;

    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
