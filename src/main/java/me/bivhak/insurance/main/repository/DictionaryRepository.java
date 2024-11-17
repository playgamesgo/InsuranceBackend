package me.bivhak.insurance.main.repository;

import me.bivhak.insurance.main.models.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
}