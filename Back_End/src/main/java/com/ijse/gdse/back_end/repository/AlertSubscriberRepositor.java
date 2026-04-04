package com.ijse.gdse.back_end.repository;

import com.ijse.gdse.back_end.entity.AlertSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertSubscriberRepositor extends JpaRepository<AlertSubscriber, Long> {
    // නගරය අනුව Active පරිශීලකයින් ලැයිස්තුවක් ලබා ගැනීම
    List<AlertSubscriber> findByCityAndActiveTrue(String city);
}
