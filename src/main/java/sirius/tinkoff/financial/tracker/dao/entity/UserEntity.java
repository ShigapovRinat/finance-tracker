package sirius.tinkoff.financial.tracker.dao.entity;

import lombok.EqualsAndHashCode;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "client")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String surname;

    @Column(name = "login", unique = true)
    private String login;

    @OneToMany(mappedBy = "user")
    private List<WalletEntity> wallets;

    @Column(name = "phone_number", length = 20, nullable = false)
    private Long phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> categories;

    @Getter
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
