package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Service.EstonianWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("estonian-english")
@RestController
public class EstonianWordsController {

    @Autowired
    private EstonianWordService estonianWordService;

    @GetMapping
    public List<EstonianWord> getAll() {
        return estonianWordService.getAll();
    }

    @PostMapping
    public EstonianWord addWord(@RequestParam(value = "word") String word,
                                @RequestParam(value = "translation") String translation) {
        return estonianWordService.addWord(word.toLowerCase(), translation.toLowerCase());
    }

    @GetMapping("/translate")
    public List<Translation> getTranslation(@RequestParam(value = "word") String word) {
        return estonianWordService.translate(word.toLowerCase());
    }

    @DeleteMapping("{id}")
    public void deleteWordAndTranslations(@PathVariable Long id) {
        estonianWordService.deleteWordAndTranslations(id);
    }

    @DeleteMapping("{id}/{translationId}")
    public void deleteTranslations(@PathVariable Long id,
                                   @PathVariable Long translationId) {
        estonianWordService.deleteTranslation(id, translationId);
    }
}
