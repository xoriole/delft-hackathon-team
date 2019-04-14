package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.repository.CitizenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CitizenServiceTest {

    @TestConfiguration
    static class CitizenServiceTestContextConfiguration {

        @Bean
        public CitizenService citizenService() {
            return new CitizenService();
        }
    }

    @Autowired
    private CitizenService citizenService;

    @MockBean
    private CitizenRepository citizenRepository;

    @Before
    public void setUp() {
        Citizen citizen = new Citizen();
        citizen.setFirstname("First");
        citizen.setLastname("Last");
        citizen.setDateOfBirth(Calendar.getInstance().getTime());
        citizen.setPlaceOfBirth("Delft");
        citizen.setPassportPhoto("photo_url_here");
        citizen.setBsn("1100.00.101");

        Mockito.when(citizenRepository.findByBsn(citizen.getBsn()))
                .thenReturn(citizen);
    }

    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String bsn = "1100.00.101";
        Citizen found = citizenService.getCitizenByBSN(bsn);

        assertThat(found.getBsn())
                .isEqualTo(bsn);
    }
}