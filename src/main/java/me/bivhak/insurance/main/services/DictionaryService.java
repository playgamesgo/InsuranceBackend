package me.bivhak.insurance.main.services;

import me.bivhak.insurance.main.models.Dictionary;
import me.bivhak.insurance.main.repository.DictionaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    public DictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public List<String> findByName(String name) {
        return switch (name) {
            case "health" -> dictionaryRepository.findAll().getFirst().getHealth();
            case "travel" -> dictionaryRepository.findAll().getFirst().getTravel();
            case "housing" -> dictionaryRepository.findAll().getFirst().getHousing();
            case "animals" -> dictionaryRepository.findAll().getFirst().getAnimals();
            default -> null;
        };
    }

    public void saveDictionary(String name, List<String> values) {
        Dictionary dictionary = new Dictionary();
        switch (name) {
            case "health":
                dictionary.setHealth(values);
                break;
            case "travel":
                dictionary.setTravel(values);
                break;
            case "housing":
                dictionary.setHousing(values);
                break;
            case "animals":
                dictionary.setAnimals(values);
                break;
            default:
                return;
        }
        dictionaryRepository.save(dictionary);
    }
}
