package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Service.EnglishWordService;
import Telia.EstonianEnglishDictionary.Service.EstonianWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("english-estonian")
@RestController
public class EnglishWordsController {

    @Autowired
    private EstonianWordService estonianWordService;

    @Autowired
    private EnglishWordService englishWordService;

    @GetMapping
    public List<EnglishWord> getAll() {
        return englishWordService.getAll();
    }

    @PostMapping
    public EnglishWord addWord(@RequestParam(value = "word") String word,
                               @RequestParam(value = "translation") String translation,
                               @RequestParam(value = "add") Boolean add) {
        if (add) {
            estonianWordService.addWord(translation, word);
        }
        return englishWordService.addWord(word.toLowerCase(), translation.toLowerCase());
    }

    @GetMapping("/translate")
    public Map<String, Object> getTranslation(@RequestParam(value = "word") String word) {
        return englishWordService.translate(word.toLowerCase());
    }

    @DeleteMapping("{id}")
    public void deleteWordAndTranslations(@PathVariable Long id) {
        englishWordService.deleteWordAndTranslations(id);
    }

    @DeleteMapping("{id}/{translationId}")
    public void deleteTranslations(@PathVariable Long id,
                                   @PathVariable Long translationId) {
        englishWordService.deleteTranslation(id, translationId);
    }
}
