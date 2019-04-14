package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CitizenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CitizenRepository citizenRepository;

    @Test
    public void whenFindByBSN_thenReturnCitizen() {
        // given
        Citizen citizen = new Citizen();
        citizen.setFirstname("First");
        citizen.setLastname("Last");
        citizen.setDateOfBirth(Calendar.getInstance().getTime());
        citizen.setPlaceOfBirth("Delft");
        citizen.setPassportPhoto("photo_url_here");
        citizen.setBsn("1100.00.101");

        entityManager.persist(citizen);
        entityManager.flush();

        // when
        Citizen found = citizenRepository.findByBsn(citizen.getBsn());

        // then
        assertThat(found.getBsn())
                .isEqualTo(citizen.getBsn());
    }
}