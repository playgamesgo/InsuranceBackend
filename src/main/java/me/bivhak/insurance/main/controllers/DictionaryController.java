package me.bivhak.insurance.main.controllers;

import me.bivhak.insurance.main.models.Dictionary;
import me.bivhak.insurance.main.payload.response.MessageResponse;
import me.bivhak.insurance.main.services.DictionaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getDictionaryByName(@PathVariable String name) {
        return switch (name) {
            case "health" -> ResponseEntity.ok(dictionaryService.findByName("health"));
            case "travel" -> ResponseEntity.ok(dictionaryService.findByName("travel"));
            case "housing" -> ResponseEntity.ok(dictionaryService.findByName("housing"));
            case "animals" -> ResponseEntity.ok(dictionaryService.findByName("animals"));
            default -> ResponseEntity.badRequest().body(new MessageResponse("Dictionary not found"));
        };
    }
}
