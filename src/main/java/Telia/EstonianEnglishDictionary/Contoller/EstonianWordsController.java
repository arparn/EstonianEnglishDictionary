package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Service.EnglishWordService;
import Telia.EstonianEnglishDictionary.Service.EstonianWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("estonian-english")
@RestController
public class EstonianWordsController {

    @Autowired
    private EnglishWordService englishWordService;

    @Autowired
    private EstonianWordService estonianWordService;

    @GetMapping
    public List<EstonianWord> getAll() {
        return estonianWordService.getAll();
    }

    @PostMapping
    public EstonianWord addWord(@RequestParam(value = "word") String word,
                                @RequestParam(value = "translation") String translation,
                                @RequestParam(value = "add") Boolean add) {
        if (add) {
            englishWordService.addWord(translation, word);
        }
        return estonianWordService.addWord(word.toLowerCase(), translation.toLowerCase());
    }

    @GetMapping("/translate")
    public Map<String, Object> getTranslation(@RequestParam(value = "word") String word) {
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
