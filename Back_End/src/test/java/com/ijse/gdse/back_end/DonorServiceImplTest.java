package com.ijse.gdse.back_end;


import com.ijse.gdse.back_end.dto.DonorDTO;
import com.ijse.gdse.back_end.entity.Donor;
import com.ijse.gdse.back_end.repository.DonorRepository;
import com.ijse.gdse.back_end.service.impl.DonorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonorServiceImplTest {

    @InjectMocks
    private DonorServiceImpl donorService;

    @Mock
    private DonorRepository donorRepository;

    private Donor donor;
    private DonorDTO donorDTO;

    @BeforeEach
    public void setUp() {
        donorDTO = DonorDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .amount(BigDecimal.valueOf(100.0))
                .build();


        donor = Donor.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .amount(BigDecimal.valueOf(100.0))
                .build();
    }

    @Test
    void shouldAddDonor() {
        when(donorRepository.save(any(Donor.class))).thenReturn(donor);

        Donor savedDonor = donorService.addDonor(donorDTO);

        Assertions.assertNotNull(savedDonor);
        Assertions.assertEquals(donor.getId(), savedDonor.getId());
        Assertions.assertEquals(donor.getName(), savedDonor.getName());
        verify(donorRepository, times(1)).save(any(Donor.class));
    }

    @Test
    void shouldGetDonorById() {
        when(donorRepository.findById(1L)).thenReturn(Optional.of(donor));

        Donor foundDonor = donorService.getDonorById(1L);

        Assertions.assertNotNull(foundDonor);
        Assertions.assertEquals(donor.getName(), foundDonor.getName());
        verify(donorRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnAllDonors() {
        List<Donor> mockList = Arrays.asList(donor);
        when(donorRepository.findAll()).thenReturn(mockList);

        List<Donor> donors = donorService.getAllDonors();

        Assertions.assertNotNull(donors);
        Assertions.assertEquals(1, donors.size());
        verify(donorRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateDonor() {
        DonorDTO updateDTO = DonorDTO.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .amount(BigDecimal.valueOf(200.0))
                .build();

        Donor updatedDonor = Donor.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .amount(BigDecimal.valueOf(200.0))
                .build();

        when(donorRepository.findById(1L)).thenReturn(Optional.of(donor));
        when(donorRepository.save(any(Donor.class))).thenReturn(updatedDonor);

        Donor result = donorService.updateDonor(1L, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Jane Doe", result.getName());
        Assertions.assertEquals("jane@example.com", result.getEmail());
        Assertions.assertEquals(BigDecimal.valueOf(200.0), result.getAmount());
        verify(donorRepository, times(1)).findById(1L);
        verify(donorRepository, times(1)).save(any(Donor.class));
    }


    @Test
    void shouldDeleteDonor() {
        when(donorRepository.findById(1L)).thenReturn(Optional.of(donor));
        doNothing().when(donorRepository).delete(donor);

        donorService.deleteDonor(1L);

        verify(donorRepository, times(1)).findById(1L);
        verify(donorRepository, times(1)).delete(donor);
    }

    @Test
    void shouldCountDonors() {
        when(donorRepository.count()).thenReturn(5L);

        long count = donorService.countDonors();

        Assertions.assertEquals(5L, count);
        verify(donorRepository, times(1)).count();
    }
}
