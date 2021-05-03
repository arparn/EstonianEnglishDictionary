package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Service.EnglishWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
