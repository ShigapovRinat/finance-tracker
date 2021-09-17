package sirius.tinkoff.financial.tracker.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long pictureId;

    @Column(nullable = false)
    private boolean isIncome;

    @OneToMany(mappedBy = "category")
    private List<TransactionEntity> transactions;

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
