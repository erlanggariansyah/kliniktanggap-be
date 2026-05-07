package com.albert.kliniktanggap.repository;

import com.albert.kliniktanggap.entity.WeightConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightConfigRepository extends JpaRepository<WeightConfig, Long> {
}
