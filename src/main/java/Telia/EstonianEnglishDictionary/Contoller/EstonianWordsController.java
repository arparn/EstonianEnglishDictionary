package Telia.EstonianEnglishDictionary.Contoller;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Service.EstonianWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
