package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Service.EnglishWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("english-estonian")
@RestController
public class EnglishWordsController {

    @Autowired
    private EnglishWordService englishWordService;

    @GetMapping
    public List<EnglishWord> getAll() {
        return englishWordService.getAll();
    }

    @PostMapping
    public EnglishWord addWord(@RequestParam(value = "word") String word,
                               @RequestParam(value = "translation") String translation) {
        return englishWordService.addWord(word.toLowerCase(), translation.toLowerCase());
    }

    @GetMapping("/translate")
    public List<String> getTranslation(@RequestParam(value = "word") String word) {
        return englishWordService.translate(word.toLowerCase());
    }
}
