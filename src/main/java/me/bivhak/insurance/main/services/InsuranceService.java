package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Insurance;
import me.bivhak.insurance.main.repository.InsuranceRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;

    public InsuranceService(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public void save(Insurance insurance) {
        insuranceRepository.save(insurance);
    }

    public Optional<Insurance> findById(Long insuranceId) {
        return insuranceRepository.findById(insuranceId);
    }

    public void delete(Insurance insurance) {
        insuranceRepository.delete(insurance);
    }

    public Collection<Insurance> findAll() {
        return insuranceRepository.findAll();
    }
}
