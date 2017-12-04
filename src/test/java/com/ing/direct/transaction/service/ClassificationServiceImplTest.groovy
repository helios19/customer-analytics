package com.ing.direct.transaction.service

import com.ing.direct.transaction.model.Transaction
import spock.lang.Specification
import spock.lang.Unroll

import static com.ing.direct.common.utils.ClassUtils.toDate

class ClassificationServiceImplTest extends Specification {

    def classificationService = new ClassificationServiceImpl()


    def static transaction_afternoon_person = [
            Transaction.builder().id("1").customer("22").date(toDate("31/05/2016 9:15:24 PM"))
                    .amount(BigDecimal.valueOf(-5.97)).description("LOREM NUNC DOLOR CURSUS").build(),
            Transaction.builder().id("2").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(-15.31)).description("LOREM IN").build(),
            Transaction.builder().id("3").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(1328.4)).description("LOREM NUNC DOLOR CURSUS").build(),
            Transaction.builder().id("4").customer("22").date(toDate("31/05/2016 9:15:24 PM"))
                    .amount(BigDecimal.valueOf(-16.47)).description("NUNC FAUCIBUS").build()
    ]

    def static transaction_morning_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(-19.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 10:35:24 AM"))
                    .amount(BigDecimal.valueOf(-1.42)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(-17.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(-18.24)).description("SEM NUNC").build()
    ]

    def static transaction_bigspender_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(39.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(-19.16)).description("INTERDUM").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 10:35:24 AM"))
                    .amount(BigDecimal.valueOf(-1.42)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(-17.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("9").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(-18.24)).description("SEM NUNC").build()
    ]

    def static transaction_bigticketspender_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(139.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(1000.42)).description("INTERDUM").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 10:35:24 AM"))
                    .amount(BigDecimal.valueOf(-1328.4)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(117.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("9").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(1118.24)).description("SEM NUNC").build()
    ]

    def static transaction_potentialloan_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(39.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(1000.42)).description("INTERDUM").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 10:35:24 AM"))
                    .amount(BigDecimal.valueOf(-1328.4)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(-17.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("9").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(-18.24)).description("SEM NUNC").build()
    ]

    def static transaction_fastspender_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(139.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(1000.42)).description("INTERDUM").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 10:35:24 AM"))
                    .amount(BigDecimal.valueOf(-128.4)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(117.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("9").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(1118.24)).description("SEM NUNC").build()
    ]

    def static transaction_pootentialsaver_person = [
            Transaction.builder().id("5").customer("22").date(toDate("31/05/2016 6:28:17 AM"))
                    .amount(BigDecimal.valueOf(139.16)).description("INTERDUM").build(),
            Transaction.builder().id("6").customer("22").date(toDate("31/05/2016 6:28:17 PM"))
                    .amount(BigDecimal.valueOf(1000.42)).description("INTERDUM").build(),
            Transaction.builder().id("7").customer("22").date(toDate("31/05/2016 10:35:24 PM"))
                    .amount(BigDecimal.valueOf(-12.4)).description("AUGUE MAGNA").build(),
            Transaction.builder().id("8").customer("22").date(toDate("31/05/2016 6:33:26 PM"))
                    .amount(BigDecimal.valueOf(117.58)).description("IPSUM NON NON").build(),
            Transaction.builder().id("9").customer("22").date(toDate("31/05/2016 6:20:23 AM"))
                    .amount(BigDecimal.valueOf(1118.24)).description("SEM NUNC").build()
    ]


    @Unroll
    def "should classify customer"() {

        given:

        when: "invoking classification service"
        ClassificationEnum[] classificationEnums = classificationService.classifyCustomer(transactions)

        then:
        classificationEnums.sort() == expectClassification.sort()

        where:
        transactions                        | month | expectClassification
        transaction_afternoon_person        | 5     | [ClassificationEnum.AFTERNOON_PERSON, ClassificationEnum.POTENTIAL_SAVER]
        transaction_morning_person          | 5     | [ClassificationEnum.MORNING_PERSON]
        transaction_bigspender_person       | 5     | [ClassificationEnum.BIG_SPENDER, ClassificationEnum.MORNING_PERSON]
        transaction_bigticketspender_person | 5     | [ClassificationEnum.BIG_TICKET_SPENDER, ClassificationEnum.FAST_SPENDER, ClassificationEnum.MORNING_PERSON]
        transaction_potentialloan_person    | 5     | [ClassificationEnum.POTENTIAL_LOAN, ClassificationEnum.BIG_TICKET_SPENDER, ClassificationEnum.MORNING_PERSON,]
        transaction_fastspender_person      | 5     | [ClassificationEnum.FAST_SPENDER, ClassificationEnum.MORNING_PERSON, ClassificationEnum.POTENTIAL_SAVER]
        transaction_pootentialsaver_person  | 5     | [ClassificationEnum.POTENTIAL_SAVER, ClassificationEnum.AFTERNOON_PERSON]


    }
}