package com.ing.direct.transaction.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.ing.direct.common.utils.ClassUtils.DATE_FORMAT_PATTERN;

/**
 * Plain java class representing an transaction resource.
 * <p>
 * <p>This class also declares a compound-index based on {@code customer} and {@code date} fields.</p>
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "transaction_customer_date_idx", def = "{'customer': 1, 'date': 1}")
})
@Data
@Builder
public class Transaction {
    @Id
    private String id;

    @Field
    @Indexed
    @NotNull
    private String customer;

    @Field
    @Indexed
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT_PATTERN)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @Field
    @NotNull
    private BigDecimal amount;

    @Field
    @NotNull
    private String description;
}
